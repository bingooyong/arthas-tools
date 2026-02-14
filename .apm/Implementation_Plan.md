# arthas-tools – APM Implementation Plan
**Memory Strategy:** Dynamic-MD
**Last Modification:** Manager Agent – Phase 06 completed; Phase 06 summary appended to Memory_Root. All six phases complete.
**Project Overview:** 独立 Java 工具，通过 Attach API 附加到指定 Java 进程，在不重启、不修改目标代码的前提下，按「类+字段/方法」定位对象并将 Map/List/Set/Collection/数组等内容导出到可配置路径；参考 Arthas 思路、无第三方 Attach 依赖；首版 P0 功能 + 可扩展设计，代码遵循阿里巴巴规约与 Google Java Style。

---

## Phase 1: Project Structure & Build

### Task 1.1 – Create Maven root and multi-module structure - Agent_Build
- **Objective:** Establish a buildable Maven multi-module skeleton with parent and agent/attacher modules.
- **Output:** Root `pom.xml` (packaging `pom`), `agent/` and `attacher/` module directories each with a `pom.xml`, and `<modules>` in parent.
- **Guidance:** Create project root with parent POM; add `agent` and `attacher` as submodules with correct `<parent>` reference. No business code yet. Follow Alibaba + Google Java Style for any POM structure conventions.

- Create project root and root `pom.xml` with `<packaging>pom</packaging>` and `<modules><module>agent</module><module>attacher</module></modules>`.
- Add `agent` directory with `agent/pom.xml` whose `<parent>` is the root project; set `artifactId` (e.g. `arthas-tools-agent`).
- Add `attacher` directory with `attacher/pom.xml` whose `<parent>` is the root project; set `artifactId` (e.g. `arthas-tools-attacher`).
- Ensure `mvn -q compile` at root succeeds (empty modules are compilable).

### Task 1.2 – Configure parent POM (JDK, encoding, compiler) - Agent_Build
- **Objective:** Centralize JDK version, encoding, and compiler settings in the parent POM.
- **Output:** Parent POM with `maven.compiler.source`/`target` 8, UTF-8 encoding, and `maven-compiler-plugin` configuration.
- **Guidance:** **Depends on: Task 1.1 Output.** Use Java 8 compatibility; set `project.build.sourceEncoding` to UTF-8; add `maven-compiler-plugin` with source/target 8 (or 1.8). Optionally add empty `dependencyManagement` for future use.

- In parent POM set `maven.compiler.source` and `maven.compiler.target` to `8` (or `1.8`) via properties; set `project.build.sourceEncoding` to `UTF-8`.
- Add `maven-compiler-plugin` in `<build><plugins>` with same source/target.
- Optionally add `<dependencyManagement>` with no dependencies for later use.
- Run `mvn -q compile` at root to verify.

---

## Phase 2: Agent Module

### Task 2.1 – Agent entry and argument parsing - Agent_Agent
- **Objective:** Implement agentmain and parse agentArgs into class name, field/method name, output path, and result file path.
- **Output:** Agent entry class with agentmain, argument parsing, and validation; failed validation writes to result file and exits.
- **Guidance:** Use `agentmain(String agentArgs, Instrumentation inst)`. Define agentArgs format (e.g. key=value or ordered); parse and validate class name, field name or method name, output path, **result file path (required for Attacher feedback contract)**; on validation failure write result file with failure reason and return. Contract must align with Attacher (Task 3.1/3.3) so result path is passed via CLI and read after loadAgent.

- Implement agentmain and delegate to an internal bootstrap that parses agentArgs.
- Define and document agentArgs format (e.g. `class=...&field=...` or `outputPath=...&resultPath=...`); parse into typed parameters; **include resultPath so Attacher can pass --result-file and read feedback**.
- Validate required parameters; if invalid, write "FAIL <reason>" to result path (if provided) and exit.
- Ensure code follows Alibaba Java guidelines and Google Java Style.

### Task 2.2 – Locate target object by reflection - Agent_Agent
- **Objective:** Given class name and field name (or method name), obtain the target object (Map/List/Set/Collection or array) via reflection.
- **Output:** Reflection-based object locator supporting static field and static method return; clear error when type is unsupported.
- **Guidance:** **Depends on: Task 2.1 Output.** Load target class; get static field value **or** invoke static method (PRD: "类 + 字段名" or "类 + 方法名"); validate that the result is a supported type (Map, List, Set, Collection, or array); throw or return clear error if not.

- Load the target class by name; resolve **either** static field **or** static method (per agentArgs: field name vs method name).
- If field: get static field value; if method: invoke and take return value.
- Check type (Map, List, Set, Collection, or array); otherwise fail with a clear message.
- Keep logic in a dedicated class for testability and clarity.

### Task 2.3 – Export strategy interface and Map/List implementations - Agent_Agent
- **Objective:** Define export strategy interface and implement Map and List exporters; register and dispatch by type.
- **Output:** Strategy interface, Map and List implementations, and a registry/dispatcher that selects by object type.
- **Guidance:** **Depends on: Task 2.2 Output.** Define an interface that takes the object and produces export content (e.g. string or stream); implement for Map (e.g. key=value or JSON) and List (ordered lines or JSON); register and provide a single entry that selects strategy by runtime type. Leave extension point for Set/Collection/array.

- Define export strategy interface (e.g. accepts object, writes to Appendable or returns String).
- Implement Map strategy (key-value pairs, format configurable or fixed per PRD); implement List strategy (ordered elements).
- Register strategies and provide a dispatcher that chooses by object.getClass() or type checks.
- Ensure extensibility for Set, Collection, and array in a later task.

### Task 2.4 – Export strategies for Set, Collection, and array - Agent_Agent
- **Objective:** Implement and register export strategies for Set, Collection, and array types.
- **Output:** Set, Collection, and array strategies registered; same export format style as Map/List where applicable.
- **Guidance:** **Depends on: Task 2.3 Output.** Implement iteration-based export for Set and Collection; handle Object[] and primitive arrays; register in the same registry used in 2.3.

- Implement Set and Collection exporters (iterate and write elements, consistent with List style).
- Implement array exporter for Object[] and primitive arrays (e.g. iterate and write elements).
- Register all in the strategy registry; reuse same format conventions (e.g. one item per line or JSON).

### Task 2.5 – File writer and result feedback - Agent_Agent
- **Objective:** Write exported content to the specified path and write "OK <path>" or "FAIL <reason>" to the result file for Attacher.
- **Output:** Utility that creates parent dirs, writes content to output path, and writes result line to result file path.
- **Guidance:** **Depends on: Task 2.3 Output (content format).** Accept export content and output path; create parent directory if needed; write content; write "OK <outputPath>" or "FAIL <reason>" to result file path (from agentArgs). Use UTF-8 and safe file I/O.

- Implement writing of export content to user-specified output path; create parent directories if necessary.
- Write a single result line to the result file path: "OK <path>" on success or "FAIL <message>" on failure.
- Use try-with-resources and clear exception handling; follow project code style.

### Task 2.6 – Agent integration and error handling - Agent_Agent
- **Objective:** Wire parsing → locate → export → write and result; centralize exception handling so result file is always written on failure.
- **Output:** Integrated agent flow with end-to-end error handling and result feedback.
- **Guidance:** **Depends on: Tasks 2.1, 2.2, 2.3, 2.4, 2.5 Output.** Invoke parser → locator → strategy selection → export → file writer; wrap in try/catch; on any exception write "FAIL <message>" to result file so Attacher can show it.

- In agent bootstrap, call parser (2.1) → locator (2.2) → select strategy (2.3/2.4) → export → writer (2.5).
- Wrap full flow in try/catch; on exception write "FAIL <exception message>" to result path.
- Ensure result file is written in both success and failure cases so Attacher has consistent feedback.

---

## Phase 3: Attacher Module

### Task 3.1 – CLI argument parsing - Agent_Attacher
- **Objective:** Parse and validate command-line arguments: PID, class name, field or method name, output path, Agent JAR path, result file path.
- **Output:** Parsed and validated CLI parameters; usage message and exit on validation failure.
- **Guidance:** Define options (e.g. --pid, --class, --field/--method, --output, --agent-jar, --result-file); validate PID is numeric, paths present; print usage and exit on failure. No external platform; code only.

- Define CLI options (e.g. PID, class, field or method, output path, agent JAR path, result file path); use library or hand-written parsing.
- Validate required args and formats (e.g. PID numeric, output path writable); print usage and exit on failure.
- Follow Alibaba + Google Java Style.

### Task 3.2 – Attach to target JVM and load Agent - Agent_Attacher
- **Objective:** Use VirtualMachine.attach(pid) and loadAgent(agentJarPath, agentArgs); handle attach/load failures with clear messages.
- **Output:** Attach and load logic with AttachException and load failure handling; agentArgs format matches Agent (2.1) contract.
- **Guidance:** **Depends on: Task 3.1 Output.** Use com.sun.tools.attach.VirtualMachine; build agentArgs string (include result file path); call loadAgent; catch exceptions and print clear error (e.g. "Attach failed: ..." / "Load agent failed: ..."). **JDK 8:** Attach API in `tools.jar` (must be on classpath). **JDK 9+:** in `jdk.attach` module, no separate JAR. Document in build/docs.

- Obtain VirtualMachine for given PID via VirtualMachine.attach(pid).
- Build agentArgs string consistent with Agent's parser (class, field/method, output path, result path).
- Call vm.loadAgent(agentJarPath, agentArgs); catch AttachException and other exceptions; print explicit error messages.
- Document Attach API requirement: JDK 8 = tools.jar on classpath; JDK 9+ = use module path.

### Task 3.3 – Read result file and print feedback - Agent_Attacher
- **Objective:** After loadAgent returns, read the result file and print "导出成功，路径：xxx" or "导出失败：原因" to console.
- **Output:** Result file reader and console feedback in Chinese per PRD F7.
- **Guidance:** **Depends on: Task 3.2 Output.** After loadAgent, wait for result file with **bounded timeout** (e.g. poll up to 5s) to avoid infinite wait if Agent crashes; parse "OK <path>" or "FAIL <msg>"; print corresponding Chinese message; optionally delete result file.

- After loadAgent returns, wait for result file with a **timeout** (e.g. 5 seconds poll); read first line.
- If line starts with "OK ", print "导出成功，路径：<path>"; if "FAIL ", print "导出失败：<message>".
- Handle missing or malformed result file: on timeout print e.g. "导出失败：未收到结果（超时）"; do not block indefinitely.

---

## Phase 4: Packaging & Run

### Task 4.1 – Package Agent JAR with Agent-Class - Agent_Packaging
- **Objective:** Produce a standalone Agent JAR with manifest Agent-Class for loading by Attacher.
- **Output:** agent module JAR with MANIFEST.MF Agent-Class pointing to the agentmain class; minimal or no external dependencies.
- **Guidance:** **Depends on: Task 2.6 Output by Agent_Agent.** In agent module POM, configure maven-jar-plugin (or equivalent) to set Manifest Agent-Class to the class that implements agentmain; run mvn package to produce the JAR. **Keep agent JAR dependencies minimal (JDK only)** so it loads in the target JVM without classpath conflicts.

- Configure agent module build to add Manifest entry Agent-Class with the fully qualified class name of the agent entry.
- Ensure the JAR contains the agent entry and all agent code; **do not add third-party dependencies** so the Agent loads cleanly in any target JVM (JDK only).
- Verify with `jar tf` or by loading in a test JVM that the manifest is correct.

### Task 4.2 – Package Attacher runnable JAR and run instructions - Agent_Packaging
- **Objective:** Produce runnable Attacher JAR and document how to run with Agent JAR location and example commands.
- **Output:** Attacher JAR (with dependencies), README or doc snippet describing layout (Agent JAR + Attacher JAR) and example CLI commands.
- **Guidance:** **Depends on: Task 3.3 Output by Agent_Attacher; Task 4.1 Output.** Use maven-assembly-plugin or maven-shade-plugin for Attacher; document that Agent JAR path is passed via CLI (e.g. --agent-jar) or default (e.g. same directory); add example command with PID, class, field, output path.

- Configure Attacher module to build an executable JAR with dependencies (e.g. main class, classpath in manifest or shaded).
- Document in README or docs: place Agent JAR and Attacher JAR (e.g. same dir); example: `java -jar attacher.jar --pid 1234 --class com.example.Cache --field map --output /tmp/out.txt --agent-jar ./agent.jar`.
- Include note on result file path (e.g. --result-file /tmp/result.txt) if required by contract.

---

## Phase 5: Documentation

### Task 5.1 – README (intro, build, run, Arthas relation) - Agent_Docs
- **Objective:** Provide README with project intro, background, build and run instructions, and relation to Arthas.
- **Output:** README.md with overview, build steps (mvn package), run layout, and Arthas reference note.
- **Guidance:** **Depends on: Task 4.2 Output.** Write in Chinese or bilingual as per project; link to detailed CLI doc if in separate file. No external platform.

- Write project name and one-paragraph intro and background (e.g. export in-process cache without restart).
- **List supported target types** from Context Synthesis: Map, List, Set, Collection, array (extensible design).
- Document build: run `mvn package` at root; mention agent and attacher JARs output locations.
- Document run: place Agent and Attacher JARs; point to example command and/or CLI doc; briefly list main parameters.
- Add short "Relation to Arthas" note: reference only, same Attach/Agent idea, no dependency.

### Task 5.2 – CLI usage and security/permission recommendations - Agent_Docs
- **Objective:** Document all CLI parameters, example commands, and security/permission recommendations for output path and sensitive data.
- **Output:** CLI usage doc (in README or docs/) with parameters, examples, and security notes.
- **Guidance:** **Depends on: Task 4.2 Output.** List every option with description; give 2–3 example commands; state that output path should be restricted and content may be sensitive; recommend file permissions and storage.

- List all Attacher CLI parameters with short descriptions (--pid, --class, --field/--method, --output, --agent-jar, --result-file, etc.); **document that --field and --method are mutually exclusive (exactly one required)**.
- Provide 2–3 example commands (different target types or paths).
- Add "Security and permissions" section: restrict output directory access; exported content may contain keys/sensitive data; recommend permissions (e.g. 600) and safe storage; **note known risks: Attach may require same user/permissions as target process; container or restricted environments may block Attach** (from Context Synthesis).

---

## Phase 6: Test & Acceptance

**Purpose:** 通过独立 Demo 进程验证工具可用性：用工具导出指定内存结构并校验内容与预期一致，且 **不破坏目标进程**（只读、不修改目标 JVM）。验收仅在 Demo 上执行，禁止对已有生产/含敏感数据的服务做验收。

**JDK 全量堆 dump 说明：** JDK 提供全堆 dump 命令（如 `jmap -dump:format=b,file=heap.hprof <pid>` 或 `jcmd <pid> GC.heap_dump <file>`），会生成堆快照文件，目标进程在 dump 后继续运行（可能短暂停顿）。本工具与全堆 dump 不同：仅读取用户指定的「类+字段/方法」对应对象并导出为文本，**不修改目标进程内存**，也不 dump 整个堆；适用于按需导出单个 Map/List 等，风险低于全堆 dump。全堆 dump 用于其他诊断场景，此处不要求实现。

### Task 6.1 – Demo application (in-process cache with known content) - Agent_Test
- **Objective:** 提供一个可独立运行的 Demo 应用，进程内持有已知内容的静态 Map（及可选 List/Set），进程持续运行以便被 Attacher 附加并导出。
- **Output:** 可运行的 Demo 主类（含 main），静态字段或静态方法返回 Map/List 且内容可预期（如固定 key/value 或有序元素）；启动后保持运行（如 sleep 循环或等待），不依赖现有业务服务。
- **Guidance:** **Depends on: Task 4.2 Output (run layout).** Demo 与主工程分离（如 `demo/` 模块或 `demo/` 目录下独立可执行 JAR/class），避免与 agent/attacher 代码耦合。内容需可预期以便验收时比对（例如 Map 含 "key1"="value1", "key2"="value2"；List 含 ["a","b","c"]）。**禁止在含真实密钥或生产数据的进程上做验收。**

- 实现 Demo 主类：定义 public static 的 Map（及可选 List/Set）并填入已知数据；或提供 static 方法返回上述结构。
- main 中启动后保持运行（如 `while (true) { Thread.sleep(60_000); }` 或类似），便于获取 PID 并执行 Attacher。
- 文档说明 Demo 用途：仅用于验收工具导出结果与进程存活，不用于生产。

### Task 6.2 – Acceptance procedure: run tool, verify output and process health - Agent_Test
- **Objective:** 定义并执行验收步骤：启动 Demo → 用 Attacher 导出指定类/字段（或方法）→ 校验导出文件内容与预期一致 → 校验 Demo 进程仍存活且未异常退出。
- **Output:** 验收步骤文档（可选：可执行脚本），包含：启动 Demo、获取 PID、调用 Attacher（--pid、--class、--field/--method、--output、--agent-jar、--result-file）、检查 result 文件为 "OK"、检查 output 文件内容与预期一致、检查 Demo 进程仍存在（如 jps 或 ps）。明确写出 **禁止对生产或含敏感密钥的已有服务执行本验收**。
- **Guidance:** **Depends on: Task 6.1 Output; Task 4.2 Output.** 使用已构建的 Agent JAR 与 Attacher JAR；验收仅在 Demo 进程上执行。校验内容时按当前 Agent 导出格式（如 Map 为 key=value 每行）做字符串比对或按行比对。**不修改目标进程**：工具仅读取并导出，验收通过后 Demo 应仍正常运行。

- 编写步骤文档（README 或 docs/acceptance.md）：(1) 启动 Demo 并记录 PID；(2) 执行 java -jar attacher.jar ... 指向 Demo 的 PID 与 Demo 的类名/字段（或方法）；(3) 确认控制台输出「导出成功」且 result 文件首行为 "OK <path>"；(4) 确认 output 文件内容与 Demo 中预设的 Map/List 内容一致（格式与 Agent 实现一致）；(5) 确认 Demo 进程仍在（jps 或 ps 可见同一 PID）。
- 可选：提供 shell 脚本自动化 (2)(3)(4)(5)，脚本内不启动生产服务、仅操作 Demo。

### Task 6.3 – Document verification, safety, and JDK heap dump reference - Agent_Docs
- **Objective:** 在文档中说明工具为只读、不修改目标进程；说明验收仅限 Demo；并简要说明 JDK 全量堆 dump 命令供参考（与本工具区别）。
- **Output:** README 或 docs 中新增「验收与安全」或「验证说明」小节：本工具仅读取指定对象并导出到文件，不修改目标 JVM 内存；验收必须在独立 Demo 上进行，禁止对已有生产/含敏感数据服务做验收；JDK 可用 jmap/jcmd 做全堆 dump（用途不同），并注明与本工具的区别。
- **Guidance:** **Depends on: Task 6.2 Output.** 强调：**已有服务不能出问题**——验收只用 Demo；若要对真实服务使用工具，需在非生产环境且经授权后按需导出指定字段，并确保输出路径与权限受控。

- 在 README 或 docs 中增加「验收与安全」：工具行为为只读；验收流程与 Demo 使用方式；禁止对生产/含密钥服务做自动化验收。
- 简要列出 JDK 全量堆 dump：`jmap -dump:format=b,file=heap.hprof <pid>`、`jcmd <pid> GC.heap_dump <file>`；说明与本工具（按类+字段/方法导出单对象）的区别及适用场景。

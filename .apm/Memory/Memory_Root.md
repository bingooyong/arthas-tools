# arthas-tools – APM Memory Root
**Memory Strategy:** Dynamic-MD
**Project Overview:** 独立 Java 工具，通过 Attach API 附加到指定 Java 进程，在不重启、不修改目标代码的前提下，按「类+字段/方法」定位对象并将 Map/List/Set/Collection/数组等内容导出到可配置路径；参考 Arthas 思路、无第三方 Attach 依赖；首版 P0 功能 + 可扩展设计，代码遵循阿里巴巴规约与 Google Java Style。

---

## Phase 01 – Project Structure & Build Summary
- **Outcome:** Maven 多模块骨架已就绪：根 POM（packaging pom）、agent 与 attacher 子模块及正确 parent 引用；根 POM 中已统一 JDK 1.8、UTF-8 编码、maven-compiler-plugin 与空 dependencyManagement；根目录 `mvn -q compile` 通过。
- **Agents:** Agent_Build
- **Task logs:** [Task 1.1 – Create Maven root and multi-module structure](.apm/Memory/Phase_01_Project_Structure_Build/Task_1_1_Create_Maven_root_and_multi_module_structure.md) · [Task 1.2 – Configure parent POM (JDK, encoding, compiler)](.apm/Memory/Phase_01_Project_Structure_Build/Task_1_2_Configure_parent_POM_JDK_encoding_compiler.md)

---

## Phase 02 – Agent Module Summary
- **Outcome:** Agent 模块已实现：入口 `CacheExportAgent.agentmain`，参数解析 `AgentArgsParser`（格式 class=...&field=...&outputPath=...&resultPath=...），反射定位 `TargetLocator`，导出策略接口及 Map/List/Set/Collection/array 实现与 `ExportStrategyRegistry`，文件写入与结果反馈 `ResultWriter`（OK/FAIL 写 resultPath），`AgentBootstrap` 串联全流程并统一异常处理（任一处失败写 "FAIL <message>" 到 result 文件）。
- **Agents:** Agent_Agent
- **Task logs:** [Task 2.1 – Agent entry and argument parsing](.apm/Memory/Phase_02_Agent_Module/Task_2_1_Agent_entry_and_argument_parsing.md) · [Task 2.2 – Locate target object by reflection](.apm/Memory/Phase_02_Agent_Module/Task_2_2_Locate_target_object_by_reflection.md) · [Task 2.3 – Export strategy interface and Map/List](.apm/Memory/Phase_02_Agent_Module/Task_2_3_Export_strategy_interface_and_Map_List_implementations.md) · [Task 2.4 – Set/Collection/array strategies](.apm/Memory/Phase_02_Agent_Module/Task_2_4_Export_strategies_for_Set_Collection_and_array.md) · [Task 2.5 – File writer and result feedback](.apm/Memory/Phase_02_Agent_Module/Task_2_5_File_writer_and_result_feedback.md) · [Task 2.6 – Agent integration and error handling](.apm/Memory/Phase_02_Agent_Module/Task_2_6_Agent_integration_and_error_handling.md)

---

## Phase 03 – Attacher Module Summary
- **Outcome:** Attacher 模块已实现：CLI 解析（AttacherArgs、CliParser，--pid/--class/--field|--method/--output/--agent-jar/--result-file），attach 与 loadAgent（AttachRunner、AgentArgsBuilder，agentArgs 与 Agent Task 2.1 契约一致），结果文件读取与中文反馈（ResultFileReader，5s 超时轮询，"导出成功，路径：xxx" / "导出失败：原因"）；JDK 8 通过 profile 引入 tools.jar，文档见 docs/attacher-attach-api.md。
- **Agents:** Agent_Attacher
- **Task logs:** [Task 3.1 – CLI argument parsing](.apm/Memory/Phase_03_Attacher_Module/Task_3_1_CLI_argument_parsing.md) · [Task 3.2 – Attach to target JVM and load Agent](.apm/Memory/Phase_03_Attacher_Module/Task_3_2_Attach_to_target_JVM_and_load_Agent.md) · [Task 3.3 – Read result file and print feedback](.apm/Memory/Phase_03_Attacher_Module/Task_3_3_Read_result_file_and_print_feedback.md)

---

## Phase 04 – Packaging & Run Summary
- **Outcome:** Agent JAR 已配置 manifest Agent-Class（com.github.bingooyong.arthas.agent.CacheExportAgent），无第三方依赖，经 mvn package 与 jar tf/manifest 校验；Attacher 通过 maven-assembly-plugin 产出可执行 JAR（Main-Class: com.github.bingooyong.arthas.attacher.Main，含依赖含 tools.jar），README.md 已说明布局与完整示例命令（--agent-jar、--result-file）。
- **Agents:** Agent_Packaging
- **Task logs:** [Task 4.1 – Package Agent JAR with Agent-Class](.apm/Memory/Phase_04_Packaging_Run/Task_4_1_Package_Agent_JAR_with_Agent_Class.md) · [Task 4.2 – Package Attacher runnable JAR and run instructions](.apm/Memory/Phase_04_Packaging_Run/Task_4_2_Package_Attacher_runnable_JAR_and_run_instructions.md)

---

## Phase 05 – Documentation Summary
- **Outcome:** README.md 已包含项目简介与背景、支持目标类型（Map/List/Set/Collection/数组）、构建（mvn package）与运行说明、与 Arthas 关系说明；完整 CLI 参数表、--field/--method 二选一约束、3 条示例命令、「安全与权限建议」小节（输出路径限制、敏感数据、权限 600、Attach 同用户/容器风险）。文档集中维护于 README，并引用 docs/attacher-attach-api.md。
- **Agents:** Agent_Docs
- **Task logs:** [Task 5.1 – README (intro, build, run, Arthas relation)](.apm/Memory/Phase_05_Documentation/Task_5_1_README_intro_build_run_Arthas_relation.md) · [Task 5.2 – CLI usage and security/permission recommendations](.apm/Memory/Phase_05_Documentation/Task_5_2_CLI_usage_and_security_permission_recommendations.md)

---

## Phase 06 – Test & Acceptance Summary
- **Outcome:** Demo 模块（DemoApp：静态 Map cache、List items、方法 getKeysList，已知内容、main 保持运行）；验收步骤文档 docs/acceptance.md 与可选脚本 scripts/acceptance.sh（启动 Demo → Attacher 导出 → 校验 result/output/进程存活，禁止对生产/含密钥服务验收）；README 新增「验收与安全」与「执行前是否应先备份」、增强「JDK 全量堆 dump」适用场景，docs/acceptance.md 与 README 交叉引用。工具只读、验收仅限 Demo、已有服务不能出问题。
- **Agents:** Agent_Test, Agent_Docs
- **Task logs:** [Task 6.1 – Demo application](.apm/Memory/Phase_06_Test_Acceptance/Task_6_1_Demo_application_in_process_cache_with_known_content.md) · [Task 6.2 – Acceptance procedure](.apm/Memory/Phase_06_Test_Acceptance/Task_6_2_Acceptance_procedure_run_tool_verify_output_and_process_health.md) · [Task 6.3 – Document verification, safety, JDK heap dump](.apm/Memory/Phase_06_Test_Acceptance/Task_6_3_Document_verification_safety_and_JDK_heap_dump_reference.md)

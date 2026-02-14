---
task_ref: "Task 6.3 - Document verification, safety, and JDK heap dump reference"
agent_assignment: "Agent_Docs"
memory_log_path: ".apm/Memory/Phase_06_Test_Acceptance/Task_6_3_Document_verification_safety_and_JDK_heap_dump_reference.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Document verification, safety, and JDK heap dump reference

## Task Reference
Implementation Plan: **Task 6.3 - Document verification, safety, and JDK heap dump reference** assigned to **Agent_Docs**

## Context from Dependencies
This task depends on **Task 6.2 Output**: 验收步骤与 Demo 使用方式已文档化。在本任务中于 README 或 docs 中增加「验收与安全」/「验证说明」小节，强调工具只读、验收仅限 Demo、已有服务不能出问题；并补充 JDK 全量堆 dump 命令及与本工具的区别。

## Objective
在文档中说明工具为只读、不修改目标进程；说明验收仅限 Demo；并简要说明 JDK 全量堆 dump 命令供参考（与本工具区别）。

## Detailed Instructions
- 在 README 或 docs 中增加「验收与安全」：工具行为为只读（仅读取指定对象并导出到文件，不修改目标 JVM 内存）；验收流程与 Demo 使用方式；禁止对生产/含密钥服务做自动化验收。
- 简要列出 JDK 全量堆 dump：`jmap -dump:format=b,file=heap.hprof <pid>`、`jcmd <pid> GC.heap_dump <file>`；说明与本工具（按类+字段/方法导出单对象）的区别及适用场景。
- 强调：**已有服务不能出问题**——验收只用 Demo；若要对真实服务使用工具，需在非生产环境且经授权后按需导出指定字段，并确保输出路径与权限受控。

## Expected Output
- **Deliverables:** README 或 docs 中新增「验收与安全」或「验证说明」小节：本工具仅读取指定对象并导出到文件，不修改目标 JVM 内存；验收必须在独立 Demo 上进行，禁止对已有生产/含敏感数据服务做验收；JDK 可用 jmap/jcmd 做全堆 dump（用途不同），并注明与本工具的区别。
- **Success criteria:** 上述要点均已在文档中体现；读者能区分本工具与 JDK 全堆 dump 的用途与风险。
- **File locations:** README.md 和/或 docs/（如 docs/acceptance.md 或 docs/verification-safety.md）。

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_06_Test_Acceptance/Task_6_3_Document_verification_safety_and_JDK_heap_dump_reference.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

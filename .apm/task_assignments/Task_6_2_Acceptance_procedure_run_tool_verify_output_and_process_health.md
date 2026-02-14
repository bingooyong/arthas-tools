---
task_ref: "Task 6.2 - Acceptance procedure: run tool, verify output and process health"
agent_assignment: "Agent_Test"
memory_log_path: ".apm/Memory/Phase_06_Test_Acceptance/Task_6_2_Acceptance_procedure_run_tool_verify_output_and_process_health.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Acceptance procedure: run tool, verify output and process health

## Task Reference
Implementation Plan: **Task 6.2 - Acceptance procedure: run tool, verify output and process health** assigned to **Agent_Test**

## Context from Dependencies
- **Task 6.1 Output:** Demo 主类、类名、静态 Map/List 的字段名（或方法名）及已知内容；Demo 启动方式与 PID 获取方式。
- **Task 4.2 Output:** Agent JAR 与 Attacher JAR 路径（如 agent/target/*.jar、attacher/target/*-runnable.jar），运行命令格式（--pid、--class、--field/--method、--output、--agent-jar、--result-file）。验收使用已构建的 Agent JAR 与 Attacher JAR，仅在 Demo 进程上执行。工具仅读取并导出，不修改目标进程；验收通过后 Demo 应仍正常运行。

## Objective
定义并执行验收步骤：启动 Demo → 用 Attacher 导出指定类/字段（或方法）→ 校验导出文件内容与预期一致 → 校验 Demo 进程仍存活且未异常退出。

## Detailed Instructions
- 编写步骤文档（README 或 docs/acceptance.md）：(1) 启动 Demo 并记录 PID；(2) 执行 java -jar attacher.jar ... 指向 Demo 的 PID 与 Demo 的类名/字段（或方法）；(3) 确认控制台输出「导出成功」且 result 文件首行为 "OK <path>"；(4) 确认 output 文件内容与 Demo 中预设的 Map/List 内容一致（格式与 Agent 实现一致，如 Map 为 key=value 每行）；(5) 确认 Demo 进程仍在（jps 或 ps 可见同一 PID）。
- 明确写出 **禁止对生产或含敏感密钥的已有服务执行本验收**。可选：提供 shell 脚本自动化 (2)(3)(4)(5)，脚本内仅操作 Demo、不启动生产服务。

## Expected Output
- **Deliverables:** 验收步骤文档（可选：可执行脚本），包含启动 Demo、获取 PID、调用 Attacher、检查 result 为 "OK"、检查 output 内容与预期一致、检查 Demo 进程仍存在。
- **Success criteria:** 文档可被他人按步骤执行完成验收；若提供脚本，运行后能完成上述校验。
- **File locations:** README 或 docs/acceptance.md；可选脚本如 scripts/acceptance.sh。

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_06_Test_Acceptance/Task_6_2_Acceptance_procedure_run_tool_verify_output_and_process_health.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

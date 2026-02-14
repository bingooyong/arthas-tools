---
task_ref: "Task 6.1 - Demo application (in-process cache with known content)"
agent_assignment: "Agent_Test"
memory_log_path: ".apm/Memory/Phase_06_Test_Acceptance/Task_6_1_Demo_application_in_process_cache_with_known_content.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Demo application (in-process cache with known content)

## Task Reference
Implementation Plan: **Task 6.1 - Demo application (in-process cache with known content)** assigned to **Agent_Test**

## Context from Dependencies
This task depends on **Task 4.2 Output (run layout)**: Attacher 与 Agent JAR 的构建产物与运行方式已确定（README 中有布局与示例命令）。Demo 需与主工程分离，以便后续用 Attacher 附加到 Demo 进程并导出其静态 Map/List；Demo 的类名、字段名（或方法名）将用于验收时的 --class、--field/--method 参数。

## Objective
提供一个可独立运行的 Demo 应用，进程内持有已知内容的静态 Map（及可选 List/Set），进程持续运行以便被 Attacher 附加并导出。

## Detailed Instructions
- 实现 Demo 主类：定义 public static 的 Map（及可选 List/Set）并填入已知数据；或提供 static 方法返回上述结构。内容需可预期以便验收时比对（例如 Map 含 "key1"="value1", "key2"="value2"；List 含 ["a","b","c"]）。
- main 中启动后保持运行（如 `while (true) { Thread.sleep(60_000); }` 或类似），便于获取 PID 并执行 Attacher。
- Demo 与主工程分离（如 `demo/` 模块或 `demo/` 目录下独立可执行 JAR/class），避免与 agent/attacher 代码耦合。
- 文档说明 Demo 用途：仅用于验收工具导出结果与进程存活，不用于生产。**禁止在含真实密钥或生产数据的进程上做验收。**

## Expected Output
- **Deliverables:** 可运行的 Demo 主类（含 main），静态字段或静态方法返回 Map/List 且内容可预期；启动后保持运行；不依赖现有业务服务。
- **Success criteria:** Demo 可独立启动、可通过 jps 等获取 PID；类名与字段/方法名明确，便于 Task 6.2 写验收步骤。
- **File locations:** 如 `demo/` 模块或 `demo/` 目录；主类及简要说明（README 或 demo 内 doc）。

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_06_Test_Acceptance/Task_6_1_Demo_application_in_process_cache_with_known_content.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

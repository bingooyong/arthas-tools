---
task_ref: "Task 3.3 - Read result file and print feedback"
agent_assignment: "Agent_Attacher"
memory_log_path: ".apm/Memory/Phase_03_Attacher_Module/Task_3_3_Read_result_file_and_print_feedback.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Read result file and print feedback

## Task Reference
Implementation Plan: **Task 3.3 - Read result file and print feedback** assigned to **Agent_Attacher**

## Context from Dependencies
Based on your Task 3.2 work: after loadAgent returns, the Agent writes "OK <path>" or "FAIL <message>" to the result file path (passed in agentArgs). This task reads that file and prints the corresponding Chinese message to console. Use the same result file path from CLI (Task 3.1).

## Objective
After loadAgent returns, read the result file and print "导出成功，路径：xxx" or "导出失败：原因" to console.

## Detailed Instructions
- After loadAgent returns, wait for result file with a **timeout** (e.g. 5 seconds poll) to avoid infinite wait if Agent crashes.
- Read first line. If line starts with "OK ", print "导出成功，路径：<path>"; if "FAIL ", print "导出失败：<message>".
- Handle missing or malformed result file: on timeout print e.g. "导出失败：未收到结果（超时）"; do not block indefinitely.
- Optionally delete result file after reading. Per PRD F7: console feedback in Chinese.

## Expected Output
- **Deliverables:** Result file reader and console feedback in Chinese per PRD F7.
- **Success criteria:** Bounded wait for result file; "导出成功，路径：xxx" or "导出失败：原因" printed; timeout handled.
- **File locations:** attacher module; result reader and main flow integration.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_03_Attacher_Module/Task_3_3_Read_result_file_and_print_feedback.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

---
task_ref: "Task 3.1 - CLI argument parsing"
agent_assignment: "Agent_Attacher"
memory_log_path: ".apm/Memory/Phase_03_Attacher_Module/Task_3_1_CLI_argument_parsing.md"
execution_type: "single-step"
dependency_context: false
ad_hoc_delegation: false
---

# APM Task Assignment: CLI argument parsing

## Task Reference
Implementation Plan: **Task 3.1 - CLI argument parsing** assigned to **Agent_Attacher**

## Objective
Parse and validate command-line arguments: PID, class name, field or method name, output path, Agent JAR path, result file path.

## Detailed Instructions
- Define CLI options (e.g. --pid, --class, --field/--method, --output, --agent-jar, --result-file); use library or hand-written parsing.
- Validate required args and formats (e.g. PID numeric, output path writable); print usage and exit on failure.
- Follow Alibaba + Google Java Style. No external platform; code only.

## Expected Output
- **Deliverables:** Parsed and validated CLI parameters; usage message and exit on validation failure.
- **Success criteria:** All options defined; validation rejects invalid input with usage; parsed params available for Task 3.2.
- **File locations:** attacher module (e.g. `attacher/src/main/java/...`); CLI parser and option definitions.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_03_Attacher_Module/Task_3_1_CLI_argument_parsing.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

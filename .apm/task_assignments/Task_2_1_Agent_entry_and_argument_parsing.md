---
task_ref: "Task 2.1 - Agent entry and argument parsing"
agent_assignment: "Agent_Agent"
memory_log_path: ".apm/Memory/Phase_02_Agent_Module/Task_2_1_Agent_entry_and_argument_parsing.md"
execution_type: "single-step"
dependency_context: false
ad_hoc_delegation: false
---

# APM Task Assignment: Agent entry and argument parsing

## Task Reference
Implementation Plan: **Task 2.1 - Agent entry and argument parsing** assigned to **Agent_Agent**

## Objective
Implement agentmain and parse agentArgs into class name, field/method name, output path, and result file path.

## Detailed Instructions
- Implement agentmain and delegate to an internal bootstrap that parses agentArgs. Use `agentmain(String agentArgs, Instrumentation inst)`.
- Define and document agentArgs format (e.g. `class=...&field=...` or `outputPath=...&resultPath=...`); parse into typed parameters; **include resultPath so Attacher can pass --result-file and read feedback**.
- Validate required parameters; if invalid, write "FAIL <reason>" to result path (if provided) and exit.
- Ensure code follows Alibaba Java guidelines and Google Java Style. Contract must align with Attacher (Task 3.1/3.3) so result path is passed via CLI and read after loadAgent.

## Expected Output
- **Deliverables:** Agent entry class with agentmain, argument parsing, and validation; failed validation writes to result file and exits.
- **Success criteria:** agentArgs parsed to class name, field or method name, output path, result file path; validation failure writes "FAIL <reason>" to result file.
- **File locations:** agent module (e.g. `agent/src/main/java/...`); entry class and bootstrap/parser as appropriate.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_02_Agent_Module/Task_2_1_Agent_entry_and_argument_parsing.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

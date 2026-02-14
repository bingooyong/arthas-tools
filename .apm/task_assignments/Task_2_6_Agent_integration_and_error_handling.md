---
task_ref: "Task 2.6 - Agent integration and error handling"
agent_assignment: "Agent_Agent"
memory_log_path: ".apm/Memory/Phase_02_Agent_Module/Task_2_6_Agent_integration_and_error_handling.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Agent integration and error handling

## Task Reference
Implementation Plan: **Task 2.6 - Agent integration and error handling** assigned to **Agent_Agent**

## Context from Dependencies
This task wires together all Phase 02 outputs:
- **Task 2.1:** Parser (agentArgs → class, field/method, output path, result path); validation and "FAIL" write on invalid args.
- **Task 2.2:** Locator (class + field/method → target object; supported types: Map, List, Set, Collection, array).
- **Task 2.3/2.4:** Strategy interface, registry/dispatcher, Map/List/Set/Collection/array exporters.
- **Task 2.5:** File writer (export content → output path; result line "OK <path>" or "FAIL <message>" → result file).

**Integration:** In agent bootstrap, call parser (2.1) → locator (2.2) → select strategy (2.3/2.4) → export → writer (2.5). Wrap full flow in try/catch; on any exception write "FAIL <exception message>" to result path. Ensure result file is written in both success and failure cases.

## Objective
Wire parsing → locate → export → write and result; centralize exception handling so result file is always written on failure.

## Detailed Instructions
- In agent bootstrap, invoke: parser (2.1) → locator (2.2) → select strategy (2.3/2.4) → export → writer (2.5).
- Wrap full flow in try/catch; on exception write "FAIL <exception message>" to result path.
- Ensure result file is written in both success and failure cases so Attacher has consistent feedback.

## Expected Output
- **Deliverables:** Integrated agent flow with end-to-end error handling and result feedback.
- **Success criteria:** Single entry path runs parse → locate → export → write; any failure writes "FAIL <message>" to result file.
- **File locations:** agent module; bootstrap/entry wiring and error handling.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_02_Agent_Module/Task_2_6_Agent_integration_and_error_handling.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

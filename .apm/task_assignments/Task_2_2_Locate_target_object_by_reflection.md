---
task_ref: "Task 2.2 - Locate target object by reflection"
agent_assignment: "Agent_Agent"
memory_log_path: ".apm/Memory/Phase_02_Agent_Module/Task_2_2_Locate_target_object_by_reflection.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Locate target object by reflection

## Task Reference
Implementation Plan: **Task 2.2 - Locate target object by reflection** assigned to **Agent_Agent**

## Context from Dependencies
Based on your Task 2.1 work: use the parsed parameters (class name, field name or method name) from the agent bootstrap/parser. The locator should be invoked with these typed parameters; keep logic in a dedicated class for testability and clarity.

## Objective
Given class name and field name (or method name), obtain the target object (Map/List/Set/Collection or array) via reflection.

## Detailed Instructions
- Load the target class by name; resolve **either** static field **or** static method (per agentArgs: field name vs method name).
- If field: get static field value; if method: invoke and take return value.
- Check type (Map, List, Set, Collection, or array); otherwise fail with a clear message.
- Keep logic in a dedicated class for testability and clarity.

## Expected Output
- **Deliverables:** Reflection-based object locator supporting static field and static method return; clear error when type is unsupported.
- **Success criteria:** Target object obtained for supported types; clear error for unsupported types.
- **File locations:** agent module; dedicated locator class.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_02_Agent_Module/Task_2_2_Locate_target_object_by_reflection.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

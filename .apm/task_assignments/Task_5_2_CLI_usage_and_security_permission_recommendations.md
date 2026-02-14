---
task_ref: "Task 5.2 - CLI usage and security/permission recommendations"
agent_assignment: "Agent_Docs"
memory_log_path: ".apm/Memory/Phase_05_Documentation/Task_5_2_CLI_usage_and_security_permission_recommendations.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: CLI usage and security/permission recommendations

## Task Reference
Implementation Plan: **Task 5.2 - CLI usage and security/permission recommendations** assigned to **Agent_Docs**

## Context from Dependencies
This task depends on **Task 4.2 Output by Agent_Packaging**: README already has layout and example command. Add or expand a CLI usage section (in README or docs/) with every parameter, 2–3 example commands, and a "Security and permissions" section. Align with existing Attacher options (--pid, --class, --field/--method, --output, --agent-jar, --result-file).

## Objective
Document all CLI parameters, example commands, and security/permission recommendations for output path and sensitive data.

## Detailed Instructions
- List all Attacher CLI parameters with short descriptions (--pid, --class, --field/--method, --output, --agent-jar, --result-file, etc.); **document that --field and --method are mutually exclusive (exactly one required)**.
- Provide 2–3 example commands (different target types or paths).
- Add "Security and permissions" section: restrict output directory access; exported content may contain keys/sensitive data; recommend permissions (e.g. 600) and safe storage; **note known risks: Attach may require same user/permissions as target process; container or restricted environments may block Attach** (from Context Synthesis).

## Expected Output
- **Deliverables:** CLI usage doc (in README or docs/) with parameters, examples, and security notes.
- **Success criteria:** Every option described; --field/--method mutual exclusivity stated; 2–3 examples; security/permissions and Attach risks noted.
- **File locations:** README.md and/or docs/ (e.g. docs/cli-usage.md).

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_05_Documentation/Task_5_2_CLI_usage_and_security_permission_recommendations.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

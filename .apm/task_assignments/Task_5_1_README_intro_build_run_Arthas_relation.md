---
task_ref: "Task 5.1 - README (intro, build, run, Arthas relation)"
agent_assignment: "Agent_Docs"
memory_log_path: ".apm/Memory/Phase_05_Documentation/Task_5_1_README_intro_build_run_Arthas_relation.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: README (intro, build, run, Arthas relation)

## Task Reference
Implementation Plan: **Task 5.1 - README (intro, build, run, Arthas relation)** assigned to **Agent_Docs**

## Context from Dependencies
This task depends on **Task 4.2 Output by Agent_Packaging**: README.md already describes layout and example command (--agent-jar, --result-file). Extend or refine the README with project intro, supported target types, build/run summary, and Arthas relation; link to detailed CLI doc if in separate file (Task 5.2).

## Objective
Provide README with project intro, background, build and run instructions, and relation to Arthas.

## Detailed Instructions
- Write project name and one-paragraph intro and background (e.g. export in-process cache without restart).
- **List supported target types** from Context Synthesis: Map, List, Set, Collection, array (extensible design).
- Document build: run `mvn package` at root; mention agent and attacher JARs output locations.
- Document run: place Agent and Attacher JARs; point to example command and/or CLI doc; briefly list main parameters.
- Add short "Relation to Arthas" note: reference only, same Attach/Agent idea, no dependency.
- Write in Chinese or bilingual as per project. No external platform.

## Expected Output
- **Deliverables:** README.md with overview, build steps (mvn package), run layout, and Arthas reference note.
- **Success criteria:** Intro, supported types, build, run, and Arthas note present; optional link to CLI doc.
- **File locations:** README.md (root, extend existing from 4.2 or replace as needed).

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_05_Documentation/Task_5_1_README_intro_build_run_Arthas_relation.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

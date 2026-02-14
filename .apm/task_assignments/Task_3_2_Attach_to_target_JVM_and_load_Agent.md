---
task_ref: "Task 3.2 - Attach to target JVM and load Agent"
agent_assignment: "Agent_Attacher"
memory_log_path: ".apm/Memory/Phase_03_Attacher_Module/Task_3_2_Attach_to_target_JVM_and_load_Agent.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Attach to target JVM and load Agent

## Task Reference
Implementation Plan: **Task 3.2 - Attach to target JVM and load Agent** assigned to **Agent_Attacher**

## Context from Dependencies
Based on your Task 3.1 work: use the parsed CLI parameters (PID, class, field or method, output path, agent JAR path, result file path) to build the agentArgs string and perform attach/load. **AgentArgs format must match the Agent contract (Task 2.1):** key=value with `&` separator, e.g. `class=...&field=...&outputPath=...&resultPath=...` (or `method=...` when method is used). Result path is required so Attacher can read feedback after loadAgent.

## Objective
Use VirtualMachine.attach(pid) and loadAgent(agentJarPath, agentArgs); handle attach/load failures with clear messages.

## Detailed Instructions
- Obtain VirtualMachine for given PID via VirtualMachine.attach(pid).
- Build agentArgs string consistent with Agent's parser (class, field/method, output path, result path).
- Call vm.loadAgent(agentJarPath, agentArgs); catch AttachException and other exceptions; print explicit error messages (e.g. "Attach failed: ..." / "Load agent failed: ...").
- **JDK 8:** Attach API in `tools.jar` (must be on classpath). **JDK 9+:** in `jdk.attach` module, no separate JAR. Document in build/docs.

## Expected Output
- **Deliverables:** Attach and load logic with AttachException and load failure handling; agentArgs format matches Agent (2.1) contract.
- **Success criteria:** attach(pid) and loadAgent(agentJarPath, agentArgs) invoked; exceptions caught with clear console messages; Attach API requirement documented.
- **File locations:** attacher module; attach/load class and build/docs note.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_03_Attacher_Module/Task_3_2_Attach_to_target_JVM_and_load_Agent.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

---
task_ref: "Task 4.1 - Package Agent JAR with Agent-Class"
agent_assignment: "Agent_Packaging"
memory_log_path: ".apm/Memory/Phase_04_Packaging_Run/Task_4_1_Package_Agent_JAR_with_Agent_Class.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Package Agent JAR with Agent-Class

## Task Reference
Implementation Plan: **Task 4.1 - Package Agent JAR with Agent-Class** assigned to **Agent_Packaging**

## Context from Dependencies
This task depends on **Task 2.6 Output by Agent_Agent** (integrated agent flow).

**Integration Steps (complete before main work):**
1. Read `agent/src/main/java/com/github/bingooyong/arthas/agent/CacheExportAgent.java` to confirm the class that implements `agentmain(String, Instrumentation)` and its fully qualified class name.
2. Read `agent/pom.xml` to confirm the agent module has no third-party dependencies (JDK only) so the JAR loads cleanly in any target JVM.
3. Confirm agent entry is invoked from Attacher via `loadAgent(agentJarPath, agentArgs)`; the JAR manifest must declare `Agent-Class` so the Attach API can load the entry.

**Producer Output Summary:**
- **Entry point:** `CacheExportAgent` with `agentmain(String agentArgs, Instrumentation inst)`; bootstrap runs in same JVM as target.
- **Key artifact:** `agent/` module builds the JAR to be loaded; all agent code (parser, locator, export strategies, ResultWriter, AgentBootstrap) must be in the JAR.
- **Constraint:** Agent JAR must have minimal or no external dependencies (JDK only) to avoid classpath conflicts in target process.

**Integration Requirements:**
- Set manifest `Agent-Class` to the fully qualified name of the class that implements `agentmain` (e.g. `com.github.bingooyong.arthas.agent.CacheExportAgent`).
- Do not add third-party dependencies to the agent module; verify with `jar tf` or a test JVM load.

**User Clarification Protocol:** If the agent entry class name or package differs from the above after reading the files, use the actual FQCN for the manifest.

## Objective
Produce a standalone Agent JAR with manifest Agent-Class for loading by Attacher.

## Detailed Instructions
- Configure agent module build to add Manifest entry Agent-Class with the fully qualified class name of the agent entry.
- Ensure the JAR contains the agent entry and all agent code; **do not add third-party dependencies** so the Agent loads cleanly in any target JVM (JDK only).
- Verify with `jar tf` or by loading in a test JVM that the manifest is correct. Run `mvn package` (or equivalent) in the agent module or from root to produce the JAR.

## Expected Output
- **Deliverables:** agent module JAR with MANIFEST.MF Agent-Class pointing to the agentmain class; minimal or no external dependencies.
- **Success criteria:** `mvn package` produces agent JAR; manifest contains Agent-Class: <FQCN>; JAR contains all agent classes; no extra libs required for load.
- **File locations:** `agent/pom.xml` (maven-jar-plugin or equivalent); agent JAR output (e.g. `agent/target/*.jar`).

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_04_Packaging_Run/Task_4_1_Package_Agent_JAR_with_Agent_Class.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

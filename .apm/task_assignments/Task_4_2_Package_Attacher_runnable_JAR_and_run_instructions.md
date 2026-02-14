---
task_ref: "Task 4.2 - Package Attacher runnable JAR and run instructions"
agent_assignment: "Agent_Packaging"
memory_log_path: ".apm/Memory/Phase_04_Packaging_Run/Task_4_2_Package_Attacher_runnable_JAR_and_run_instructions.md"
execution_type: "single-step"
dependency_context: true
ad_hoc_delegation: false
---

# APM Task Assignment: Package Attacher runnable JAR and run instructions

## Task Reference
Implementation Plan: **Task 4.2 - Package Attacher runnable JAR and run instructions** assigned to **Agent_Packaging**

## Context from Dependencies
This task depends on **Task 3.3 Output by Agent_Attacher** (result file reader and Main flow) and **Task 4.1 Output** (Agent JAR).

**From Task 4.1:** Agent JAR is produced in the agent module (e.g. `agent/target/arthas-tools-agent-<version>.jar`). Attacher must receive the Agent JAR path via CLI (e.g. `--agent-jar`). Document that both JARs are needed and give example layout.

**From Task 3.3 (Agent_Attacher):** Attacher entry is `Main` (attacher module): parses CLI via CliParser, runs AttachRunner.attachAndLoad, then ResultFileReader.waitAndPrintFeedback(resultFilePath). Main class for the runnable JAR must be the attacher's main class (e.g. `com.github.bingooyong.arthas.attacher.Main`). Attacher has dependencies (e.g. JDK 8 tools.jar via profile); package as executable JAR with dependencies (maven-assembly-plugin or maven-shade-plugin).

**Integration Requirements:**
- Build Attacher as executable JAR with dependencies (main class in manifest or shaded).
- Document in README or docs: place Agent JAR and Attacher JAR (e.g. same dir); example command with PID, class, field, output path, --agent-jar; include --result-file if required by contract.

## Objective
Produce runnable Attacher JAR and document how to run with Agent JAR location and example commands.

## Detailed Instructions
- Configure Attacher module to build an executable JAR with dependencies (e.g. main class, classpath in manifest or shaded). Use maven-assembly-plugin or maven-shade-plugin.
- Document in README or docs: place Agent JAR and Attacher JAR (e.g. same dir); example: `java -jar attacher.jar --pid 1234 --class com.example.Cache --field map --output /tmp/out.txt --agent-jar ./agent.jar`.
- Include note on result file path (e.g. --result-file /tmp/result.txt) if required by contract.

## Expected Output
- **Deliverables:** Attacher JAR (with dependencies), README or doc snippet describing layout (Agent JAR + Attacher JAR) and example CLI commands.
- **Success criteria:** `mvn package` (or equivalent) produces runnable attacher JAR; README or docs describe layout and at least one full example command.
- **File locations:** `attacher/pom.xml` (assembly/shade plugin, main class); README or `docs/` snippet.

## Memory Logging
Upon completion, you **MUST** log work in: `.apm/Memory/Phase_04_Packaging_Run/Task_4_2_Package_Attacher_runnable_JAR_and_run_instructions.md`
Follow .apm/guides/Memory_Log_Guide.md instructions.

## Reporting Protocol
After logging, you **MUST** output a **Final Task Report** code block.
- **Format:** Use the exact template provided in your .claude/commands/apm-3-initiate-implementation.md instructions.
- **Perspective:** Write it from the User's perspective so they can copy-paste it back to the Manager.

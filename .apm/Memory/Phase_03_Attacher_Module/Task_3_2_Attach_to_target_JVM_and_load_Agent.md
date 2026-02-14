---
agent: Agent_Attacher
task_ref: Task 3.2 - Attach to target JVM and load Agent
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 3.2 - Attach to target JVM and load Agent

## Summary
Implemented attach and load logic using VirtualMachine.attach(pid) and loadAgent(agentJarPath, agentArgs); agentArgs format matches Agent (Task 2.1) contract; AttachException and load failures produce clear console messages; Attach API requirement documented.

## Details
- Used parsed CLI params from Task 3.1. Added `AgentArgsBuilder.build(AttacherArgs)` to build agentArgs string: class=...&field=... (or method=...)&outputPath=...&resultPath=..., matching AgentArgsParser contract.
- Added `AttachRunner.attachAndLoad(AttacherArgs)`: VirtualMachine.attach(String.valueOf(pid)), then vm.loadAgent(agentJarPath, agentArgs); catch AttachNotSupportedException and generic Exception for load, print "Attach failed: ..." / "Load agent failed: ..." to stderr and rethrow as IOException; finally vm.detach() with best-effort.
- JDK 8: added Maven profile `jdk8-attach` (activation jdk 1.8) with system-scope dependency on tools.jar (systemPath ${java.home}/../lib/tools.jar). JDK 9+: no extra dependency. Documented in `docs/attacher-attach-api.md`.

## Output
- **Files:** `attacher/.../AgentArgsBuilder.java`, `AttachRunner.java`; `attacher/pom.xml` (profile, maven-jar-plugin mainClass); `docs/attacher-attach-api.md`
- Attach and load logic invoked from Main after parse; agentArgs format aligned with Agent.

## Issues
None

## Next Steps
Task 3.3 (read result file and print feedback).

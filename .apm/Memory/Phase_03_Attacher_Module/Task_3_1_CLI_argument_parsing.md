---
agent: Agent_Attacher
task_ref: Task 3.1 - CLI argument parsing
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 3.1 - CLI argument parsing

## Summary
Implemented CLI argument parsing and validation for the Attacher: options --pid, --class, --field/--method, --output, --agent-jar, --result-file; validation rejects invalid input and prints usage then exits.

## Details
- Added `AttacherArgs` (immutable holder for pid, className, field/method, outputPath, agentJarPath, resultFilePath) and `CliParser.parseOrExit(String[] argv)`.
- Defined options: --pid (numeric), --class (required), exactly one of --field or --method, --output, --agent-jar (file must exist and be readable), --result-file. Validation ensures PID numeric, agent JAR readable, and when parent dirs exist for output/result paths they are writable.
- On any validation failure, CliParser prints usage to stderr and calls System.exit(1). No external library; hand-written parsing.

## Output
- **Files:** `attacher/src/main/java/com/github/bingooyong/arthas/attacher/AttacherArgs.java`, `CliParser.java`
- Parsed params (AttacherArgs) are used by Task 3.2 for building agentArgs and attach/load.

## Issues
None

## Next Steps
Task 3.2 (attach to target JVM and load Agent).

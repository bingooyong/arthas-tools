---
agent: Agent_Agent
task_ref: Task 2.1 - Agent entry and argument parsing
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 2.1 - Agent entry and argument parsing

## Summary
Implemented agentmain entry, agentArgs parsing (class, field/method, outputPath, resultPath), validation, and writing "FAIL <reason>" to result path on validation failure.

## Details
- Added `CacheExportAgent` with `agentmain(String agentArgs, Instrumentation inst)` delegating to `AgentBootstrap.run()`.
- Defined agentArgs format as key=value pairs separated by `&`: `class`, `field` or `method`, `outputPath`, `resultPath`; documented in `AgentArgsParser` Javadoc.
- `AgentArgsParser.parseAndValidate()` parses to map, validates required params and exactly-one of field/method; on failure writes "FAIL <reason>" to resultPath (if provided) and returns null.
- `AgentBootstrap` (2.1 scope) only parses and returns when args null; full flow wired in Task 2.6.

## Output
- **Files:** `agent/src/main/java/com/github/bingooyong/arthas/agent/AgentArgs.java`, `AgentArgsParser.java`, `CacheExportAgent.java`, `AgentBootstrap.java`
- Entry: `CacheExportAgent.agentmain`; bootstrap invokes parser; validation failure writes to result path and exits.

## Issues
None

## Next Steps
Task 2.2 (locate target by reflection).

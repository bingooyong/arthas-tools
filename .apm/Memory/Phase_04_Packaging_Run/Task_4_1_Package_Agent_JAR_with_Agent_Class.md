---
agent: Agent_Packaging
task_ref: Task 4.1 - Package Agent JAR with Agent-Class
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 4.1 - Package Agent JAR with Agent-Class

## Summary
为 agent 模块配置了 MANIFEST.MF 的 Agent-Class 条目，指向 `com.github.bingooyong.arthas.agent.CacheExportAgent`；经 `mvn package` 与 `jar tf`/manifest 校验，产出的 JAR 仅含 JDK 依赖，可被 Attacher 通过 loadAgent 正常加载。

## Details
- 完成依赖集成：确认 `CacheExportAgent` 实现 `agentmain(String, Instrumentation)`，FQCN 为 `com.github.bingooyong.arthas.agent.CacheExportAgent`；确认 `agent/pom.xml` 无第三方依赖；确认 Attacher 通过 `vm.loadAgent(agentJarPath, agentArgs)` 加载，需 JAR 声明 Agent-Class。
- 在 `agent/pom.xml` 中增加 `maven-jar-plugin`，在 `<archive><manifestEntries>` 中设置 `Agent-Class: com.github.bingooyong.arthas.agent.CacheExportAgent`。
- 执行 `mvn package -pl agent -am`，用 `unzip -p ... META-INF/MANIFEST.MF` 与 `jar tf` 验证：manifest 含 Agent-Class，JAR 内含全部 agent 类（CacheExportAgent、AgentBootstrap、AgentArgsParser、ResultWriter、TargetLocator、各 ExportStrategy 等），无额外 lib。

## Output
- 修改文件：`agent/pom.xml`（新增 build/plugins/maven-jar-plugin 及 Agent-Class manifestEntries）
- 产出：`agent/target/arthas-tools-agent-1.0.0-SNAPSHOT.jar`，MANIFEST.MF 含 `Agent-Class: com.github.bingooyong.arthas.agent.CacheExportAgent`

## Issues
None

## Next Steps
Task 4.2 使用本 Agent JAR 路径（如 --agent-jar）构建 Attacher 可执行 JAR 并编写运行文档。

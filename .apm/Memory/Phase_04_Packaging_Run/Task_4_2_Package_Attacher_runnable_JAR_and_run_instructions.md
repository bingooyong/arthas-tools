---
agent: Agent_Packaging
task_ref: Task 4.2 - Package Attacher runnable JAR and run instructions
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 4.2 - Package Attacher runnable JAR and run instructions

## Summary
为 Attacher 模块配置了 maven-assembly-plugin，产出带依赖的可执行 JAR（Main-Class: com.github.bingooyong.arthas.attacher.Main）；在项目根目录新增 README.md，说明 Agent JAR 与 Attacher JAR 的布局及完整示例命令（含 --agent-jar、--result-file）。

## Details
- 依赖 Task 4.1 的 Agent JAR 路径（agent/target/arthas-tools-agent-1.0.0-SNAPSHOT.jar）与 Task 3.3 的 Main 入口与结果文件契约。
- 新增 `attacher/src/main/assembly/attacher-runnable.xml`：打包为单 JAR，unpack runtime 与 system（JDK 8 tools.jar）依赖；在 attacher pom.xml 中配置 maven-assembly-plugin 使用该 descriptor，并设置 archive.manifest.mainClass，绑定到 package 阶段。
- 执行 `mvn package -pl attacher -am`，生成 `attacher/target/arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar`，验证 MANIFEST.MF 含 Main-Class。
- 新建 `README.md`：构建说明、推荐目录布局（Agent JAR + Attacher JAR 同目录）、完整示例命令（--pid、--class、--field、--output、--agent-jar、--result-file），并注明 --result-file 为契约要求及 JDK 8 文档链接。

## Output
- 新增：`attacher/src/main/assembly/attacher-runnable.xml`；`README.md`
- 修改：`attacher/pom.xml`（增加 maven-assembly-plugin 与 runnable 构建）
- 产出 JAR：`attacher/target/arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar`（可执行，含依赖）

## Issues
None

## Next Steps
None

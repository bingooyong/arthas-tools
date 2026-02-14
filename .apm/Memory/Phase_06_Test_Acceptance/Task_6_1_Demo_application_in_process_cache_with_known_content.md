---
agent: Agent_Test
task_ref: Task 6.1 - Demo application (in-process cache with known content)
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 6.1 - Demo application (in-process cache with known content)

## Summary
新增独立 demo 模块，提供可运行的 Demo 主类，持有已知内容的静态 Map 与 List（及静态方法 getKeysList），main 保持运行便于 Attacher 附加；Demo 与 agent/attacher 分离，并文档说明仅用于验收、禁止在生产或含密钥进程上使用。

## Details
- 依赖 Task 4.2：Attacher/Agent JAR 路径与运行命令格式已从 README 获知。
- 在根 pom 中新增 `<module>demo</module>`，新建 demo/pom.xml（mainClass: DemoApp），与 agent/attacher 无代码依赖。
- 实现 `com.github.bingooyong.arthas.demo.DemoApp`：静态字段 `cache`（LinkedHashMap，key1=value1, key2=value2, key3=value3）、`items`（ArrayList，a,b,c）；静态无参方法 `getKeysList()` 返回与 items 相同内容；main 中 `while (true) { Thread.sleep(60_000); }` 保持进程存活；使用 ManagementFactory.getRuntimeMXBean().getName() 输出进程信息以兼容 JDK 8。
- 编写 demo/README.md：说明用途（仅验收）、已知内容表（类名、字段/方法、预期导出格式）、运行方式及禁止在生产/含密钥进程上验收。

## Output
- 新增：`demo/pom.xml`，`demo/src/main/java/com/github/bingooyong/arthas/demo/DemoApp.java`，`demo/README.md`
- 修改：根目录 `pom.xml`（加入 demo 模块）
- 构建产物：`demo/target/arthas-tools-demo-1.0.0-SNAPSHOT.jar`（可执行，含 Main-Class）
- 类名：`com.github.bingooyong.arthas.demo.DemoApp`；字段：`cache`（Map）、`items`（List）；方法：`getKeysList()`。导出格式与 Agent 一致：Map 为 key=value 每行，List 为每行一元素。

## Issues
None

## Next Steps
Task 6.2 使用本 Demo 的类名/字段/方法编写验收步骤并可选脚本。

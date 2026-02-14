# Demo 应用（验收用）

本模块为**验收与测试专用**的 Demo 进程，与主工程（agent/attacher）分离，不依赖业务服务。

## 用途

- 进程内持有**已知内容**的静态 `Map` 与 `List`，便于用 Attacher 导出后比对结果。
- 启动后持续运行，便于获取 PID 并使用 `java -jar attacher.jar ...` 附加导出。
- **仅用于验收工具导出结果与进程存活，不用于生产。**

**禁止在含真实密钥或生产数据的进程上做验收。**

## 已知内容（供验收比对）

| 目标 | 类型 | 类名 | 字段/方法 | 预期导出内容 |
|------|------|------|-----------|--------------|
| Map | 静态字段 | `com.github.bingooyong.arthas.demo.DemoApp` | `cache` | 每行 `key=value`：key1=value1, key2=value2, key3=value3 |
| List | 静态字段 | `com.github.bingooyong.arthas.demo.DemoApp` | `items` | 每行一个元素：a, b, c |
| List | 静态方法 | `com.github.bingooyong.arthas.demo.DemoApp` | `getKeysList` | 同上：a, b, c |

## 运行方式

在项目根目录构建后：

```bash
# 构建（含 demo）
mvn package

# 运行 Demo（保持前台运行）
java -jar demo/target/arthas-tools-demo-1.0.0-SNAPSHOT.jar
```

或单独构建并运行：

```bash
mvn package -pl demo
java -jar demo/target/arthas-tools-demo-1.0.0-SNAPSHOT.jar
```

在另一终端用 `jps` 或 `ps` 获取该进程的 PID，再使用 Attacher 指定 `--class com.github.bingooyong.arthas.demo.DemoApp` 及 `--field cache` 或 `--field items`（或 `--method getKeysList`）进行导出验收。

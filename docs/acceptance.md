# 验收步骤：运行工具、校验导出与进程健康

本文档定义如何使用 **Demo 进程** 验收「进程内缓存导出」工具：启动 Demo → 用 Attacher 导出指定类/字段（或方法）→ 校验导出文件内容与预期一致 → 校验 Demo 进程仍存活。

**禁止对生产或含敏感密钥的已有服务执行本验收。** 验收仅限使用项目提供的 Demo 进程。工具为只读、不修改目标 JVM 内存；更多「验收与安全」说明见 [README - 验收与安全](../README.md#验收与安全)。

---

## 前置条件

- 已构建项目：`mvn package`（产出 agent JAR、attacher runnable JAR、demo JAR）。
- 工具与 Demo 所用 JDK 版本兼容（建议同版本）。

推荐将 Agent 与 Attacher 放在同一目录，例如：

```
tools/
├── arthas-tools-agent-1.0.0-SNAPSHOT.jar
└── arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar
```

---

## 验收步骤

### 1. 启动 Demo 并记录 PID

在项目根目录执行：

```bash
java -jar demo/target/arthas-tools-demo-1.0.0-SNAPSHOT.jar
```

在**另一终端**获取该 Java 进程的 PID：

```bash
jps -l | grep DemoApp
# 或
jps | grep arthas-tools-demo
```

记下 PID（例如 `12345`），后续步骤用 `$PID` 表示。

### 2. 执行 Attacher 导出（Map 静态字段）

假设 Agent JAR 与 Attacher JAR 均在当前目录，且 PID 为 `12345`：

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 12345 \
  --class com.github.bingooyong.arthas.demo.DemoApp \
  --field cache \
  --output /tmp/acceptance-cache.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file /tmp/acceptance-result.txt
```

（若 JAR 在其他路径，请替换 `--agent-jar` 与 `java -jar` 中的路径。）

### 3. 确认控制台与 result 文件

- **控制台** 应出现：`导出成功，路径：/tmp/acceptance-cache.txt`（或你使用的 output 路径）。
- **result 文件** `/tmp/acceptance-result.txt` 首行应为：`OK /tmp/acceptance-cache.txt`（或对应 output 路径）。

若出现 `导出失败：...` 或 result 首行为 `FAIL ...`，则验收不通过，需排查 PID、类名、字段名及 JAR 路径。

### 4. 确认 output 文件内容与 Demo 预设一致

Demo 的静态 Map `cache` 预设为：`key1=value1`、`key2=value2`、`key3=value3`。导出格式为每行 `key=value`。

检查导出文件内容：

```bash
cat /tmp/acceptance-cache.txt
```

预期内容（顺序可能因实现略有差异，但三行均存在即可）：

```
key1=value1
key2=value2
key3=value3
```

### 5. 可选：验收 List 字段或静态方法

- **导出 List 静态字段 `items`**（预期每行一个元素：a, b, c）：

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 12345 \
  --class com.github.bingooyong.arthas.demo.DemoApp \
  --field items \
  --output /tmp/acceptance-items.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file /tmp/acceptance-result2.txt
```

检查 `/tmp/acceptance-items.txt` 内容应包含三行：`a`、`b`、`c`。

- **导出静态方法返回值 `getKeysList`**（预期同上）：

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 12345 \
  --class com.github.bingooyong.arthas.demo.DemoApp \
  --method getKeysList \
  --output /tmp/acceptance-method.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file /tmp/acceptance-result3.txt
```

### 6. 确认 Demo 进程仍存在

执行完上述导出后，Demo 进程应仍在运行（未因 Attach/导出而退出）：

```bash
jps -l | grep DemoApp
# 或
ps -p 12345
```

同一 PID 仍存在即表示进程健康，验收通过。

---

## 自动化脚本（可选）

项目提供 `scripts/acceptance.sh`，在**仅操作 Demo、不启动任何生产服务**的前提下，可自动完成：启动 Demo → 获取 PID → 调用 Attacher 导出 cache → 检查 result 为 "OK"、output 内容与预期一致 → 检查 Demo 进程仍存在。使用方式见脚本内注释或运行 `./scripts/acceptance.sh`（需在项目根目录执行，且已 `mvn package`）。

---

## 验收通过标准小结

- 控制台输出「导出成功」且 result 文件首行为 `OK <output路径>`。
- output 文件内容与 Demo 中预设的 Map/List 内容一致（格式与 Agent 实现一致）。
- Demo 进程在导出后仍存在（jps/ps 可见同一 PID）。

再次强调：**禁止对生产或含敏感密钥的已有服务执行本验收。**

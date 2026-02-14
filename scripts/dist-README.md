# dist 目录说明

本目录由 `make dist` 生成，包含构建产物，可直接用于运行与验收。

## 文件

| 文件 | 用途 |
|------|------|
| `arthas-tools-agent-1.0.0-SNAPSHOT.jar` | Agent JAR，由 Attacher 加载到目标进程 |
| `arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar` | Attacher 可执行 JAR（入口） |
| `arthas-tools-demo-1.0.0-SNAPSHOT.jar` | Demo 进程，仅用于验收 |

## 日常使用（导出目标进程内缓存）

在**本目录**下执行（将 `PID`、`类名`、`字段或方法`、`输出路径` 替换为实际值）：

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid <目标进程PID> \
  --class <目标类全限定名> \
  --field <静态字段名> \
  --output <导出文件路径> \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file <结果反馈文件路径>
```

或使用 `--method <静态无参方法名>` 代替 `--field`（二选一）。详见项目根目录 [README.md](../README.md)。

## 验收（仅限 Demo，禁止对生产/含密钥服务执行）

**方式一：在项目根目录执行（推荐）**

```bash
make accept
# 或
./scripts/acceptance.sh
```

脚本会：启动 Demo → 用 Attacher 导出 Demo 的 `cache` 字段 → 校验「导出成功」、result 为 OK、output 内容与预期一致、Demo 进程仍存活。

**方式二：在本目录下手动验收**

1. **终端 1**，在本目录启动 Demo：
   ```bash
   java -jar arthas-tools-demo-1.0.0-SNAPSHOT.jar
   ```
2. **终端 2**，用 jps 获取 Demo 的 PID，再在本目录执行 Attacher（将 `12345` 换为实际 PID）：
   ```bash
   jps -l | grep DemoApp
   java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
     --pid 12345 \
     --class com.github.bingooyong.arthas.demo.DemoApp \
     --field cache \
     --output /tmp/acceptance-cache.txt \
     --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
     --result-file /tmp/acceptance-result.txt
   ```
3. 确认控制台输出「导出成功」、result 首行为 `OK ...`、导出内容含 `key1=value1` 等，且 Demo 进程仍运行。

更详细步骤见 [docs/acceptance.md](../docs/acceptance.md)。

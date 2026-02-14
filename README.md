# arthas-tools

[![CI](https://img.shields.io/github/actions/workflow/status/bingooyong/arthas-tools/ci.yml?branch=main&label=CI)](https://github.com/bingooyong/arthas-tools/actions/workflows/ci.yml)
[![Release](https://img.shields.io/github/v/release/bingooyong/arthas-tools)](https://github.com/bingooyong/arthas-tools/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 8](https://img.shields.io/badge/Java-8-blue.svg)](https://openjdk.org/)

进程内缓存导出工具：通过 JVM Attach API 附加到目标进程，在不重启、不修改目标代码的前提下，按「类 + 字段/方法」定位对象，将 Map、List、Set、Collection 或数组等内容导出到指定文件。适用于排查线上缓存、导出配置或状态等场景，无需目标应用配合即可导出进程内数据。

## 支持的目标类型

当前支持导出的目标类型（可扩展设计）：

- **Map**：键值对形式导出
- **List**：有序列表逐行导出
- **Set**：集合元素逐行导出
- **Collection**：按集合迭代顺序导出
- **数组**：支持对象数组与基本类型数组

目标对象需为**静态字段**或**静态无参方法的返回值**，且类型为上述之一。

## 构建

在项目根目录执行：

```bash
mvn package
```

或使用 Make 将产物与验收脚本一并输出到 `dist/`（`dist/` 已加入 `.gitignore`，不纳入版本库）：

```bash
make dist    # 构建并填充 dist：三个 JAR + scripts/acceptance.sh + dist 说明
make accept  # 先 make dist，再执行验收（./scripts/acceptance.sh）
```

产物位置：

- **Agent JAR**：`agent/target/arthas-tools-agent-1.0.0-SNAPSHOT.jar`（由 Attacher 加载，manifest 中声明 `Agent-Class`）
- **Attacher 可执行 JAR**：`attacher/target/arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar`（含依赖，可直接 `java -jar` 运行）
- **验收脚本**：`scripts/acceptance.sh`（纳入版本库）；`make dist` 会将其复制到 `dist/` 供 `cd dist && ./acceptance.sh` 使用

## 发布 Release（GitHub Actions）

已配置 **GitHub Actions**：推送以 `v` 开头的 tag 时，自动构建并发布到 [GitHub Releases](https://github.com/bingooyong/arthas-tools/releases)。

**发布步骤：**

1. （可选）将 `pom.xml` 及各模块的版本号改为正式版（如 `1.0.0`，去掉 `-SNAPSHOT`），提交。
2. 打 tag 并推送：
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
3. 在仓库 **Actions** 页查看工作流运行情况；完成后在 **Releases** 页即可看到新版本及三个 JAR 附件。

工作流文件：`.github/workflows/release.yml`。无需在仓库中配置额外 Secret（使用内置 `GITHUB_TOKEN` 即可）。

## 运行方式

需同时准备 **Agent JAR** 与 **Attacher JAR**，建议放在同一目录。

### 推荐目录布局

```
tools/
├── arthas-tools-agent-1.0.0-SNAPSHOT.jar
└── arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar
```

### 快速示例

导出 PID 为 1234 的进程中类 `com.example.Cache` 的静态字段 `map` 到 `/tmp/out.txt`，结果反馈写入 `/tmp/result.txt`：

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 1234 \
  --class com.example.Cache \
  --field map \
  --output /tmp/out.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file /tmp/result.txt
```

完整 CLI 参数说明、更多示例及安全与权限建议见下方 [CLI 使用说明](#cli-使用说明) 与 [安全与权限建议](#安全与权限建议)。验收与安全说明见 [验收与安全](#验收与安全)。

## 验收与安全

- **本工具为只读**：仅按「类 + 字段/方法」读取目标进程中的指定对象并导出到文件，**不修改目标 JVM 内存**，不写入目标进程。
- **验收仅限 Demo**：验收与日常测试必须在项目提供的 **Demo 进程**（`demo/` 模块）上进行；**禁止对生产或含敏感密钥的已有服务做自动化验收**。详见 [docs/acceptance.md](docs/acceptance.md)。
- **已有服务不能出问题**：验收只用 Demo；若要对真实业务使用本工具，应在**非生产环境**且**经授权**后，按需导出指定类/字段，并确保**输出路径与权限受控**，避免敏感数据泄露。不得在未验证、未备份或含生产密钥的进程上直接执行。

JDK 全量堆 dump 命令及与本工具的区别见下方 [JDK 全量堆 dump 与本工具区别](#jdk-全量堆-dump-与本工具区别)。

## 与 Arthas 的关系

本工具在思路上参考了 [Arthas](https://github.com/alibaba/arthas) 的 Attach + Agent 模式：通过 Attach API 注入 Agent、在目标 JVM 内执行逻辑。**本仓库与 Arthas 无代码依赖、无运行时依赖**，仅为独立实现，适用于轻量、定向的进程内缓存/数据导出场景。

**Agent 与 Attacher 的职责、架构及进程交互说明**见 [docs/design-agent-attacher.md](docs/design-agent-attacher.md)。

---

## CLI 使用说明

### 参数列表

| 参数 | 说明 |
|------|------|
| `--pid` | 目标 JVM 进程 ID（必填，数字） |
| `--class` | 目标类全限定名（必填） |
| `--field` | 要导出的**静态字段**名 |
| `--method` | 要导出的**静态无参方法**名（调用后取返回值导出） |
| `--output` | 导出内容写入的文件路径（必填） |
| `--agent-jar` | Agent JAR 的路径（必填，需可读） |
| `--result-file` | Agent 写入结果反馈的文件路径（必填）；Attacher 会在此文件出现后读取并打印「导出成功，路径：xxx」或「导出失败：原因」 |

**重要**：`--field` 与 `--method` **二选一且必填其一**，不能同时指定，也不能都不指定。

### 示例命令

**1. 导出 Map 类型静态字段**

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 1234 \
  --class com.example.Cache \
  --field map \
  --output /tmp/cache-export.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file /tmp/result.txt
```

**2. 导出静态方法返回值（如 List）**

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 5678 \
  --class com.example.ConfigHolder \
  --method getKeys \
  --output /var/run/export/keys.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file /var/run/export/result.txt
```

**3. 导出到当前目录（注意权限）**

```bash
java -jar arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar \
  --pid 9012 \
  --class com.myapp.State \
  --field list \
  --output ./export-out.txt \
  --agent-jar ./arthas-tools-agent-1.0.0-SNAPSHOT.jar \
  --result-file ./export-result.txt
```

### 安全与权限建议

- **输出目录与文件权限**：导出文件可能包含缓存键、配置或业务数据，建议将输出目录限制为仅运行用户可访问，导出完成后对文件设置严格权限（例如 `chmod 600`），并避免放在 web 可访问或共享目录。
- **敏感数据**：导出内容可能含密钥、 token 等敏感信息，请按公司策略存储、传输与保留时间，必要时加密或阅后即焚。
- **Attach 权限与环境**：
  - 使用 Attach API 时，通常需要与目标 JVM 进程**相同的操作系统用户**（或具备相应权限），否则可能抛出 `AttachException`。
  - 在容器、受限环境或安全策略较严的主机上，Attach 可能被禁止或受限，若遇无法附加的情况，需在运维与安全策略允许的前提下调整环境（如同用户、放宽容器权限等）。

JDK 8 下 Attach API 的 classpath 说明见 [docs/attacher-attach-api.md](docs/attacher-attach-api.md)。

---

## JDK 全量堆 dump 与本工具区别

**JDK 全量内存 dump：** 若需导出整个堆（用于其它诊断场景），可使用 JDK 自带命令：

- **jmap**：`jmap -dump:format=b,file=heap.hprof <pid>`（可加 `live` 仅 dump 存活对象）
- **jcmd**：`jcmd <pid> GC.heap_dump <file>`（推荐，开销更小）

两者都会生成堆快照文件，目标进程在 dump 后**继续运行**，仅可能短暂停顿。

**与本工具的区别与适用场景：** 本工具**只按「类 + 字段/方法」读取并导出那一个对象**到文本文件，不 dump 整个堆，也不修改目标进程内存，对目标进程的影响比全堆 dump 更小，适用于**按需导出单个 Map/List/Set 等缓存或配置**。JDK 全堆 dump 适用于**离线分析整个堆、内存泄漏或 OOM 诊断**等场景，会生成较大二进制文件并可能造成短暂停顿。两者用途不同，本仓库不实现全堆 dump。

---

## 执行前是否应先备份？

**建议：**

- **对重要、生产或含敏感数据（如密钥）的进程：** 执行前**应先备份**或确认具备恢复手段；首次使用本工具时，**强烈建议仅在 Demo 或非生产环境**验证，**不要直接对生产或含密钥的服务执行**，以免误操作或环境差异影响已有服务。
- **本工具为只读**：仅读取指定静态字段/方法并写入导出文件，不修改目标 JVM 内存。即便如此，Attach 与 Agent 加载会与目标进程交互，因此对已有服务执行前建议：在非生产环境先跑通、备份关键配置与数据、确认输出路径与权限受控后再在生产使用。
- **验收与日常测试**：仅使用项目提供的 **Demo 进程** 做验收与测试，禁止对生产或含敏感密钥的已有服务做自动化验收。

---

## 对目标进程的影响与风险

**本工具设计为只读**：仅读取指定静态字段/方法并写入文件，不修改目标 JVM 内存中的业务数据。

**但仍存在对目标进程产生影响的风险：**

- **Attach 与加载 Agent** 会向目标 JVM 注入并执行代码。若 Agent 内部抛错、或 JVM/实现存在缺陷，**有可能导致目标进程卡住、控制台无响应、甚至 Ctrl+C 无法退出**（需在另一终端用 `kill -9 <PID>` 结束进程）。
- 因此**不能保证“对正常进程零影响”**；在极端情况下**可能影响生产环境**（进程无响应、需强制结束）。

**使用前提与建议：**

- **禁止**在未备份、未在非生产环境验证的情况下，对**生产或含敏感密钥的服务**执行本工具。
- 仅在**可接受“目标进程可能需 kill -9 恢复”**的场景下对生产使用；且需经授权、在非生产先验证、并做好备份与回滚准备。
- 卡住的进程可从**另一终端**执行 `kill -9 <目标 PID>` 结束。

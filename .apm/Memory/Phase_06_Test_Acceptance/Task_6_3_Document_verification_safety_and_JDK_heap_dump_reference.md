---
agent: Agent_Docs
task_ref: Task 6.3 - Document verification, safety, and JDK heap dump reference
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 6.3 - Document verification, safety, and JDK heap dump reference

## Summary
在 README 中新增「验收与安全」小节，明确工具只读、验收仅限 Demo、已有服务不能出问题及对真实服务使用时的前提；在「JDK 全量堆 dump 与本工具区别」中补充适用场景说明；在 docs/acceptance.md 开头增加与 README 验收与安全的交叉引用。

## Details
- 依赖 Task 6.2：验收步骤与 Demo 使用方式已在 docs/acceptance.md 文档化。
- README 在「与 Arthas 的关系」前新增 **## 验收与安全**：本工具为只读（仅读取指定对象并导出到文件，不修改目标 JVM 内存）；验收仅限 Demo、禁止对生产/含密钥服务做自动化验收；已有服务不能出问题，对真实服务使用需非生产、经授权、输出路径与权限受控；并链接至 docs/acceptance.md 与同文「JDK 全量堆 dump 与本工具区别」小节。
- 在「JDK 全量堆 dump 与本工具区别」段中补充**适用场景**：本工具适用于按需导出单个 Map/List/Set 等；JDK 全堆 dump 适用于离线分析整个堆、内存泄漏或 OOM 诊断等，并说明两者用途不同。
- docs/acceptance.md 首段增加一句：工具为只读、不修改目标 JVM 内存，更多说明见 README 验收与安全。

## Output
- 修改：`README.md`（新增「验收与安全」小节、增强 JDK 全量堆 dump 段的区别与适用场景）
- 修改：`docs/acceptance.md`（与 README 验收与安全的交叉引用）
- 未新增独立 docs/verification-safety.md（要点已集中在 README 验收与安全及现有小节中）

## Issues
None

## Next Steps
None

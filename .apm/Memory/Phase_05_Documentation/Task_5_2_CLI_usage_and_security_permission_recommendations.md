---
agent: Agent_Docs
task_ref: Task 5.2 - CLI usage and security/permission recommendations
status: Completed
ad_hoc_delegation: false
compatibility_issues: false
important_findings: false
---

# Task Log: Task 5.2 - CLI usage and security/permission recommendations

## Summary
在 README 中新增完整的 CLI 使用说明（参数表、二选一约束、2–3 条示例命令）以及「安全与权限建议」小节，满足文档化与风险提示要求。

## Details
- 在 README 内增加「CLI 使用说明」章节：以表格列出全部 Attacher 参数（--pid, --class, --field, --method, --output, --agent-jar, --result-file）及简短说明。
- 明确写出 **--field 与 --method 二选一且必填其一**，不得同时指定或都不指定。
- 提供 3 条示例命令：导出 Map 字段、导出静态方法返回值（List）、导出到当前目录，覆盖不同目标类型与路径。
- 新增「安全与权限建议」：限制输出目录与文件权限、说明导出内容可能含敏感数据、建议权限（如 600）与安全存储；并注明 Attach 需与目标进程同用户/权限、容器或受限环境可能阻止 Attach（与 Context Synthesis 一致）。
- 未单独创建 docs/cli-usage.md，所有内容集中在 README 内以保持单点维护；README 运行章节已链接至该 CLI 与安全小节。

## Output
- **文件**：`README.md`（项目根目录）
- **内容**：参数列表表、二选一说明、3 条示例命令、「安全与权限建议」小节；文末保留对 docs/attacher-attach-api.md 的引用。

## Issues
None

## Next Steps
无。

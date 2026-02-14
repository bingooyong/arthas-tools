#!/usr/bin/env bash
# 验收脚本：仅操作 Demo 进程，禁止对生产/含密钥服务执行。
# 用法：在项目根目录执行 ./scripts/acceptance.sh 或 make accept

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DIST_DIR="$(cd "$SCRIPT_DIR/../dist" && pwd)"
cd "$DIST_DIR"

AGENT_JAR="$DIST_DIR/arthas-tools-agent-1.0.0-SNAPSHOT.jar"
ATTACHER_JAR="$DIST_DIR/arthas-tools-attacher-1.0.0-SNAPSHOT-runnable.jar"
DEMO_JAR="$DIST_DIR/arthas-tools-demo-1.0.0-SNAPSHOT.jar"

for f in "$AGENT_JAR" "$ATTACHER_JAR" "$DEMO_JAR"; do
  if [[ ! -f "$f" ]]; then
    echo "缺少 JAR: $f（请先执行 make dist）"
    exit 1
  fi
done

OUT_DIR="/tmp/arthas-tools-acceptance-$$"
mkdir -p "$OUT_DIR"
OUT_FILE="$OUT_DIR/cache.txt"
RESULT_FILE="$OUT_DIR/result.txt"
DEMO_LOG="$OUT_DIR/demo.log"
DEMO_PID=""
ACCEPTANCE_PASSED=""

cleanup() {
  if [[ -n "$DEMO_PID" ]] && kill -0 "$DEMO_PID" 2>/dev/null; then
    kill "$DEMO_PID" 2>/dev/null || true
  fi
  # 验收通过时保留导出文件供查看，失败时删除临时目录
  if [[ "$ACCEPTANCE_PASSED" != "1" ]] && [[ -d "$OUT_DIR" ]]; then
    rm -rf "$OUT_DIR"
  fi
}
trap cleanup EXIT

echo "在后台启动 Demo..."
java -jar "$DEMO_JAR" > "$DEMO_LOG" 2>&1 &
DEMO_PID=$!
sleep 2
if ! kill -0 "$DEMO_PID" 2>/dev/null; then
  echo "Demo 启动失败，请查看 $DEMO_LOG"
  exit 1
fi
echo "Demo PID: $DEMO_PID"

echo "运行 Attacher 导出 cache..."
OUT_MSG=$(java -jar "$ATTACHER_JAR" \
  --pid "$DEMO_PID" \
  --class com.github.bingooyong.arthas.demo.DemoApp \
  --field cache \
  --output "$OUT_FILE" \
  --agent-jar "$AGENT_JAR" \
  --result-file "$RESULT_FILE" 2>&1)

if ! echo "$OUT_MSG" | grep -q "导出成功"; then
  echo "Attacher 未报导出成功。输出: $OUT_MSG"
  exit 1
fi

FIRST_LINE=$(head -n1 "$RESULT_FILE")
if [[ ! "$FIRST_LINE" =~ ^OK\  ]]; then
  echo "result 文件首行应为 'OK <path>'，实际: $FIRST_LINE"
  exit 1
fi

echo "校验导出文件内容..."
CONTENT=$(cat "$OUT_FILE")
for pair in "key1=value1" "key2=value2" "key3=value3"; do
  if ! echo "$CONTENT" | grep -q "^${pair}$"; then
    echo "期望 output 中含 '$pair'。内容: $CONTENT"
    exit 1
  fi
done

if ! kill -0 "$DEMO_PID" 2>/dev/null; then
  echo "导出后 Demo 进程已退出，期望进程仍存活。"
  exit 1
fi

ACCEPTANCE_PASSED=1
echo "验收通过：导出成功、内容一致、Demo 进程仍运行。"
echo ""
echo "导出文件路径: $OUT_FILE"
echo "导出内容:"
echo "---"
cat "$OUT_FILE"
echo "---"
echo ""
echo "临时目录已保留，可查看: $OUT_DIR （无需保留时可手动删除）"

# 构建并填充 dist 目录（dist 已被 .gitignore 排除）
.PHONY: dist accept clean

VERSION ?= 1.0.0-SNAPSHOT
AGENT_JAR   := agent/target/arthas-tools-agent-$(VERSION).jar
ATTACHER_JAR := attacher/target/arthas-tools-attacher-$(VERSION)-runnable.jar
DEMO_JAR    := demo/target/arthas-tools-demo-$(VERSION).jar

dist:
	mvn -q clean package -DskipTests
	@mkdir -p dist
	cp "$(AGENT_JAR)" dist/
	cp "$(ATTACHER_JAR)" dist/
	cp "$(DEMO_JAR)" dist/
	cp scripts/acceptance.sh dist/ && chmod +x dist/acceptance.sh
	cp scripts/dist-README.md dist/README.md
	@echo "dist 已更新: dist/*.jar, dist/acceptance.sh, dist/README.md"

# 验收（需先 make dist；在项目根目录执行）
accept: dist
	@./scripts/acceptance.sh

clean:
	mvn -q clean
	rm -rf dist

# Attach API 说明

Attacher 使用 JVM Attach API（`com.sun.tools.attach.VirtualMachine`）附加到目标进程并加载 Agent。

- **JDK 8**：Attach API 位于 `tools.jar`，编译和运行均需将 `tools.jar` 置于 classpath。  
  例如：`java -cp "arthas-tools-attacher.jar:$JAVA_HOME/lib/tools.jar" com.github.bingooyong.arthas.attacher.Main ...`  
  使用 JDK 8 构建时，attacher 模块通过 Maven profile `jdk8-attach` 自动引入 `tools.jar`（system 作用域）。

- **JDK 9+**：Attach API 在 `jdk.attach` 模块中，无需额外 JAR，直接编译运行即可。

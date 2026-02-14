package com.github.bingooyong.arthas.attacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the agentArgs string for {@link com.sun.tools.attach.VirtualMachine#loadAgent(String, String)}.
 *
 * <p>Format must match the Agent contract (Task 2.1): key=value with {@code &} separator,
 * e.g. {@code class=...&field=...&outputPath=...&resultPath=...} or {@code method=...} when method is used.
 */
public final class AgentArgsBuilder {

    private AgentArgsBuilder() {}

    /**
     * Builds agentArgs from parsed Attacher CLI arguments.
     *
     * @param args parsed attacher arguments
     * @return agentArgs string for loadAgent
     */
    public static String build(AttacherArgs args) {
        List<String> pairs = new ArrayList<>();
        pairs.add("class=" + args.getClassName());
        if (args.getFieldName() != null) {
            pairs.add("field=" + args.getFieldName());
        } else {
            pairs.add("method=" + args.getMethodName());
        }
        pairs.add("outputPath=" + args.getOutputPath());
        pairs.add("resultPath=" + args.getResultFilePath());
        return String.join("&", pairs);
    }
}

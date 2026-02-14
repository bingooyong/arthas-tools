package com.github.bingooyong.arthas.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import com.github.bingooyong.arthas.agent.export.ExportStrategy;
import com.github.bingooyong.arthas.agent.export.ExportStrategyRegistry;

/**
 * Bootstrap: parses agentArgs and orchestrates locate → export → write.
 * Full flow: parser (2.1) → locator (2.2) → select strategy (2.3/2.4) → export → writer (2.5).
 * On any exception writes "FAIL &lt;message&gt;" to result path so Attacher has consistent feedback.
 */
public final class AgentBootstrap {

    private static final ExportStrategyRegistry STRATEGY_REGISTRY = new ExportStrategyRegistry();

    private AgentBootstrap() {}

    /**
     * Entry from agentmain. Entire body wrapped in try/catch so no exception escapes to the
     * target JVM's agent thread (which could leave the process in a bad state).
     */
    public static void run(String agentArgs, Instrumentation inst) {
        String resultPathForFailure = null;
        try {
            AgentArgs args = AgentArgsParser.parseAndValidate(agentArgs);
            if (args == null) {
                return; // Parser already wrote FAIL to result path if available
            }
            resultPathForFailure = args.getResultPath();
            Object target =
                    TargetLocator.locate(
                            args.getClassName(),
                            args.getFieldName(),
                            args.getMethodName());
            ExportStrategy strategy = STRATEGY_REGISTRY.selectStrategy(target);
            StringBuilder content = new StringBuilder();
            strategy.export(target, content);
            ResultWriter.writeSuccess(content, args.getOutputPath(), resultPathForFailure);
        } catch (Throwable t) {
            String message = t.getMessage() != null ? t.getMessage() : t.getClass().getName();
            if (resultPathForFailure != null && !resultPathForFailure.isEmpty()) {
                try {
                    ResultWriter.writeFailure(resultPathForFailure, message);
                } catch (IOException e) {
                    // best effort; cannot report further
                }
            }
            // Do not rethrow: ensure agent thread exits normally and does not affect target process
        }
    }
}

package com.github.bingooyong.arthas.agent;

import java.lang.instrument.Instrumentation;

/**
 * Agent entry for in-process cache export. Loaded by Attacher via VirtualMachine.loadAgent().
 *
 * <p>Implements {@code agentmain(String agentArgs, Instrumentation inst)}. Delegates to
 * bootstrap which parses agentArgs, locates target object, exports content, and writes output
 * and result file. See {@link AgentArgsParser} for agentArgs format.
 */
public final class CacheExportAgent {

    public static void agentmain(String agentArgs, Instrumentation inst) {
        AgentBootstrap.run(agentArgs, inst);
    }
}

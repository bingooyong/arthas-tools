package com.github.bingooyong.arthas.attacher;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;

/**
 * Attaches to the target JVM and loads the Agent JAR with the given agentArgs.
 *
 * <p>Handles AttachNotSupportedException and load failures with clear console messages.
 * JDK 8: Attach API is in tools.jar (must be on classpath). JDK 9+: in jdk.attach module.
 */
public final class AttachRunner {

    private AttachRunner() {}

    /**
     * Attaches to the process and loads the agent. Returns normally on success.
     * On failure, prints error to stderr and throws.
     *
     * @param args parsed CLI arguments (pid, agent JAR path, and params for agentArgs)
     * @throws IOException if loadAgent or attach fails (after printing message)
     */
    public static void attachAndLoad(AttacherArgs args) throws IOException {
        String agentJarPath = args.getAgentJarPath();
        String agentArgs = AgentArgsBuilder.build(args);
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach(String.valueOf(args.getPid()));
        } catch (AttachNotSupportedException e) {
            System.err.println("Attach failed: " + e.getMessage());
            throw new IOException("Attach failed: " + e.getMessage(), e);
        }
        try {
            vm.loadAgent(agentJarPath, agentArgs);
        } catch (Exception e) {
            System.err.println("Load agent failed: " + e.getMessage());
            throw new IOException("Load agent failed: " + e.getMessage(), e);
        } finally {
            try {
                if (vm != null) {
                    vm.detach();
                }
            } catch (IOException e) {
                System.err.println("Detach warning: " + e.getMessage());
            }
        }
    }
}

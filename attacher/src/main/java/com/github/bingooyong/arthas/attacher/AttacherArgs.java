package com.github.bingooyong.arthas.attacher;

import java.util.Objects;

/**
 * Parsed and validated CLI arguments for the Attacher.
 *
 * <p>Exactly one of {@link #getFieldName()} or {@link #getMethodName()} is non-null.
 * All other fields are required.
 */
public final class AttacherArgs {

    private final long pid;
    private final String className;
    private final String fieldName;
    private final String methodName;
    private final String outputPath;
    private final String agentJarPath;
    private final String resultFilePath;

    public AttacherArgs(
            long pid,
            String className,
            String fieldName,
            String methodName,
            String outputPath,
            String agentJarPath,
            String resultFilePath) {
        this.pid = pid;
        this.className = className;
        this.fieldName = fieldName;
        this.methodName = methodName;
        this.outputPath = outputPath;
        this.agentJarPath = agentJarPath;
        this.resultFilePath = resultFilePath;
    }

    public long getPid() {
        return pid;
    }

    public String getClassName() {
        return className;
    }

    /** Non-null if target is a static field; null if using method. */
    public String getFieldName() {
        return fieldName;
    }

    /** Non-null if target is a static method return value; null if using field. */
    public String getMethodName() {
        return methodName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getAgentJarPath() {
        return agentJarPath;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttacherArgs that = (AttacherArgs) o;
        return pid == that.pid
                && Objects.equals(className, that.className)
                && Objects.equals(fieldName, that.fieldName)
                && Objects.equals(methodName, that.methodName)
                && Objects.equals(outputPath, that.outputPath)
                && Objects.equals(agentJarPath, that.agentJarPath)
                && Objects.equals(resultFilePath, that.resultFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, className, fieldName, methodName, outputPath, agentJarPath, resultFilePath);
    }
}

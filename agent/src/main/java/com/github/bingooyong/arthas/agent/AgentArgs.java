package com.github.bingooyong.arthas.agent;

import java.util.Objects;

/**
 * Parsed agent arguments for cache export.
 *
 * <p>Contract: exactly one of {@link #getFieldName()} or {@link #getMethodName()} is non-null.
 * All other fields are required.
 */
public final class AgentArgs {

    private final String className;
    private final String fieldName;
    private final String methodName;
    private final String outputPath;
    private final String resultPath;

    public AgentArgs(
            String className,
            String fieldName,
            String methodName,
            String outputPath,
            String resultPath) {
        this.className = className;
        this.fieldName = fieldName;
        this.methodName = methodName;
        this.outputPath = outputPath;
        this.resultPath = resultPath;
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

    public String getResultPath() {
        return resultPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentArgs that = (AgentArgs) o;
        return Objects.equals(className, that.className)
                && Objects.equals(fieldName, that.fieldName)
                && Objects.equals(methodName, that.methodName)
                && Objects.equals(outputPath, that.outputPath)
                && Objects.equals(resultPath, that.resultPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, fieldName, methodName, outputPath, resultPath);
    }
}

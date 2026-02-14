package com.github.bingooyong.arthas.agent;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses and validates agent argument string.
 *
 * <h2>agentArgs format</h2>
 * <p>Key-value pairs separated by {@code &}. Keys: {@code class}, {@code field} or {@code method},
 * {@code outputPath}, {@code resultPath}. Example:
 *
 * <pre>
 * class=com.example.MyClass&amp;field=MY_MAP&amp;outputPath=/tmp/out.txt&amp;resultPath=/tmp/result.txt
 * </pre>
 *
 * <p>Or with method:
 *
 * <pre>
 * class=com.example.MyClass&amp;method=getCache&amp;outputPath=/tmp/out.txt&amp;resultPath=/tmp/result.txt
 * </pre>
 *
 * <p>Required: {@code class}, exactly one of {@code field} or {@code method}, {@code outputPath},
 * {@code resultPath}. Values are URL-decoded if needed (no {@code +} or {@code %} in typical use).
 */
public final class AgentArgsParser {

    private static final String CLASS = "class";
    private static final String FIELD = "field";
    private static final String METHOD = "method";
    private static final String OUTPUT_PATH = "outputPath";
    private static final String RESULT_PATH = "resultPath";

    private AgentArgsParser() {}

    /**
     * Parses agentArgs string into a map of keys to values. Does not validate.
     */
    public static Map<String, String> parseToMap(String agentArgs) {
        Map<String, String> map = new HashMap<>();
        if (agentArgs == null || agentArgs.isEmpty()) {
            return map;
        }
        for (String pair : agentArgs.split("&")) {
            int eq = pair.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String key = pair.substring(0, eq).trim();
            // Value may contain '=' (e.g. path with =); take the rest as value
            String value = pair.substring(eq + 1).trim();
            if (!key.isEmpty()) {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * Parses and validates agentArgs. Returns non-null AgentArgs if valid.
     * If invalid and resultPath is available (from partial parse), writes "FAIL &lt;reason&gt;" to it.
     *
     * @param agentArgs raw agent argument string
     * @return parsed args or null if validation failed
     */
    public static AgentArgs parseAndValidate(String agentArgs) {
        Map<String, String> map = parseToMap(agentArgs);
        String className = map.get(CLASS);
        String fieldName = map.get(FIELD);
        String methodName = map.get(METHOD);
        String outputPath = map.get(OUTPUT_PATH);
        String resultPath = map.get(RESULT_PATH);

        String failureReason = validate(className, fieldName, methodName, outputPath, resultPath);
        if (failureReason != null) {
            if (resultPath != null && !resultPath.isEmpty()) {
                writeFailToResultPath(resultPath, failureReason);
            }
            return null;
        }

        return new AgentArgs(
                className,
                fieldName != null && !fieldName.isEmpty() ? fieldName : null,
                methodName != null && !methodName.isEmpty() ? methodName : null,
                outputPath,
                resultPath);
    }

    private static String validate(
            String className,
            String fieldName,
            String methodName,
            String outputPath,
            String resultPath) {
        if (className == null || className.trim().isEmpty()) {
            return "missing or empty class";
        }
        boolean hasField = fieldName != null && !fieldName.trim().isEmpty();
        boolean hasMethod = methodName != null && !methodName.trim().isEmpty();
        if (hasField == hasMethod) {
            return "exactly one of field or method must be set";
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            return "missing or empty outputPath";
        }
        if (resultPath == null || resultPath.trim().isEmpty()) {
            return "missing or empty resultPath";
        }
        return null;
    }

    /**
     * Writes a single line "FAIL &lt;message&gt;" to the result path. UTF-8. Overwrites file.
     */
    public static void writeFailToResultPath(String resultPath, String message) {
        String line = "FAIL " + (message != null ? message : "unknown error") + "\n";
        Path path = Paths.get(resultPath).toAbsolutePath().normalize();
        try {
            if (path.getParent() != null && !Files.isDirectory(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException ignored) {
            // best effort
        }
        try (OutputStream out = Files.newOutputStream(path)) {
            out.write(line.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {
            // best effort; caller may log
        }
    }
}

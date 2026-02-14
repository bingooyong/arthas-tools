package com.github.bingooyong.arthas.attacher;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Parses and validates command-line arguments for the Attacher CLI.
 *
 * <p>Options: --pid, --class, --field, --method, --output, --agent-jar, --result-file.
 * Exactly one of --field or --method is required. On validation failure, prints usage to stderr
 * and exits with code 1.
 */
public final class CliParser {

    private static final String OPT_PID = "--pid";
    private static final String OPT_CLASS = "--class";
    private static final String OPT_FIELD = "--field";
    private static final String OPT_METHOD = "--method";
    private static final String OPT_OUTPUT = "--output";
    private static final String OPT_AGENT_JAR = "--agent-jar";
    private static final String OPT_RESULT_FILE = "--result-file";

    private CliParser() {}

    /**
     * Parses argv and returns validated {@link AttacherArgs}, or prints usage and exits.
     *
     * @param argv command-line arguments
     * @return validated arguments (never null)
     */
    public static AttacherArgs parseOrExit(String[] argv) {
        if (argv == null || argv.length == 0) {
            printUsageAndExit("Missing arguments.");
        }
        String pidStr = null;
        String className = null;
        String fieldName = null;
        String methodName = null;
        String outputPath = null;
        String agentJarPath = null;
        String resultFilePath = null;

        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (OPT_PID.equals(arg)) {
                pidStr = nextValue(argv, i, "pid");
                i++;
            } else if (OPT_CLASS.equals(arg)) {
                className = nextValue(argv, i, "class");
                i++;
            } else if (OPT_FIELD.equals(arg)) {
                fieldName = nextValue(argv, i, "field");
                i++;
            } else if (OPT_METHOD.equals(arg)) {
                methodName = nextValue(argv, i, "method");
                i++;
            } else if (OPT_OUTPUT.equals(arg)) {
                outputPath = nextValue(argv, i, "output");
                i++;
            } else if (OPT_AGENT_JAR.equals(arg)) {
                agentJarPath = nextValue(argv, i, "agent-jar");
                i++;
            } else if (OPT_RESULT_FILE.equals(arg)) {
                resultFilePath = nextValue(argv, i, "result-file");
                i++;
            }
        }

        String err = validate(pidStr, className, fieldName, methodName, outputPath, agentJarPath, resultFilePath);
        if (err != null) {
            printUsageAndExit(err);
        }

        long pid = Long.parseLong(pidStr);
        err = validatePaths(outputPath, agentJarPath, resultFilePath);
        if (err != null) {
            printUsageAndExit(err);
        }

        return new AttacherArgs(pid, className, fieldName, methodName, outputPath, agentJarPath, resultFilePath);
    }

    private static String nextValue(String[] argv, int optionIndex, String optionName) {
        int valueIndex = optionIndex + 1;
        if (valueIndex >= argv.length) {
            printUsageAndExit("Missing value for --" + optionName.replace('-', ' ') + ".");
        }
        return argv[valueIndex];
    }

    private static String validate(
            String pidStr,
            String className,
            String fieldName,
            String methodName,
            String outputPath,
            String agentJarPath,
            String resultFilePath) {
        if (pidStr == null || pidStr.trim().isEmpty()) {
            return "Missing or empty --pid.";
        }
        if (!pidStr.matches("\\d+")) {
            return "Invalid --pid: must be numeric.";
        }
        if (className == null || className.trim().isEmpty()) {
            return "Missing or empty --class.";
        }
        boolean hasField = fieldName != null && !fieldName.trim().isEmpty();
        boolean hasMethod = methodName != null && !methodName.trim().isEmpty();
        if (hasField && hasMethod) {
            return "Cannot specify both --field and --method.";
        }
        if (!hasField && !hasMethod) {
            return "Must specify exactly one of --field or --method.";
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            return "Missing or empty --output.";
        }
        if (agentJarPath == null || agentJarPath.trim().isEmpty()) {
            return "Missing or empty --agent-jar.";
        }
        if (resultFilePath == null || resultFilePath.trim().isEmpty()) {
            return "Missing or empty --result-file.";
        }
        return null;
    }

    private static String validatePaths(String outputPath, String agentJarPath, String resultFilePath) {
        File agentJar = new File(agentJarPath);
        if (!agentJar.isFile() || !agentJar.canRead()) {
            return "Agent JAR not found or not readable: " + agentJarPath;
        }
        Path parentOut = Paths.get(outputPath).toAbsolutePath().normalize().getParent();
        if (parentOut != null && Files.exists(parentOut) && !Files.isWritable(parentOut)) {
            return "Output path parent not writable: " + outputPath;
        }
        Path parentResult = Paths.get(resultFilePath).toAbsolutePath().normalize().getParent();
        if (parentResult != null && Files.exists(parentResult) && !Files.isWritable(parentResult)) {
            return "Result file path parent not writable: " + resultFilePath;
        }
        return null;
    }

    private static void printUsageAndExit(String message) {
        System.err.println(message);
        System.err.println();
        System.err.println("Usage: <main> --pid <pid> --class <class> (--field <name> | --method <name>)");
        System.err.println("       --output <path> --agent-jar <path> --result-file <path>");
        System.err.println();
        System.err.println("  --pid         Target JVM process ID (numeric).");
        System.err.println("  --class       Fully qualified class name containing cache.");
        System.err.println("  --field       Static field name (use this or --method, not both).");
        System.err.println("  --method      Static method name returning cache (use this or --field, not both).");
        System.err.println("  --output      Output file path for exported content.");
        System.err.println("  --agent-jar   Path to the Agent JAR to load.");
        System.err.println("  --result-file Path for result feedback (OK/FAIL line).");
        System.exit(1);
    }
}

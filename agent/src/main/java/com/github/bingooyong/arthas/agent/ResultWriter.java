package com.github.bingooyong.arthas.agent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes export content to the output path and writes a single result line to the result path:
 * "OK &lt;outputPath&gt;" on success or "FAIL &lt;message&gt;" on failure. UTF-8. Creates parent
 * directories for the output file as needed.
 */
public final class ResultWriter {

    private ResultWriter() {}

    /**
     * Writes content to the output path (creating parent dirs) and "OK &lt;outputPath&gt;" to the
     * result path.
     *
     * @param content    export content (UTF-8)
     * @param outputPath path for the export file
     * @param resultPath path for the result feedback line
     * @throws IOException if writing fails
     */
    public static void writeSuccess(CharSequence content, String outputPath, String resultPath)
            throws IOException {
        Path out = Paths.get(outputPath).toAbsolutePath().normalize();
        ensureParentDir(out.getParent());
        try (java.io.Writer w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            w.append(content);
        }
        writeResultLine(resultPath, "OK " + outputPath);
    }

    /**
     * Writes "FAIL &lt;message&gt;" to the result path. Does not write to output path.
     */
    public static void writeFailure(String resultPath, String message) throws IOException {
        writeResultLine(resultPath, "FAIL " + (message != null ? message : "unknown error"));
    }

    private static void writeResultLine(String resultPath, String line) throws IOException {
        Path path = Paths.get(resultPath).toAbsolutePath().normalize();
        ensureParentDir(path.getParent());
        String withNewline = line + "\n";
        Files.write(path, withNewline.getBytes(StandardCharsets.UTF_8));
    }

    /** Avoids FileAlreadyExistsException when parent is e.g. /tmp (symlink on macOS). */
    private static void ensureParentDir(Path parent) throws IOException {
        if (parent == null) {
            return;
        }
        if (!Files.isDirectory(parent)) {
            Files.createDirectories(parent);
        }
    }
}

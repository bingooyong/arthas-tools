package com.github.bingooyong.arthas.attacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Waits for the result file (with timeout), reads the first line, and prints Chinese feedback.
 *
 * <p>Expects "OK &lt;path&gt;" or "FAIL &lt;message&gt;". On timeout, prints "导出失败：未收到结果（超时）".
 * Optionally deletes the result file after reading.
 */
public final class ResultFileReader {

    private static final long POLL_INTERVAL_MS = 200;
    private static final long DEFAULT_TIMEOUT_MS = 5000;

    private ResultFileReader() {}

    /**
     * Waits for the result file up to the given timeout, then reads and prints feedback.
     *
     * @param resultFilePath path to the result file (from agentArgs resultPath)
     * @param timeoutMs      maximum wait time in milliseconds
     * @param deleteAfter    if true, delete the result file after reading
     */
    public static void waitAndPrintFeedback(String resultFilePath, long timeoutMs, boolean deleteAfter) {
        Path path = Paths.get(resultFilePath).toAbsolutePath().normalize();
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (Files.isRegularFile(path) && Files.isReadable(path)) {
                readAndPrint(path, deleteAfter);
                return;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("导出失败：等待结果时被中断");
                return;
            }
        }
        System.out.println("导出失败：未收到结果（超时）");
    }

    /**
     * Waits for the result file with default 5 second timeout, then reads and prints feedback.
     * Does not delete the result file.
     */
    public static void waitAndPrintFeedback(String resultFilePath) {
        waitAndPrintFeedback(resultFilePath, DEFAULT_TIMEOUT_MS, false);
    }

    private static void readAndPrint(Path path, boolean deleteAfter) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            if (line != null) {
                line = line.trim();
                if (line.startsWith("OK ")) {
                    String outPath = line.substring(3).trim();
                    System.out.println("导出成功，路径：" + outPath);
                } else if (line.startsWith("FAIL ")) {
                    String reason = line.substring(5).trim();
                    System.out.println("导出失败：" + reason);
                } else {
                    System.out.println("导出失败：结果格式异常");
                }
            } else {
                System.out.println("导出失败：结果文件为空");
            }
        } catch (IOException e) {
            System.out.println("导出失败：读取结果文件失败 - " + e.getMessage());
        } finally {
            if (deleteAfter) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                    // best effort
                }
            }
        }
    }
}

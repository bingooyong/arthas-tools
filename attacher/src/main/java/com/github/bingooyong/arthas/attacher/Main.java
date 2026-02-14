package com.github.bingooyong.arthas.attacher;

import java.io.IOException;

/**
 * Attacher CLI entry: parse args, attach to target JVM, load Agent, then read result file and print feedback.
 */
public final class Main {

    public static void main(String[] args) {
        AttacherArgs attacherArgs = CliParser.parseOrExit(args);
        try {
            AttachRunner.attachAndLoad(attacherArgs);
            ResultFileReader.waitAndPrintFeedback(attacherArgs.getResultFilePath());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}

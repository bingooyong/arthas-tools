package com.github.bingooyong.arthas.agent.export;

import java.io.IOException;
import java.util.List;

/**
 * Exports a List as one element per line, in order. Elements are stringified with
 * Object.toString(); null is written as "null".
 */
public final class ListExportStrategy implements ExportStrategy {

    @Override
    public void export(Object target, Appendable appendable) throws IOException {
        List<?> list = (List<?>) target;
        for (Object item : list) {
            appendable.append(String.valueOf(item)).append('\n');
        }
    }
}

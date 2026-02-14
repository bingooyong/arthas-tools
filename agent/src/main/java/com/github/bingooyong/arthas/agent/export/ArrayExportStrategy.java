package com.github.bingooyong.arthas.agent.export;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Exports an array (Object[] or primitive array) as one element per line, in index order.
 * Format consistent with List: one item per line.
 */
public final class ArrayExportStrategy implements ExportStrategy {

    @Override
    public void export(Object target, Appendable appendable) throws IOException {
        if (!target.getClass().isArray()) {
            throw new IllegalArgumentException("not an array: " + target.getClass().getName());
        }
        int length = Array.getLength(target);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(target, i);
            appendable.append(String.valueOf(item)).append('\n');
        }
    }
}

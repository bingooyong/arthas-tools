package com.github.bingooyong.arthas.agent.export;

import java.io.IOException;
import java.util.Set;

/**
 * Exports a Set as one element per line (iteration order is implementation-dependent).
 * Elements are stringified with Object.toString(); null is written as "null".
 * Format consistent with List: one item per line.
 */
public final class SetExportStrategy implements ExportStrategy {

    @Override
    public void export(Object target, Appendable appendable) throws IOException {
        Set<?> set = (Set<?>) target;
        for (Object item : set) {
            appendable.append(String.valueOf(item)).append('\n');
        }
    }
}

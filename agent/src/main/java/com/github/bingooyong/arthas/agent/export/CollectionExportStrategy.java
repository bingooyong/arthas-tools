package com.github.bingooyong.arthas.agent.export;

import java.io.IOException;
import java.util.Collection;

/**
 * Exports a Collection (that is not List or Set, e.g. Queue) as one element per line.
 * Format consistent with List/Set: one item per line.
 */
public final class CollectionExportStrategy implements ExportStrategy {

    @Override
    public void export(Object target, Appendable appendable) throws IOException {
        Collection<?> coll = (Collection<?>) target;
        for (Object item : coll) {
            appendable.append(String.valueOf(item)).append('\n');
        }
    }
}

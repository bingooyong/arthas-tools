package com.github.bingooyong.arthas.agent.export;

import java.io.IOException;

/**
 * Strategy for exporting a supported target object (Map, List, Set, Collection, array) to
 * character content, written to an {@link Appendable}.
 */
public interface ExportStrategy {

    /**
     * Exports the given object to the appendable. The object is guaranteed to be one of
     * Map, List, Set, Collection, or array.
     *
     * @param target    the object to export (non-null, supported type)
     * @param appendable where to write the export content
     * @throws IOException if writing fails
     */
    void export(Object target, Appendable appendable) throws IOException;
}

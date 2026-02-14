package com.github.bingooyong.arthas.agent.export;

import java.io.IOException;
import java.util.Map;

/**
 * Exports a Map as key=value pairs, one per line. Keys and values are stringified with
 * Object.toString(); null is written as "null".
 */
public final class MapExportStrategy implements ExportStrategy {

    @Override
    public void export(Object target, Appendable appendable) throws IOException {
        @SuppressWarnings("unchecked")
        Map<Object, Object> map = (Map<Object, Object>) target;
        for (Map.Entry<Object, Object> e : map.entrySet()) {
            appendable.append(String.valueOf(e.getKey()))
                    .append('=')
                    .append(String.valueOf(e.getValue()))
                    .append('\n');
        }
    }
}

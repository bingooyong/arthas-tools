package com.github.bingooyong.arthas.agent.export;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Registry and dispatcher for export strategies by target type. Selects strategy via
 * {@link #selectStrategy(Object)} using the object's runtime class (Map, List, Set,
 * Collection, array).
 */
public final class ExportStrategyRegistry {

    private final Map<Class<?>, ExportStrategy> strategies = new HashMap<>();

    public ExportStrategyRegistry() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(Map.class, new MapExportStrategy());
        register(List.class, new ListExportStrategy());
        register(Set.class, new SetExportStrategy());
        register(Collection.class, new CollectionExportStrategy());
        register(Object[].class, new ArrayExportStrategy());
    }

    /**
     * Registers a strategy for the given type. Subtypes of the given class will match
     * by iterating registered types; for exact dispatch we use the object's class and
     * its interfaces/superclass.
     */
    public void register(Class<?> type, ExportStrategy strategy) {
        strategies.put(type, strategy);
    }

    /**
     * Selects the strategy for the given target object. Checks in order: exact class,
     * then interfaces (Map, List, Set, Collection), then superclass, then array.
     *
     * @param target the object to export (Map, List, Set, Collection, or array)
     * @return the strategy for that type
     * @throws IllegalArgumentException if no strategy is registered for the type
     */
    public ExportStrategy selectStrategy(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("target is null");
        }
        Class<?> clazz = target.getClass();

        // Exact class
        ExportStrategy s = strategies.get(clazz);
        if (s != null) {
            return s;
        }

        // Interfaces in fixed order: Map, List, Set, Collection (prefer more specific)
        Class<?>[] preferred = {Map.class, List.class, Set.class, Collection.class};
        for (Class<?> type : preferred) {
            if (type.isAssignableFrom(clazz)) {
                s = strategies.get(type);
                if (s != null) {
                    return s;
                }
            }
        }

        // Array: use strategies.get(Object[].class) etc.; we register array strategy by component type or a marker
        if (clazz.isArray()) {
            s = strategies.get(clazz);
            if (s != null) {
                return s;
            }
            // Fallback: array strategy may be registered for "array" via a key like Object[].class
            // Task 2.4 will register ArrayExportStrategy; we need a key. Use clazz (e.g. String[].class).
            // So in 2.4 we register one strategy that handles all arrays - we can register with a
            // synthetic key. Simpler: register with Object[].class and in ArrayExportStrategy we handle
            // any array. So in selectStrategy for array we look for Object[].class.
            Class<?> arrayType = Object[].class;
            s = strategies.get(arrayType);
            if (s != null) {
                return s;
            }
        }

        // Superclass (e.g. AbstractList -> List)
        Class<?> sup = clazz.getSuperclass();
        if (sup != null) {
            s = strategies.get(sup);
            if (s != null) {
                return s;
            }
        }

        throw new IllegalArgumentException("no export strategy for type: " + clazz.getName());
    }
}

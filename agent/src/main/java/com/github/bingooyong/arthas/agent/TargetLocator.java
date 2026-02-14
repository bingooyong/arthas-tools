package com.github.bingooyong.arthas.agent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Locates the target cache object (Map, List, Set, Collection, or array) by class name and
 * static field name or static method name.
 */
public final class TargetLocator {

    private TargetLocator() {}

    /**
     * Resolves the target object from the given class and either static field or static method.
     *
     * @param className  fully qualified class name
     * @param fieldName  name of static field (use null if using method)
     * @param methodName name of static method to invoke (use null if using field)
     * @return the target object (Map, List, Set, Collection, or array)
     * @throws IllegalArgumentException if class/field/method not found, or type not supported
     */
    public static Object locate(String className, String fieldName, String methodName) {
        Class<?> clazz = loadClass(className);
        Object target;
        if (fieldName != null && !fieldName.isEmpty()) {
            target = getStaticFieldValue(clazz, fieldName);
        } else if (methodName != null && !methodName.isEmpty()) {
            target = invokeStaticMethod(clazz, methodName);
        } else {
            throw new IllegalArgumentException("exactly one of fieldName or methodName must be set");
        }
        validateTargetType(target);
        return target;
    }

    /**
     * Checks whether the object is a supported export type.
     */
    public static boolean isSupportedType(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Map || obj instanceof List || obj instanceof Set
                || obj instanceof Collection) {
            return true;
        }
        return obj.getClass().isArray();
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class not found: " + className, e);
        }
    }

    private static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new IllegalArgumentException(
                        "field " + clazz.getName() + "." + fieldName + " is not static");
            }
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(
                    "field not found: " + clazz.getName() + "." + fieldName, e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "cannot access field: " + clazz.getName() + "." + fieldName, e);
        } catch (Exception e) {
            // JDK 9+ InaccessibleObjectException etc.
            throw new IllegalArgumentException(
                    "field " + clazz.getName() + "." + fieldName + ": " + e.getMessage(), e);
        }
    }

    private static Object invokeStaticMethod(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException(
                        "method " + clazz.getName() + "." + methodName + " is not static");
            }
            method.setAccessible(true);
            return method.invoke(null);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "method not found: " + clazz.getName() + "." + methodName + "()", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "cannot access method: " + clazz.getName() + "." + methodName, e);
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new IllegalArgumentException(
                    "failed to invoke " + clazz.getName() + "." + methodName + "(): "
                            + cause.getMessage(),
                    cause);
        }
    }

    private static void validateTargetType(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("target is null");
        }
        if (target instanceof Map || target instanceof List || target instanceof Set
                || target instanceof Collection) {
            return;
        }
        if (target.getClass().isArray()) {
            return;
        }
        throw new IllegalArgumentException(
                "unsupported target type: " + target.getClass().getName()
                        + "; supported: Map, List, Set, Collection, array");
    }
}

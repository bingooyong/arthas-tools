package com.github.bingooyong.arthas.demo;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo application for acceptance testing of the in-process cache export tool.
 * Holds known static Map and List so that Attacher export output can be verified.
 * <p>
 * <strong>Usage:</strong> Run this process, obtain PID (e.g. via jps), then use
 * Attacher with --class com.github.bingooyong.arthas.demo.DemoApp and --field cache
 * or --field items (or --method getKeysList).
 * <p>
 * <strong>Not for production.</strong> Do not run acceptance against processes
 * that contain real keys or production data.
 */
public final class DemoApp {

    /** Static Map with known content for export verification. Expected export: key=value per line. */
    public static final Map<String, String> cache = new LinkedHashMap<>();

    /** Static List with known content for export verification. Expected export: one element per line. */
    public static final List<String> items = new ArrayList<>();

    static {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        items.add("a");
        items.add("b");
        items.add("c");
    }

    /** Static no-arg method returning a list (alternative target for --method). */
    public static List<String> getKeysList() {
        return Collections.unmodifiableList(items);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("DemoApp started. Process: " + ManagementFactory.getRuntimeMXBean().getName() + " (use jps to get PID)");
        System.out.println("Static fields: cache (Map), items (List); method: getKeysList()");
        System.out.println("Use Attacher with --class com.github.bingooyong.arthas.demo.DemoApp");
        while (true) {
            Thread.sleep(60_000);
        }
    }
}

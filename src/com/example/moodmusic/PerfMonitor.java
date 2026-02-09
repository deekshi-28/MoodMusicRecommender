package com.example.moodmusic;

public class PerfMonitor {

    public static long measureMillis(Runnable r) {
        long s = System.nanoTime();
        r.run();
        long e = System.nanoTime();
        return (e - s) / 1_000_000;
    }

    public static long usedMemoryBytes() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
}

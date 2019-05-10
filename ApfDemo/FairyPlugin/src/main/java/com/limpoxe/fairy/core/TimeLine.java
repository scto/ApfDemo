package com.limpoxe.fairy.core;

import android.util.Log;

public class TimeLine {
    static final String TAG = "TIMELINE";
    static boolean ENABLE = false;
    static boolean started = false;
    static long lasTimestamp = 0L;
    static long lastOffset = 0L;

    /**
     * 打印时长
     */
    public static void set(String desc) {
        if (!ENABLE || desc == null || desc.length() == 0) {
            return;
        }
        if (!started) {
            // 清零
            lasTimestamp = System.currentTimeMillis();
            started = true;
        }
        long current = System.currentTimeMillis();
        lastOffset = current - lasTimestamp;
        Log.d(TAG, desc + getOffsetFromLast());
        lasTimestamp = current;
    }

    private static String getOffsetFromLast() {
        return " [" + lastOffset + "]";
    }
}
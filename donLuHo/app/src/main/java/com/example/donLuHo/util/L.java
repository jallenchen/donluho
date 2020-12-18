package com.example.donLuHo.util;

import android.util.Log;

/**
 * 打印log.
 */

public class L {
    public static boolean sDeBug = true;
    private final static String TAG = "SUIS--->";
    private static long mStartTime = 0;

    public static void d(String s) {
        d(TAG, s);
    }

    public static void d(String tag, String s) {
        if (sDeBug) {
            Log.d(tag, s);
        }
    }

    public static void e(String s) {
        e(TAG, s);
    }

    public static void e(String tag, String s) {
        if (sDeBug) {
            Log.e(tag, s);
        }
    }

    public static void ds(String info) {
        mStartTime = System.currentTimeMillis();
        d(info);
    }

    public static void de(String info) {
        info += "(used:" + (int) ((System.currentTimeMillis() - mStartTime)) + "ms)";
        d(info);
    }

    public static void setDeBug(boolean deBug) {
        sDeBug = deBug;
    }
}

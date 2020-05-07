package com.securenative.agent.utils;

import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Scanner;

public class Utils {
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }

    public static int versionCompare(String str1, String str2) {
        try (Scanner s1 = new Scanner(str1);
             Scanner s2 = new Scanner(str2)) {
            s1.useDelimiter("\\.");
            s2.useDelimiter("\\.");

            while (s1.hasNextInt() && s2.hasNextInt()) {
                int v1 = s1.nextInt();
                int v2 = s2.nextInt();
                if (v1 < v2) {
                    return -1;
                } else if (v1 > v2) {
                    return 1;
                }
            }

            if (s1.hasNextInt() && s1.nextInt() != 0)
                return 1;
            if (s2.hasNextInt() && s2.nextInt() != 0)
                return -1;

            return 0;
        }
    }

    public static String calculateHash(String str) {
        return DigestUtils.sha256Hex(str);
    }

    public static Pair<Long, String> getProcessInfo() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        Pair<Long, String> pair = new Pair<>(0L, "");
        if (processName != null && processName.length() > 0) {
            try {
                String[] processTokens = processName.split("@");
                pair = new Pair<>(Long.parseLong(processTokens[0]), processTokens[1]);
            }
            catch (Exception e) {
                return pair;
            }
        }
        return pair;
    }

    public static Integer parseIntegerOrDefault(String str, Integer defaultValue) {
        try {
            return Integer.valueOf(str);
        }catch (NumberFormatException ex){
            return defaultValue;
        }
    }

    public static Boolean parseBooleanOrDefault(String str, Boolean defaultValue) {
        try {
            return Boolean.valueOf(str);
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static Long parseLongOrDefault(String str, Long defaultValue) {
        try {
            return Long.valueOf(str);
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static boolean timingSafeEqual(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}

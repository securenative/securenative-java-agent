package com.securenative.utils;

import com.securenative.Logger;
import com.securenative.configurations.SecureNativeOptions;
import com.securenative.exceptions.SecureNativeSDKException;
import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String COOKIE_NAME = "_sn";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA512";
    private static final int AES_KEY_SIZE = 32;
    private static final String[] ipHeaders = {"x-forwarded-for", "x-client-ip", "x-real-ip", "x-forwarded", "x-cluster-client-ip", "forwarded-for", "forwarded", "via"};

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    private static String calculateRFC2104HMAC(String data, String key) throws SecureNativeSDKException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecureNativeSDKException("failed calculating hmac");
        }
    }

    private static String calculateSignature(String payload, String apikey) {
        if (isNullOrEmpty(payload)) {
            return null;
        }
        try {
            return calculateRFC2104HMAC(payload, apikey);
        } catch (SecureNativeSDKException e) {
            return null;
        }
    }

    public static boolean isVerifiedSnRequest(String payload, String headerSignature, String apiKey) {
        String signed = calculateSignature(payload, apiKey);
        if (isNullOrEmpty(signed) || isNullOrEmpty(headerSignature)) {
            return false;
        }
        return headerSignature.equals(signed);
    }

    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }

    public static String decrypt(String s, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Logger.getLogger().info("Starting to decrypt " + s);
        if (s == null || s.length() == 0) {
            return s;
        }
        byte[] cipherText = hexToByteArray(s);
        SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, 32).getBytes(StandardCharsets.UTF_8), "AES");
        byte[] ivBytes = Arrays.copyOfRange(cipherText, 0, AES_KEY_SIZE / 2);
        cipherText = Arrays.copyOfRange(cipherText, AES_KEY_SIZE / 2, cipherText.length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, IVspec);
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8).trim();
    }

    private static byte[] hexToByteArray(String s) {
        byte[] retValue = null;
        if (s != null && s.length() != 0) {
            retValue = new byte[s.length() / 2];
            for (int i = 0; i < retValue.length; i++) {
                retValue[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
            }
        }
        return retValue;
    }

    private final static char[] HEX = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    private static String byteArrayToHex(byte[] byteArray) {
        StringBuffer hexBuffer = new StringBuffer(byteArray.length * 2);
        for (byte b : byteArray)
            for (int j = 1; j >= 0; j--)
                hexBuffer.append(HEX[(b >> (j * 4)) & 0xF]);
        return hexBuffer.toString();
    }

    public static String encrypt(String text, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivBytes = new byte[16];
        secureRandom.nextBytes(ivBytes);
        AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
        SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, 32).getBytes(StandardCharsets.UTF_8), "AES");
        byte[] source = text.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IVspec);
        int mod = source.length % 16;
        if (mod != 0) {
            text = String.format(text + "%" + (16 - mod) + "s", " ");
        }
        return byteArrayToHex(cipher.doFinal(addAll(ivBytes, text.getBytes(StandardCharsets.UTF_8)))).trim();
    }

    private static byte[] addAll(final byte[] array1, byte[] array2) {
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
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

    private static boolean isValidPublicIp(String ip) {
        Inet4Address address;
        try {
            address = (Inet4Address) InetAddress.getByName(ip);
        } catch (UnknownHostException exception) {
            return false;
        }
        return !(address.isSiteLocalAddress() ||
                address.isAnyLocalAddress() ||
                address.isLinkLocalAddress() ||
                address.isLoopbackAddress() ||
                address.isMulticastAddress());
    }

    public static String cookieIdFromRequest(ServletRequest request, SecureNativeOptions options) {
        if (request == null) {
            return "";
        }

        String cookieName = COOKIE_NAME;
        if (options.getCookieName() != null && !options.getCookieName().equals("")) {
            cookieName = options.getCookieName();
        }

        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public static String secureHeaderFromRequest(ServletRequest request) {
        if (request == null) {
            return "";
        }

        HttpServletRequest req = (HttpServletRequest) request;
        String secHeader = req.getHeader("x-securenative");
        if (secHeader != null) {
            return secHeader;
        }
        return "";
    }

    public static String clientIpFromRequest(ServletRequest request) {
        if (request == null) {
            return "";
        }

        HttpServletRequest req = (HttpServletRequest) request;
        String bestCandidate = "";

        for (String ipHeader : ipHeaders) {
            if (req.getHeader(ipHeader) != null) {
                String ip = req.getHeader(ipHeader);
                if (isValidPublicIp(ip)) {
                    bestCandidate = ip;
                }
            }
        }

        return bestCandidate;
    }

    public static String remoteIpFromRequest(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return req.getRemoteAddr();
    }

    public static String userAgentFromRequest(ServletRequest request) {
        if (request == null) {
            return "";
        }

        HttpServletRequest req = (HttpServletRequest) request;
        return req.getHeader("user-agent");
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

    public static String generateTimestamp() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
}

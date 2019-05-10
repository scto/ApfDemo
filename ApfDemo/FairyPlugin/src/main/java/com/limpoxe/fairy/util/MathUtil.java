package com.limpoxe.fairy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathUtil {

    public static long getLongFromString(String input) {
        String numbers = getNumbersFromString(input);
        try {
            return Long.parseLong(numbers);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return 0L;
    }

    public static String getNumbersFromString(String input) {
        int[] numbers = parseIntFromString(input);
        StringBuilder sb = new StringBuilder("");
        if (numbers != null) {
            for (int i : numbers) {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public static int[] parseIntFromString(String input) {
        if (input == null || input.trim().length() == 0) {
            return null;
        }
        char c;
        int len = 0;
        int pow = 0;
        int temp = 0;
        int value = 0;
        input = " " + input;
        int length = input.length();
        List<Integer> ret = new ArrayList<Integer>();
        try {
            for (int i = length - 1; i >= 0; i--) {
                c = input.charAt(i);
                if (c >= 48 && c <= 57) {
                    value = c - 48;
                    pow = (int) Math.pow(10, len);
                    temp = temp + value * pow;
                    len++;
                } else {
                    if (len > 0) {
                        ret.add(temp);
                    }
                    len = 0;
                    temp = 0;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        int j = 0;
        int size = ret.size();
        int[] ints = new int[size];
        for (int i = size - 1; i >= 0; i--) {
            ints[j] = ret.get(i);
            j++;
        }
        return ints;
    }

    public static String randomNumber(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbers[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    public static String randomLetter(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = letters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    private static Random randGen = new Random();

    private static char[] numbers = ("0123456789").toCharArray();

    private static char[] letters = ("abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
            + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
}

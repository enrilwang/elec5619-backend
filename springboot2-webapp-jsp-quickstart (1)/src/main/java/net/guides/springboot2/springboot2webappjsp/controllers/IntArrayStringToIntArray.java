package net.guides.springboot2.springboot2webappjsp.controllers;

import java.util.LinkedHashSet;
import java.util.Set;

public class IntArrayStringToIntArray {
    public static Set<Integer> intArrayStringToIntArray(String arr) {
        if (noContent(arr)) {
            return new LinkedHashSet<Integer>();
        }
        Set<Integer> result = new LinkedHashSet<Integer>();
        arr = arr.replaceAll("[^,^\\d]", "");
        if (noContent(arr)) {
            return new LinkedHashSet<Integer>();
        }
        String[] splitted = arr.split(",");
        for (String ele : splitted) {
            try {
                result.add(Integer.parseInt(ele));
            } catch (Exception e) {
                //No need to deal.
            }
        }
        return result;
    }

    private static boolean noContent(String str) {
        return str == null || str.equals("");
    }
}

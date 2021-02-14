package com.geekbrains.springbootproject.utils;

public class CommonUtils {

    public static int getIntegerOrDefault(String value, int defaultValue) {
        int funcResult;
        try {
            funcResult = Integer.parseInt(value);
        } catch(NumberFormatException e) {
            funcResult = defaultValue;
        }
        return funcResult;
    }

    public static Double castDouble(String value) {
        Double funcResult = null;
        if ((value != null) && !value.isEmpty()) {
            try {
                funcResult = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                funcResult = null;
            }
        }
        return funcResult;
    }

}

package ru.home.util;

public final class RenameUtil {
    private RenameUtil() {}

//    public static String rename(String value) {
//        String regex = "([a-z])([A-Z]+)";
//        String replacement = "$1_$2";
//        value = value.replaceAll(regex, replacement).toLowerCase();
//        return value;
//    }

    public static String rename(String value) {
        return value.toLowerCase();
    }

    public static String changeType(String value) {
        switch (value) {
            case "int":
                return "int8";
            case "nvarchar":
                return "varchar()";
            case "datetime":
            case "datetime2":
                return "timestamp";
            default:
                return value;
        }
    }
}

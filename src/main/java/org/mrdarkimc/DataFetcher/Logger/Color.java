package org.mrdarkimc.DataFetcher.Logger;

public class Color {
    public static final String error = System.getProperty("Colored") != null ? "\u001B[31m" : "";
    public static final String success = System.getProperty("Colored") != null ? "\u001B[32m" : "";
    public static final String reset = System.getProperty("Colored") != null ? "\u001B[0m" : "";
}

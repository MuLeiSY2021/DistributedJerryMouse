package pers.jerrymouse.log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LOG {
    public static final SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat logFileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String spilt = " ";
    private static StringBuilder infoBuilder = new StringBuilder();
    private static StringBuilder debugBuilder = new StringBuilder();
    private static StringBuilder errorBuilder = new StringBuilder();
    private static StringBuilder warnBuilder = new StringBuilder();
    private static String logDir = "logs";

    public static void info(String message) {
        String log = getLogout(message, LogLevel.INFO);
        simpleLogOut(log);
    }

    private static void info(StringBuilder builder) {
        String log = builder.toString();
        simpleLogOut(log);
    }

    public static void infoAppend(String message) {
        infoBuilder.append(getLogout(message, LogLevel.INFO)).append("\n");
    }

    public static void infoFlush() {
        info(infoBuilder);
        infoBuilder = new StringBuilder();
    }

    public static void debug(String message) {
        String log = getLogout(message, LogLevel.DEBUG);
        simpleLogOut(log);
    }

    private static void debug(StringBuilder builder) {
        String log = builder.toString();
        simpleLogOut(log);
    }

    public static void debugAppend(String message) {
        debugBuilder.append(getLogout(message, LogLevel.DEBUG) + "\n");
    }

    public static void debugFlush() {
        debug(debugBuilder.toString());
        debugBuilder = new StringBuilder();
    }

    private static void simpleLogOut(String log) {
        System.out.println(log);
        try {
            Utils.writeStringToFile(log + "\n", new File(getLogDir() + "log" + logFileDateFormat.format(Calendar.getInstance().getTime()) + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void warn(String message) {
        String log = getLogout(message, LogLevel.WARN);
        errorLogOut(log);
    }

    private static void warn(StringBuilder builder) {
        String log = builder.toString();
        errorLogOut(log);
    }

    public static void warnAppend(String message) {
        warnBuilder.append(getLogout(message, LogLevel.WARN)).append("\n");
    }

    public static void warnFlush() {
        warn(warnBuilder);
        warnBuilder = new StringBuilder();
    }

    public static void error(String message) {
        String log = getLogout(message, LogLevel.ERROR);
        errorLogOut(log);
    }

    public static void error(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(e.toString()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append('\n');
        }
        error(sb.toString());
    }

    private static void error(StringBuilder builder) {
        String log = builder.toString();
        errorLogOut(log);
    }

    public static void errorAppend(String message) {
        errorBuilder.append(getLogout(message, LogLevel.ERROR)).append("\n");
    }

    public static void errorFlush() {
        error(errorBuilder);
        errorBuilder = new StringBuilder();
    }

    private static void errorLogOut(String log) {
        System.err.println(log);
        try {
            Utils.writeStringToFile(log + "\n", new File(getLogDir() + "log" + logFileDateFormat.format(Calendar.getInstance().getTime()) + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLogout(String message, LogLevel logLevel) {
        return "[" + logLevel + "]" + spilt +
                logDateFormat.format(Calendar.getInstance().getTime()) + spilt +
                message + spilt;
    }

    private static String getLogDir() {
        return logDir.endsWith("/") ? logDir : logDir + "/";
    }

    public static void setLogDir(String logDir) {
        LOG.logDir = logDir;
    }

    public static enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

}

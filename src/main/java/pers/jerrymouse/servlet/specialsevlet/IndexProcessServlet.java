package pers.jerrymouse.servlet.specialsevlet;


import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class IndexProcessServlet extends FileProcessServlet {
    public static final String TAG = "FileProcessServlet";

    private static final Map<String, String> CONTENT_TYPES = new HashMap<>(); // 存储Content-Type的映射

    static {
        // 添加Content-Type的映射
        CONTENT_TYPES.put("html", "text/html");
        CONTENT_TYPES.put("txt", "text/plain");
        CONTENT_TYPES.put("css", "text/css");
        CONTENT_TYPES.put("js", "application/javascript");
        CONTENT_TYPES.put("jpg", "image/jpeg");
        CONTENT_TYPES.put("jpeg", "image/jpeg");
        CONTENT_TYPES.put("png", "image/png");
        CONTENT_TYPES.put("gif", "image/gif");
    }

    /**
     * 读取文件内容到字节数组中
     */
    private static byte[] readFileToBytes(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        try (InputStream is = Files.newInputStream(file.toPath())) {
            is.read(buffer);
        }
        return buffer;
    }

    /**
     * 获取文件的后缀名
     */
    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else {
            return fileName.substring(index + 1);
        }
    }

    @Override
    public void doGet(ServletRequest request, ServletResponse response) throws IOException {
    }

    @Override
    public void doPost(ServletRequest request, ServletResponse response) throws IOException {
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        // 获取文件的路径
        String filePath = getWorkdir(); // 去掉开头的"/"
        filePath = filePath + request.getAbsolutePath() + "/index.html"; // 构造完整路径
        super.fileOutput(filePath, response);
    }

}

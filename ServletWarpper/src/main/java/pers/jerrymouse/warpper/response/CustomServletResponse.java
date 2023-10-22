package pers.jerrymouse.warpper.response;

import pers.jerrymouse.servlet.response.ServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomServletResponse implements ServletResponse {

    private final Map<String, String> headers;
    private final PrintWriter writer;
    private final ByteArrayOutputStream output;
    private int status;
    private String statusText;

    public CustomServletResponse() {
        this.headers = new HashMap<>();
        setStatus(0);
        this.output = new ByteArrayOutputStream();
        this.writer = new PrintWriter(output);
    }

    public CustomServletResponse(InputStream input) throws IOException {
        this.headers = new HashMap<>();

        // 解析响应头信息
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String statusLine = reader.readLine();
        this.status = Integer.parseInt(statusLine.split(" ")[1]);
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int index = line.indexOf(": ");
            if (index > 0) {
                String key = line.substring(0, index);
                String value = line.substring(index + 2);
                this.headers.put(key, value);
            }
        }

        // 读取响应体
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        this.output = output;

        // 初始化响应输出流
        this.writer = new PrintWriter(new OutputStreamWriter(output));
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
        setStatusText();
    }

    @Override
    public void setStatus(int status, String msg) {
        this.status = status;
        this.statusText = msg;
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType);
    }

    @Override
    public void setAcceptRanges(String ranges) {
        headers.put("Accept-Ranges", ranges);
    }

    @Override
    public void setContentLength(int length) {
        headers.put("Content-Length", String.valueOf(length));
    }

    @Override
    public void addCookie(String name, String value) {
        headers.put("Set-Cookie", name + "=" + value);
    }

    @Override
    public void flushBuffer(OutputStream out) throws IOException {
        this.writer.flush();
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(status).append(" ").append(statusText).append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        builder.append("\r\n");
        out.write(builder.toString().getBytes());
        out.write(output.toByteArray());
        out.flush();
        out.close();
    }

    private void setStatusText() {
        switch (status) {
            case 200:
                this.statusText = "OK";
                break;

            case 404:
                this.statusText = "Not Found";
                break;

            default:
                this.statusText = "Unknown";
        }
    }

    @Override
    public boolean isCommitted() {
        return !headers.isEmpty() || output.size() > 0;
    }

    @Override
    public void sendError(int errorCode, String message) {
        status = errorCode;
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PrintWriter getWriter() {
        return this.writer;
    }

    @Override
    public OutputStream getOutputStream() {
        return output;
    }

    @Override
    public String toString() {
        this.writer.flush();
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(status).append(" ").append(statusText).append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        builder.append("\r\n");
        builder.append(new String(output.toByteArray(), StandardCharsets.UTF_8));
        return builder.toString();
    }

    @Override
    public boolean is404() {
        return this.status == 404;
    }
}


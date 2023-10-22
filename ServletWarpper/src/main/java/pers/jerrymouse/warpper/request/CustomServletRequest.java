package pers.jerrymouse.warpper.request;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.session.Cookie;
import pers.jerrymouse.warpper.session.CustomCookie;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomServletRequest implements ServletRequest {

    protected final Map<String, String> headers;
    protected final Map<String, String> parameters;
    protected final Map<String, Cookie> cookies;
    protected String method;
    protected String path;
    protected String protocol;
    protected byte[] body;

    public CustomServletRequest() {
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
        this.cookies = new HashMap<>();

    }

    public CustomServletRequest(InputStream input) throws IOException {
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
        this.cookies = new HashMap<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        String[] requestLine = null;
        try {
            requestLine = line.split(" ");
            this.method = requestLine[0];
            this.path = requestLine[1];
            this.protocol = requestLine[2];

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] header = line.split(": ");
                if (header[0].equals("Cookie")) {
                    String[] cookiesList = header[1].split("; ");
                    for (String cookie : cookiesList) {
                        String[] cookiePart = cookie.split("=");
                        cookies.put(cookiePart[0], new CustomCookie(cookiePart[1].endsWith("; ") ? cookiePart[1].substring(0, cookiePart[1].length() - 2) : cookiePart[1]));
                    }
                } else {
                    headers.put(header[0], header[1]);
                }
            }

            if ("POST".equalsIgnoreCase(method)) {
                StringBuilder body = new StringBuilder();
                while (reader.ready()) {
                    body.append((char) reader.read());
                }
                String[] params = body.toString().split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length > 1) {
                        this.parameters.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()));
                        LOG.debug(parameters.toString());
                    }
                }
            } else if ("GET".equalsIgnoreCase(method)) {
                String[] parts = path.split("\\?");
                this.path = parts[0];
                if (parts.length > 1) {
                    String[] params = parts[1].split("&");
                    for (String param : params) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length > 1) {
                            this.parameters.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()));
                            LOG.debug(parameters.toString());
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            LOG.error(line);
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(String method) {
        this.method = method;
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
    public Enumeration<String> getHeaderNames() {
        return new Enumeration<String>() {
            private final String[] arr = headers.keySet().toArray(new String[0]);
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < arr.length;
            }

            @Override
            public String nextElement() {
                return arr[index++];
            }
        };
    }

    @Override
    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    @Override
    public String getHost() {
        return headers.get("Host");
    }

    @Override
    public String getReferer() {
        return headers.get("Referer");
    }

    @Override
    public String getUserAgent() {
        return headers.get("User-Agent");
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public String getRequestPath() {
        return path;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String contentType = headers.get("Content-Type");
        if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body)));
        } else {
            throw new UnsupportedOperationException("getReader() can only be called for form data.");
        }
    }

    @Override
    public Cookie getCookie(String name) {
        return cookies.get(name);
    }

    @Override
    public int getContentLength() {
        return Integer.parseInt(headers.get("Content-Length"));
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public Charset getCharset() {
        // 读取HTTP响应正文并将字节流转换为字符串
        String contentType = headers.get("Content-Type");
        String charsetName = "UTF-8"; // 默认字符集
        if (contentType != null && contentType.contains("charset=")) {
            charsetName = contentType.substring(contentType.indexOf("charset=") + 8).trim();
        }
        return Charset.forName(charsetName);
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String path = getRawPath();
        sb.append(method).append(" ").append(path).append(" ").append(protocol).append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
            sb.append("Cookie: ").append(entry.getValue().toString()).append("\r\n");
        }
        sb.append("\r\n");
        if (body != null) {
            sb.append(new String(body));
        }
        return sb.toString();
    }

    public String parameterToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1); // 移除最后一个 "&" 符号
        return sb.toString();
    }


    @Override
    public void flushBuffer(OutputStream out) throws IOException {
        StringBuilder sb = new StringBuilder();
        String path = getRawPath();
        sb.append(method).append(" ").append(path).append(" ").append(protocol).append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
            sb.append("Cookie: ").append(entry.getValue().toString()).append("\r\n");
        }
        sb.append("\r\n");
        if (method.equals("POST")) {
            sb.append(parameterToString());
        }
        out.write(sb.toString().getBytes());
        out.flush();
    }

    protected String getRawPath() {
        return this.method.equals("GET") ? this.path + (parameters.isEmpty() ? "" : "?" + parameterToString()) : this.path;
    }


}


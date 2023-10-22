package pers.jerrymouse.servlet.request;

import pers.jerrymouse.servlet.session.Cookie;
import pers.jerrymouse.servlet.session.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;

public interface ServletRequest {

    void setHeader(String name, String value);

    String getHeader(String name);

    Enumeration<String> getHeaderNames();

    String getParameter(String name);

    Enumeration<String> getParameterNames();

    Cookie getCookie(String name);

    boolean hasSession();

    String getHost();

    String getReferer();

    String getUserAgent();

    void setBody(byte[] body);

    String method();

    String getRequestPath();

    String getAbsolutePath();

    BufferedReader getReader() throws IOException;

    int getContentLength();

    Charset getCharset();

    String getProtocol();

    Session getSession() throws IOException;

    Session getSession(boolean create) throws IOException;

    void flushBuffer(OutputStream out) throws IOException;
}

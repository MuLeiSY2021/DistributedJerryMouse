package pers.jerrymouse.servlet.request;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.service.config.WebAppConfig;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.servlet.session.Cookie;
import pers.jerrymouse.servlet.session.Session;
import pers.jerrymouse.servlet.session.SessionDB;
import pers.jerrymouse.utils.Utils;
import pers.jerrymouse.warpper.request.CustomServletRequest;
import pers.jerrymouse.warpper.session.CustomCookie;
import pers.jerrymouse.warpper.session.CustomSession;

import java.io.IOException;
import java.io.InputStream;

public class CustomServletServerRequest extends CustomServletRequest {
    private WebAppConfig config;

    private SessionDB sessionDB;

    private ServletResponse response;

    private String rootPath;

    public CustomServletServerRequest(InputStream input) throws IOException {
        super(input);
    }


    public void setSessionDB(SessionDB sessionDB) {
        this.sessionDB = sessionDB;
    }

    public void setResponse(ServletResponse response) {
        this.response = response;
    }

    public void setConfig(WebAppConfig config) {
        this.config = config;
    }

    @Override
    public Session getSession() throws IOException {
        return getSession(false);
    }

    @Override
    public boolean hasSession() {
        return getRequestedSessionId() != null;
    }

    @Override
    public Session getSession(boolean create) throws IOException {
        String sessionId = getRequestedSessionId();
        CustomSession session = null;

        if (sessionId != null) {
            session = sessionDB.get(sessionId);
        }

        if (session == null && create) {
            sessionId = Utils.generateSessionId();
            session = new CustomSession(sessionId);
            sessionDB.put(sessionId, session, config.getRootURL(), this.path);
            addCookie(new CustomCookie(sessionId));
        }

        if (session != null) {
            session.setLastAccessedTime(System.currentTimeMillis());
        }

        return session;
    }

    public Session setSession(String sessionId) throws IOException {
        CustomSession session = new CustomSession(sessionId);
        sessionDB.put(sessionId, session, config.getRootURL(), this.path);
        addCookie(new CustomCookie(sessionId));

        session.setLastAccessedTime(System.currentTimeMillis());

        return session;
    }

    private String getRequestedSessionId() {
        Cookie cookie = cookies.get(CustomCookie.COOKIE_NAME);
        if (cookie == null) {
            return null;
        } else {
            return cookie.getValue();
        }
    }


    private void addCookie(CustomCookie sessionCookie) {
        this.response.setHeader("Set-Cookie", sessionCookie.toString());
        this.cookies.put(CustomCookie.COOKIE_NAME, sessionCookie);
    }

    public void setCookiesHead() {
        String sessionId = Utils.generateSessionId();
        CustomCookie sessionCookie = new CustomCookie(sessionId);
        this.cookies.put(CustomCookie.COOKIE_NAME, sessionCookie);
        this.setHeader("Set-Cookie", sessionId);

    }

    public WebAppConfig getWebAppConfig() {
        return this.config;
    }

    @Override
    public String getAbsolutePath() {
        if (rootPath.equals("/")) {
            return "/";
        }
        if (this.path.length() < rootPath.length()) {
            LOG.error("Invalid path " + "root=" + rootPath + ";path=" + this.path);
            return null;
        }
        if (path.startsWith(this.rootPath)) {
            return this.path.substring(rootPath.length());
        } else {
            return path;
        }
    }

    public boolean isAskService() {
        int index = path.indexOf("/", 1);
        if (index == -1) {
            return path.equals(rootPath);
        } else {
            return path.substring(0, index).equals(rootPath);
        }
    }

    public void setRootURL(String rootURL) {
        this.rootPath = rootURL;
    }

    public void saveSession() throws IOException {
        sessionDB.save(config.getRootURL(), super.getRequestPath());
    }
}


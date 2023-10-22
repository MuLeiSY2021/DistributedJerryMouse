package pers.jerrymouse.partionjerry.service.container.inner;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.service.config.WebAppConfig;
import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.servlet.session.SessionDB;
import pers.jerrymouse.warpper.response.CustomServletResponse;


public class Context {
    private final String path;

    private final Wrapper wrapper;

    private final WebAppConfig config;

    private final SessionDB sessionDB;

    private final String description;

    private final String displayName;

    private final String rootURL;

    public Context(String path, Servlet servlet, WebAppConfig config) throws Exception {
        this.path = path;
        this.wrapper = new Wrapper(servlet);
        this.config = config;
        if (config != null) {
            this.sessionDB = SessionDB.getSessionDB(config.getRootURL(), this.path);
            this.description = config.getDescription();
            this.displayName = config.getDisplayName();
            this.rootURL = config.getRootURL();
        } else {
            this.sessionDB = new SessionDB();
            this.description = "empty";
            this.displayName = "empty";
            this.rootURL = "empty";
        }
    }


    public String path() {
        return path;
    }

    public ServletResponse invoke(CustomServletServerRequest request) throws Exception {
        ServletResponse response = new CustomServletResponse();

        if (request != null) {
            request.setResponse(response);
            request.setSessionDB(sessionDB);
            request.setConfig(config);
            request.setRootURL(config.getRootURL());
        }

        invoke(request, response);
        return response;
    }

    private void invoke(CustomServletServerRequest request, ServletResponse response) throws Exception {
        String cookie = request.getHeader("Set-Cookie");
        if (cookie != null) {
            request.setSession(cookie);
        }
        try {
            wrapper.invoke(request, response);
            request.saveSession();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public String getPath() {
        return path;
    }

    public WebAppConfig getConfig() {
        return config;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRootURL() {
        return rootURL;
    }
}

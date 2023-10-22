package pers.jerrymouse.partionjerry.service.config;

import pers.jerrymouse.servlet.Servlet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebAppConfig {
    private final List<Servlet> servlets = new LinkedList<>();
    private final Map<String, String> welcomeFiles = new HashMap<>();
    private String servletDir;
    private String displayName;
    private String description;
    private String rootURL;

    public List<Servlet> getServlets() {
        return servlets;
    }

    public String getServletDir() {
        return servletDir;
    }

    public void setServletDir(String servletDir) {
        this.servletDir = servletDir;
    }

    public void addServlet(Servlet servlet) {
        this.servlets.add(servlet);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRootURL() {
        return rootURL;
    }

    public void setRootURL(String rootURL) {
        this.rootURL = rootURL;
    }

    public Map<String, String> getWelcomeFiles() {
        return welcomeFiles;
    }
}
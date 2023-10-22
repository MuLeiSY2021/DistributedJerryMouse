package pers.jerrymouse.servlet;

import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

public abstract class Servlet implements ServletDao {
    private String workdir = null;

    private String mapping;

    private String name;

    private String clazz;

    @Override
    public void service(ServletRequest request, ServletResponse response) throws Exception {
        if (!(request != null &&
                response != null)) {
            throw new NullPointerException("non-HTTP request or response");
        }

        if ("GET".equalsIgnoreCase(request.method())) {
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.method())) {
            doPost(request, response);
        } else {
            response.sendError(400, "Bad Request");
        }
    }

    @Override
    public String getWorkdir() {
        return workdir;
    }

    public void setWorkdir(String workdir) {
        this.workdir = workdir;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}

package pers.jerrymouse.partionjerry.service.container.inner;

import pers.jerrymouse.servlet.ServletDao;
import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

public class Wrapper {
    private final ServletDao function;

    public Wrapper(ServletDao servlet) throws Exception {
        this.function = servlet;
    }

    public void invoke(ServletRequest request, ServletResponse response) throws Exception {
        function.service(request, response);
    }
}

package pers.jerrymouse.partionjerry.service.container.inner;

import pers.jerrymouse.partionjerry.service.config.WebAppConfig;
import pers.jerrymouse.servlet.Servlet;

public class ContextFactory {
    public static final ContextFactory INSTANCE = new ContextFactory();

    private ContextFactory() {
    }

    public Context newContext(String path, Servlet servlet, WebAppConfig config) throws Exception {
        return new Context(path, servlet, config);
    }

}

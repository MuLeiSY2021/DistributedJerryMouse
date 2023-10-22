package pers.jerrymouse.partionjerry.service.container;

import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.config.WebAppConfig;
import pers.jerrymouse.partionjerry.service.container.inner.ContextFactory;
import pers.jerrymouse.partionjerry.service.container.inner.Engine;
import pers.jerrymouse.servlet.Servlet;

public class ContainerFactory {
    public static final ContainerFactory INSTANCE = new ContainerFactory();

    private ContainerFactory() {
    }

    public Container newContainer() throws Exception {
        Engine engine = new Engine();
        for (WebAppConfig webAppConfig : Server.INSTANCE.config.getWebApps()) {
            for (Servlet servlet : webAppConfig.getServlets()) {
                engine.register(ContextFactory.INSTANCE.newContext(servlet.getMapping(), servlet, webAppConfig));
            }
        }
        Server.INSTANCE.config.setEngine(engine);
        return new Container(engine);
    }
}

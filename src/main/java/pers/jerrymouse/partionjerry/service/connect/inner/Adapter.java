package pers.jerrymouse.partionjerry.service.connect.inner;

import pers.jerrymouse.partionjerry.service.container.inner.Engine;
import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

public class Adapter implements AdapterDao {
    private final Engine engine;

    public Adapter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public ServletResponse handler(CustomServletServerRequest request) throws Exception {
        return engine.process(request);
    }

    @Override
    public ServletResponse handler404() throws Exception {
        return engine.process404();
    }
}

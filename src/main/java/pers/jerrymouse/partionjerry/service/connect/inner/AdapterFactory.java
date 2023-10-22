package pers.jerrymouse.partionjerry.service.connect.inner;

import pers.jerrymouse.partionjerry.service.container.inner.Engine;

public class AdapterFactory {
    public static final AdapterFactory INSTANCE = new AdapterFactory();

    private AdapterFactory() {
    }

    public AdapterDao newAdapter(Engine engine) {
        return new Adapter(engine);
    }
}

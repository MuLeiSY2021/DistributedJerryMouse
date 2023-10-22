package pers.jerrymouse.partionjerry.service.connect;

import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.connect.inner.AdapterDao;
import pers.jerrymouse.partionjerry.service.connect.inner.AdapterFactory;
import pers.jerrymouse.partionjerry.service.connect.inner.EndpointFactory;

public class ConnectorFactory {
    public static final ConnectorFactory INSTANCE = new ConnectorFactory();

    private ConnectorFactory() {
    }

    public Connector newConnector() {
        AdapterDao adapterDao = AdapterFactory.INSTANCE.newAdapter(Server.INSTANCE.config.engine());
        return new Connector(Server.INSTANCE.config.getPort(), EndpointFactory.INSTANCE.newEndpoint(adapterDao));
    }

}

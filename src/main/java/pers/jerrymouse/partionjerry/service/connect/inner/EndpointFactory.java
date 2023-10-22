package pers.jerrymouse.partionjerry.service.connect.inner;

public class EndpointFactory {
    public static final EndpointFactory INSTANCE = new EndpointFactory();

    private EndpointFactory() {
    }

    public EndpointDao newEndpoint(AdapterDao adapterDao) {
        return new Endpoint(10, 20, adapterDao);
    }
}

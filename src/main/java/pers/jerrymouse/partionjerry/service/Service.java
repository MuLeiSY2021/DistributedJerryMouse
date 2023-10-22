package pers.jerrymouse.partionjerry.service;

import pers.jerrymouse.partionjerry.service.connect.Connector;
import pers.jerrymouse.partionjerry.service.container.Container;

public class Service {
    private final Connector connector;
    private final Container container;
    private String displayName;
    private String description;
    private String rootUrl;


    public Service(Connector connector, Container container) {
        this.connector = connector;
        this.container = container;
    }

    public void init() throws Exception {
        connector.listen();
    }

    public void shutdown() {
        this.connector.stop();
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

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }
}

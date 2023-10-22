package pers.jerrymouse.partionjerry.service;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.config.Config;
import pers.jerrymouse.partionjerry.service.connect.Connector;
import pers.jerrymouse.partionjerry.service.connect.ConnectorFactory;
import pers.jerrymouse.partionjerry.service.container.Container;
import pers.jerrymouse.partionjerry.service.container.ContainerFactory;
import pers.jerrymouse.utils.Utils;

import java.io.File;

public class ServiceFactory {
    public static final ServiceFactory INSTANCE = new ServiceFactory();

    private ServiceFactory() {
    }

    public Service newService() {
        readConfig(Server.INSTANCE.config);
        Connector connector = null;
        Container container = null;
        try {
            container = ContainerFactory.INSTANCE.newContainer();
            connector = ConnectorFactory.INSTANCE.newConnector();
        } catch (Exception e) {
            exceptionProcess(e);
        }
        return new Service(connector, container);
    }

    public void readConfig(Config config) {
        try {
            File servlets = Utils.getFile(new File(config.coreDir()), "webapps", true);

            if (servlets == null) {
                LOG.warn("no webapps");
                return;
            }

            for (File servlet : servlets.listFiles()) {
                File webInfo = Utils.getFile(servlet, "WEB-INF", true);
                if (webInfo == null) {
                    throw new NullPointerException("WEB-INF is not a directory");
                }
                File xmlConfig = Utils.getFile(webInfo, "web.xml", false);
                SAXReader reader = new SAXReader();
                Document document = reader.read(xmlConfig);
                config.readWebAppConfig(document.getRootElement(), servlet.getPath());
            }
            config.add404WebAppConfig();

        } catch (Exception e) {
            exceptionProcess(e);
        }
    }


    public void exceptionProcess(Exception exception) {
        LOG.error(exception);
    }
}

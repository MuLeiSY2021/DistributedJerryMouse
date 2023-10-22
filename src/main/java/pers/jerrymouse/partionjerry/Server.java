package pers.jerrymouse.partionjerry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.service.Service;
import pers.jerrymouse.partionjerry.service.ServiceFactory;
import pers.jerrymouse.partionjerry.service.config.Config;
import pers.jerrymouse.partionjerry.service.connect.inner.proecssor.ProcessorFactoryDao;
import pers.jerrymouse.partionjerry.service.listener.Listener;
import pers.jerrymouse.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Server {
    public static final Server INSTANCE = new Server();
    private final HashMap<Integer, Service> services = new HashMap<>();
    public File workdir;
    public Config config;

    public void setWorkdir(File workdir) {
        this.workdir = workdir;
    }

    //!!!!!!!!!!!!!!!!!原来的psvm方法已经合并到MinServer里了

    public void init(ProcessorFactoryDao processorFactoryDao, boolean master) {
        try {
            this.config = new Config();
            LOG.info("start read config..");
            readConfig(processorFactoryDao, master);
            LOG.info("read config done");

            if (!master) {
                //从机清空webapp文件夹
                File webapp = new File(workdir.getPath() + "/webapps");
                try {
                    Utils.deleteAllFiles(webapp);
                } catch (IOException ignore) {

                }

                //开启监听webapp
                Listener listener = new Listener();
                LOG.infoAppend("Start slave listener");
                LOG.infoFlush();
                listener.listen();
            }
            LOG.info("start init services");

            Service service = ServiceFactory.INSTANCE.newService();
            services.put(service.hashCode(), service);
            service.init();

            LOG.info("all services setup");

        } catch (Exception e) {
            exceptionProcessor(e);
        }
    }

    public void readConfig(ProcessorFactoryDao processorFactoryDao, boolean master) throws Exception {
        LOG.info("Set working directory..");
        File workdir = this.workdir;

        File xmlConfigFile = Utils.getFile(workdir, "config.xml", false);
        if (xmlConfigFile == null || !xmlConfigFile.exists()) {
            LOG.error("Config file does not exist");
            return;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(xmlConfigFile);
        Element xmlConfig = document.getRootElement();
        LOG.info("Set port..");
        this.config.setPort(Integer.parseInt(xmlConfig.elementText("port")));
        LOG.info("Set out-time..");
        this.config.setOutTime(Integer.parseInt(xmlConfig.elementText("out-time")));
        LOG.info("Set name..");

        this.config.setName(xmlConfig.elementText("name"));
        LOG.info("Set description..");

        this.config.setDescription(xmlConfig.elementText("description"));

        LOG.info("Set path..");
        this.config.setCoreDir(this.workdir.getPath());

        LOG.info("Set processorFactoryDao..");
        this.config.setProcessorFactory(processorFactoryDao);
        if (!master) {
            LOG.info("Set beat-port..");
            this.config.setBeatPort(Integer.parseInt(xmlConfig.elementText("beat-port")));
        }
    }

    public void shutdown() {
        for (Service service : services.values()) {
            service.shutdown();
        }
    }

    public void exceptionProcessor(Exception e) {
        e.printStackTrace();
    }
}

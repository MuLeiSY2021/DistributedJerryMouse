package pers.jerrymouse.mainjerry.main;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.mainjerry.distributed.linktoslave.MappingStrategy;
import pers.jerrymouse.mainjerry.distributed.processor.DisProcessorFactory;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.connect.inner.proecssor.ProcessorFactory;

import java.io.File;

public class MainServer extends Server {
    public static final MainServer INSTANCE = new MainServer();

    public MainServer() {
    }

    public static void main(String[] args) {

        boolean master = false;
        MainServer.INSTANCE.setWorkdir(new File(args[0]));
        Server.INSTANCE.setWorkdir(new File(args[0]));

        LOG.setLogDir(Server.INSTANCE.workdir + "/logs/");

        LOG.infoAppend(" Set working directory" + args[0]);


        try {
            master = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            LOG.error("shell 脚本初始值设置有问题,以默认从机模式启动");
            LOG.error(e);
        }
        try {
            if (master) {
                LOG.info("Master server startup.. ");


                LOG.debug(new File(args[0]).exists() ? "true" : "false");

                Server.INSTANCE.init(new DisProcessorFactory(), true);
                MainServer.INSTANCE.init();

                LOG.infoAppend("Master server started");
                LOG.infoFlush();
            } else {
                LOG.infoAppend("Slave server startup.. ");

                LOG.infoAppend(" Start init..");
                LOG.infoFlush();

                Server.INSTANCE.init(new ProcessorFactory(), false);

                LOG.infoAppend("Slave server started");
                LOG.infoFlush();
            }
        } catch (Exception e) {
            LOG.error(e);
        }

    }

    public void init() {
        try {
            MappingStrategy.INSTANCE.init();
        } catch (Exception e) {
            LOG.error(e);
        }
    }
}

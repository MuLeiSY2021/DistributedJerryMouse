package pers.jerrymouse.partionjerry.service.listener;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {

    public void listen() {
        try {
            LOG.infoAppend("listener is running ,beat port is " + Server.INSTANCE.config.getBeatPort());
            int port = Server.INSTANCE.config.getBeatPort();
            try (ServerSocket socketServer = new ServerSocket(port)) {
                Socket socket = socketServer.accept();
                LOG.infoAppend("Start accepting , dir is " + Server.INSTANCE.workdir + "/" + "webapps");
                LOG.infoFlush();
                OutputStream out = socket.getOutputStream();
                out.flush();
                Utils.transferTo(socket, Server.INSTANCE.workdir + "/" + "webapps");
                LOG.info("Accepting End");
            }

        } catch (IOException e) {
            LOG.error(e);
        }
    }
}

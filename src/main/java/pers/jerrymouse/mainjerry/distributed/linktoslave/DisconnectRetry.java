package pers.jerrymouse.mainjerry.distributed.linktoslave;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.mainjerry.distributed.Slave;
import pers.jerrymouse.mainjerry.main.MainServer;
import pers.jerrymouse.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class DisconnectRetry implements Runnable {
    public Slave slave;

    public DisconnectRetry(Slave slave) {
        this.slave = slave;
    }

    @Override
    public void run() {
        int waitTime = 1000;
        LOG.warn("Slave" + slave.getName() + " is trying reconnection");
        SocketAddress remoteSocketAddress = slave.getBeatSocket().getRemoteSocketAddress();
        while (true) {
            LOG.info("Slave" + slave.getName() + " reconnecting...wait " + waitTime / 1000 + " seconds");
            try {
                slave.getBeatSocket().close();

                Socket socket = new Socket();
                socket.connect(remoteSocketAddress, 1000 * 3);
                socket.setSoTimeout(waitTime / 1000);
                socket.sendUrgentData(0xff);

                if (slave.getBeatSocket().isConnected()) {
                    MappingStrategy.INSTANCE.registerSlave(slave);
                    slave.setAlive();
                    slave.setBeatSocket(socket);
                    Utils.transferFrom(socket, new File(MainServer.INSTANCE.workdir + "/" + "webapps"));
                    return;
                }

            } catch (IOException ignore) {
            } catch (Exception e) {
                LOG.error(e);
            } finally {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    LOG.error(e);
                }
                waitTime = waitTime << 1;
            }

        }
    }
}

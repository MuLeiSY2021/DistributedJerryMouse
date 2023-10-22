package pers.jerrymouse.mainjerry.distributed.linktoslave;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.mainjerry.distributed.Slave;

import java.io.IOException;
import java.net.Socket;

public class CheckLink implements Runnable {
    private volatile boolean flg = true;

    @Override
    public void run() {
        int waitTime = 1000;
        while (flg) {
            try {
                int i = 0;
                for (Slave slave : MappingStrategy.INSTANCE.slaves) {
                    Socket socket = slave.getBeatSocket();
                    try {
                        socket.sendUrgentData(0xFF);
                    } catch (IOException e) {
                        if (i == 10) {
                            LOG.warn(slave.getName() + " is not disconnected");
                            MappingStrategy.INSTANCE.slaves.remove(slave);
                            slave.setDead();
                            Thread disconnectRetryThread = new Thread(new DisconnectRetry(slave));
                            disconnectRetryThread.start();
                            i = 0;
                        } else {
                            i++;
                        }
                    }
                }
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void stop() {
        this.flg = false;
    }
}
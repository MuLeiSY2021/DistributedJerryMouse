package pers.jerrymouse.mainjerry.distributed;

import java.io.IOException;

public class SlaveFactory {
    public Slave newSlave(String ip, int port, String name, int beatPort) throws IOException {
        return new Slave(ip, port, name, beatPort);
    }

}

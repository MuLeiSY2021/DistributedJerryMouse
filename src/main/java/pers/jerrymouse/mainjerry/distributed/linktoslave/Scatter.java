package pers.jerrymouse.mainjerry.distributed.linktoslave;

import pers.jerrymouse.mainjerry.distributed.Slave;
import pers.jerrymouse.mainjerry.main.MainServer;
import pers.jerrymouse.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Queue;

public class Scatter {

    public static void transportation(Queue<Slave> slaves) throws IOException {
        for (Slave slave : slaves) {
            Utils.transferFrom(slave.getBeatSocket(), new File(MainServer.INSTANCE.workdir + "/" + "webapps"));
        }
    }
}

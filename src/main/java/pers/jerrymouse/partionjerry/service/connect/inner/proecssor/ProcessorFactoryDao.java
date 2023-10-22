package pers.jerrymouse.partionjerry.service.connect.inner.proecssor;

import pers.jerrymouse.partionjerry.service.connect.inner.AdapterDao;

import java.net.Socket;

public interface ProcessorFactoryDao {
    Runnable newProcessor(Socket socket, AdapterDao adapter);
}

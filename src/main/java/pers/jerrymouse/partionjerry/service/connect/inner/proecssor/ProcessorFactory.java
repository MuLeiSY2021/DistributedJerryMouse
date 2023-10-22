package pers.jerrymouse.partionjerry.service.connect.inner.proecssor;

import pers.jerrymouse.partionjerry.service.connect.inner.AdapterDao;

import java.net.Socket;

public class ProcessorFactory implements ProcessorFactoryDao {

    public ProcessorFactory() {
    }

    @Override
    public Runnable newProcessor(Socket socket, AdapterDao adapter) {
        return new Processor(socket, adapter);
    }
}

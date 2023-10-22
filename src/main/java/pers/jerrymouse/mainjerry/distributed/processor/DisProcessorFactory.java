package pers.jerrymouse.mainjerry.distributed.processor;

import pers.jerrymouse.partionjerry.service.connect.inner.AdapterDao;
import pers.jerrymouse.partionjerry.service.connect.inner.proecssor.ProcessorFactory;

import java.net.Socket;

public class DisProcessorFactory extends ProcessorFactory {
    @Override
    public Runnable newProcessor(Socket socket, AdapterDao adapter) {
        return new DistributeProcessor(socket, adapter);
    }

}

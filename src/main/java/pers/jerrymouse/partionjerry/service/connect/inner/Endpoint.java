package pers.jerrymouse.partionjerry.service.connect.inner;

import pers.jerrymouse.partionjerry.Server;

import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Endpoint implements EndpointDao {

    private final ThreadPoolExecutor poolExecutor;

    private final AdapterDao adapter;


    public Endpoint(int coreSize, int maxSize, AdapterDao adapter) {
        this.poolExecutor = new ThreadPoolExecutor(coreSize, maxSize,
                1000, TimeUnit.SECONDS, new LinkedBlockingQueue<>(maxSize));
        this.adapter = adapter;
    }

    public void accept(Socket socket) {
        poolExecutor.execute(Server.INSTANCE.config.getProcessorFactory().newProcessor(socket, adapter));
    }

    public void send(Socket socket, byte[] response) throws Exception {
        try (OutputStream out = socket.getOutputStream()) {
            out.write(response);
        }
    }

}

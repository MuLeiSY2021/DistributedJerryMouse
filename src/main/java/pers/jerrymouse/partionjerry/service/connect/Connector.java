package pers.jerrymouse.partionjerry.service.connect;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.service.connect.inner.EndpointDao;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Connector implements Runnable {
    private final int port;

    private final EndpointDao endpoint;
    private final Thread thread;
    private STATUS status;

    public Connector(int port, EndpointDao endpoint) {
        this.port = port;
        this.endpoint = endpoint;
        this.status = STATUS.LIVE;
        this.thread = new Thread(this);
    }

    public void listen() throws Exception {
        thread.start();
    }

    public void stop() {
        this.status = STATUS.DEAD;
        thread.interrupt();
    }

    @Override
    public void run() {
        LOG.info("Serve is listening on port " + port);
        try (ServerSocketChannel server = ServerSocketChannel.open();) {
            server.bind(new InetSocketAddress(port));
            while (!status.equals(STATUS.DEAD)) {
                SocketChannel socket = server.accept();
                LOG.info(port + " A socket in");
                endpoint.accept(socket.socket());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public STATUS getStatus() {
        return status;
    }

    public enum STATUS {
        LIVE, DEAD
    }
}

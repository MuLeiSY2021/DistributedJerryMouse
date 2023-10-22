package pers.jerrymouse.mainjerry.distributed;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Slave {
    private final String ip;

    private final int port;

    private final String name;

    private final int beatPort;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private Socket beatSocket;
    private boolean isAlive;

    public Slave(String ip, int port, String name, int beatPort) throws IOException {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.beatPort = beatPort;
        beatSocket = new Socket(ip, beatPort);
        beatSocket.setKeepAlive(true);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public int getBeatPort() {
        return beatPort;
    }

    public Socket getBeatSocket() {
        return beatSocket;
    }

    public void setBeatSocket(Socket socket) {
        this.beatSocket = socket;
    }

    public boolean isAlive() {
        readLock.lock();
        boolean res = this.isAlive;
        readLock.unlock();
        return res;
    }

    public void setAlive() {
        writeLock.lock();
        isAlive = true;
        writeLock.unlock();
    }

    public void setDead() {
        writeLock.lock();
        isAlive = false;
        writeLock.unlock();
    }
}

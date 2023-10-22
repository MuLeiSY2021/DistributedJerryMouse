package pers.jerrymouse.partionjerry.service.connect.inner;


import java.net.Socket;

public interface EndpointDao {

    void accept(Socket socket) throws Exception;

    void send(Socket socket, byte[] response) throws Exception;

}

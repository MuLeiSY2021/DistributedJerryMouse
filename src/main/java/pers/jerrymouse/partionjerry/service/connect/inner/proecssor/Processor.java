package pers.jerrymouse.partionjerry.service.connect.inner.proecssor;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.connect.inner.AdapterDao;
import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

import java.io.IOException;
import java.net.Socket;

public class Processor implements ProcessorDao {

    public final Socket socket;

    public final AdapterDao adapter;

    public Processor(Socket socket, AdapterDao adapter) {
        this.socket = socket;
        this.adapter = adapter;
    }

    @Override
    public void send(ServletResponse response) throws Exception {
        response.flushBuffer(socket.getOutputStream());
    }

    @Override
    public void run() {
        execute();
    }

    protected void execute() {
        try {
            LOG.info("Get request");
            socket.setSoTimeout(Server.INSTANCE.config.getOutTime());
            ServletResponse response = adapter.handler(getServletRequest(socket));

            response.setHeader("server-name", Server.INSTANCE.config.getName());
            send(response);
            LOG.info("Send response");
        } catch (Exception e) {
            LOG.error(e);
            try {
                send404();
            } catch (Exception ex) {
                LOG.error(ex);
            }
        }
    }

    protected void send404() throws Exception {
        send(adapter.handler404());
    }

    public CustomServletServerRequest getServletRequest(Socket socket) throws IOException {
        return new CustomServletServerRequest(socket.getInputStream());
    }

    protected void execute(CustomServletServerRequest request) {
        try {
            LOG.info("Get request");
            socket.setSoTimeout(Server.INSTANCE.config.getOutTime());
            ServletResponse response = adapter.handler(request);

            response.setHeader("server-name", Server.INSTANCE.config.getName());
            send(response);
            LOG.info("Send response");
        } catch (Exception e) {
            LOG.error(e);
            try {
                send404();
            } catch (Exception ex) {
                LOG.error(ex);
            }
        }
    }
}

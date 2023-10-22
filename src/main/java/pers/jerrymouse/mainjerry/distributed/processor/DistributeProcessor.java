package pers.jerrymouse.mainjerry.distributed.processor;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.mainjerry.distributed.linktoslave.MappingStrategy;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.connect.inner.AdapterDao;
import pers.jerrymouse.partionjerry.service.connect.inner.proecssor.Processor;
import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.utils.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DistributeProcessor extends Processor {


    public DistributeProcessor(Socket socket, AdapterDao adapter) {
        super(socket, adapter);
    }

    @Override
    public void run() {
        try {
            CustomServletServerRequest request = getServletRequest(socket);

            if (!request.hasSession()) {
                request.setCookiesHead();
            }
            if (request.getRequestPath().startsWith("/monitor")) {
                super.execute(request);
                return;
            }

            Socket slave = MappingStrategy.INSTANCE.getServer(request);
            slave.setSoTimeout(Server.INSTANCE.config.getOutTime());

            try (OutputStream out = slave.getOutputStream();
                 InputStream in = slave.getInputStream();
                 OutputStream outSocket = super.socket.getOutputStream()) {

                LOG.info("Sending response");
                request.flushBuffer(out);


                outSocket.write(Utils.inputStreamToByteArray(in));
                outSocket.flush();
            }
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

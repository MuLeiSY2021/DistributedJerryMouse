package pers.jerrymouse.partionjerry.service.connect.inner.proecssor;

import pers.jerrymouse.servlet.response.ServletResponse;

public interface ProcessorDao extends Runnable {

    void send(ServletResponse response) throws Exception;


}

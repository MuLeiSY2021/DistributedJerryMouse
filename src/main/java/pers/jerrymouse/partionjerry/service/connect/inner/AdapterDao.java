package pers.jerrymouse.partionjerry.service.connect.inner;

import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

public interface AdapterDao {

    ServletResponse handler(CustomServletServerRequest httpRequest) throws Exception;

    ServletResponse handler404() throws Exception;
}

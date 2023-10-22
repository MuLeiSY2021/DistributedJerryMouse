package pers.jerrymouse.monitor;

import com.google.gson.Gson;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.config.WebAppConfig;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.partionjerry.service.config.Config;
import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.request.ServletRequest;

import java.io.IOException;

public class WebAppInfo extends Servlet {


    @Override
    public void doGet(ServletRequest request, ServletResponse response) throws IOException, Exception {
        String name = request.getParameter("name");
        WebAppResponse info = new WebAppResponse();
        Config config = Server.INSTANCE.config;

        for (WebAppConfig webAppConfig : config.getWebApps()) {
            if (!webAppConfig.getDisplayName().equals(name)) {
                continue;
            }
            info.description = webAppConfig.getDescription();
            ;
            for (Servlet servlet : webAppConfig.getServlets()) {
                info.servlet_name.add(servlet.getName());
                info.servlet_class.add(servlet.getClazz());
                info.servlet_mapping.add(servlet.getMapping());
            }
        }


        response.setHeader("Content-Type", "application/json");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(info));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);

    }

    @Override
    public void doPost(ServletRequest request, ServletResponse response) throws IOException, Exception {
        doGet(request, response);
    }
}

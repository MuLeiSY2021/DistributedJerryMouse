package pers.jerrymouse.monitor;

import com.google.gson.Gson;
import pers.jerrymouse.mainjerry.distributed.Slave;
import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.partionjerry.service.config.WebAppConfig;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.mainjerry.distributed.linktoslave.MappingStrategy;
import pers.jerrymouse.partionjerry.service.config.Config;
import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.request.ServletRequest;

import java.io.IOException;
import java.util.stream.Collectors;

public class MasterInfo extends Servlet {


    @Override
    public void doGet(ServletRequest request, ServletResponse response) throws IOException, Exception {
        MasterResponse info = new MasterResponse();

        Config config = Server.INSTANCE.config;
        info.description = config.getDescription();
        info.name = config.getName();
        info.port = config.getPort();

        info.slavesName.addAll(MappingStrategy.INSTANCE.slaveMap.values().stream().map(Slave::getName).collect(Collectors.toList()));
        info.slaves_status.addAll(MappingStrategy.INSTANCE.slaveMap.values().stream().map(Slave::isAlive).collect(Collectors.toList()));

        for (WebAppConfig webAppConfig : config.getWebApps()) {
            info.webapps_name.add(webAppConfig.getDisplayName());
        }


        response.setHeader("Content-Type", "application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(info));
        response.setStatus(200);
    }

    @Override
    public void doPost(ServletRequest request, ServletResponse response) throws IOException, Exception {
        doGet(request, response);
    }
}

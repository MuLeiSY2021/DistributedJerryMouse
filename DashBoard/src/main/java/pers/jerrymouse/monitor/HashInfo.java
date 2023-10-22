package pers.jerrymouse.monitor;

import com.google.gson.Gson;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.mainjerry.distributed.linktoslave.MappingStrategy;
import pers.jerrymouse.mainjerry.distributed.Slave;
import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.request.ServletRequest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HashInfo extends Servlet {
    int mapping_num;

    List<String> urls = new LinkedList<>();

    List<String> cookies = new LinkedList<>();


    @Override
    public void doGet(ServletRequest request, ServletResponse response) throws IOException, Exception {
        urls = new LinkedList<>();
        cookies = new LinkedList<>();
        String name = request.getParameter("name");
        for (Map.Entry<String, Slave> stringSlaveEntry : MappingStrategy.INSTANCE.getHashMapEntrySet()) {
            String[] set = stringSlaveEntry.getKey().split("; ");
            if (stringSlaveEntry.getValue().getName().equals(name)) {
                cookies.add(stringSlaveEntry.getKey().split(";")[0]);
                urls.add(set[set.length - 1]);
            }
        }
        mapping_num = urls.size();
        response.setHeader("Content-Type", "application/json");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(this));
        System.out.println(gson.toJson(this));
        //Access-Control-Allow-Methods: POST, GET, OPTIONS, DELETE
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setStatus(200);
        response.setHeader("Access-Control-Allow-Origin", "*");

    }

    @Override
    public void doPost(ServletRequest request, ServletResponse response) throws IOException, Exception {
        doGet(request, response);
    }
}

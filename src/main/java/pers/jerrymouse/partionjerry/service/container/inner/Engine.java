package pers.jerrymouse.partionjerry.service.container.inner;

import pers.jerrymouse.log.LOG;
import pers.jerrymouse.servlet.request.CustomServletServerRequest;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.servlet.specialsevlet.FileProcessServlet;
import pers.jerrymouse.servlet.specialsevlet.Status404Servlet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Engine {
    private final Map<String, Context> mapping = new HashMap<>();

    private final Map<String, String> routes = new HashMap<>();

    public void register(Context c) {
        mapping.put(c.getPath(), c);
        routes.put(c.getRootURL(), c.getRootURL());
    }

    public ServletResponse process(CustomServletServerRequest request) throws Exception {
        ServletResponse response;
        LOG.info("request:" + request.getRequestPath());
        String root = getRoot(request.getRequestPath());


        if (root == null) {
            LOG.info("root not found");
            return mapping.get(Status404Servlet.TAG).invoke(request);
        }
        LOG.info("root:" + root);

        if (mapping.containsKey(request.getRequestPath())) {
            LOG.info("找到了对应的servlet");
            Context context = getContext(request.getRequestPath());
            response = context.invoke(request);
            if (!response.is404()) {
                return response;
            }
        } else if (askFile(request.getRequestPath())) {
            response = mapping.get(getRoot(request.getRequestPath()) + "/" + FileProcessServlet.TAG).invoke(request);
            LOG.info("请求文件啦");
            if (response.getHeader("get-success").equals("true")) {
                return response;
            }
        }
        LOG.info("啥也没找到 404");

        return mapping.get(Status404Servlet.TAG).invoke(request);
    }

    public ServletResponse process404() throws Exception {
        return mapping.get(Status404Servlet.TAG).invoke(null);
    }

    public String getRoot(String requestPath) {
        for (String root : routes.keySet().stream().sorted(Comparator.comparingInt(String::length).reversed()).collect(Collectors.toList())) {
            if (requestPath.startsWith(root)) {
                return root;
            }
        }
        LOG.warn("Not get root path: " + requestPath);
        LOG.debug(routes.keySet().toString());
        return null;
    }

    public boolean askFile(String path) {
        return path.lastIndexOf(".") != -1;
    }

    private Context getContext(String path) {
        return mapping.get(path);
    }
}

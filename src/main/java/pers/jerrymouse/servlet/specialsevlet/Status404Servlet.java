package pers.jerrymouse.servlet.specialsevlet;

import pers.jerrymouse.partionjerry.Server;
import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

import java.io.IOException;

public class Status404Servlet extends FileProcessServlet {
    public static final String TAG = "Status404Servlet";


    @Override
    public void doGet(ServletRequest request, ServletResponse response) throws IOException {
    }

    @Override
    public void doPost(ServletRequest request, ServletResponse response) throws IOException {
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        response.setStatus(404);
        // 判断文件是否存在
        String filePath = Server.INSTANCE.workdir + "/core/pages/error404.html";
        super.fileOutput(filePath, response);
    }


}

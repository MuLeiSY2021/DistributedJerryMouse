package org.example;

import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;
import pers.jerrymouse.servlet.session.Session;

import java.io.PrintWriter;

public class GetFormServlet extends Servlet {
    public void doGet(ServletRequest request, ServletResponse response) throws Exception {
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String title = "使用 GET 方法读取表单数据";
        // 处理中文
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String docType = "<!DOCTYPE html> \n";
        Session session = request.getSession();
        session.setAttribute("aaa", "bbb");
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>站点名</b>："
                + name + "\n" +
                "  <li><b>网址</b>："
                + request.getParameter("url") + "\n" +
                "</ul>\n" +
                "</body></html>");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setStatus(200);

    }

    // 处理 POST 方法请求的方法
    public void doPost(ServletRequest request, ServletResponse response) throws Exception {
        doGet(request, response);
    }
}

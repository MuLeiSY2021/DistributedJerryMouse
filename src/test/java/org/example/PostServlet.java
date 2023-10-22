package org.example;

import pers.jerrymouse.servlet.Servlet;
import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

import java.io.PrintWriter;

public class PostServlet extends Servlet {
    public void doGet(ServletRequest request, ServletResponse response) throws Exception {
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String title = "使用 POST 方法读取表单数据";
        // 处理中文
        String name = new String(request.getParameter("name").getBytes("ISO8859-1"), "UTF-8");
        request.getSession().setAttribute("aaa", "bbb");
        String docType = "<!DOCTYPE html> \n";
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
    }

    // 处理 POST 方法请求的方法
    public void doPost(ServletRequest request, ServletResponse response) throws Exception {
        doGet(request, response);
    }
}

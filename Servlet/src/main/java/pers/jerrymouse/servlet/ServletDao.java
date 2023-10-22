package pers.jerrymouse.servlet;

import pers.jerrymouse.servlet.request.ServletRequest;
import pers.jerrymouse.servlet.response.ServletResponse;

import java.io.IOException;

public interface ServletDao {
    void doGet(ServletRequest request, ServletResponse response) throws IOException, Exception;

    void doPost(ServletRequest request, ServletResponse response) throws IOException, Exception;

    void service(ServletRequest request, ServletResponse response) throws IOException, Exception;

    String getWorkdir();
}

package pers.jerrymouse.monitor;

import java.util.LinkedList;
import java.util.List;

public class WebAppResponse {
    String description;
    List<String> servlet_name = new LinkedList<>();
    List<String> servlet_class = new LinkedList<>();
    List<String> servlet_mapping = new LinkedList<>();

    public WebAppResponse() {
    }


}
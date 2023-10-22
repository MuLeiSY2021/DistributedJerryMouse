package pers.jerrymouse.monitor;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class MasterResponse {
    String name;
    String description;
    int port;
    @SerializedName("slave_name")
    List<String> slavesName = new LinkedList<>();
    List<Boolean> slaves_status = new LinkedList<>();
    List<String> webapps_name = new LinkedList<>();

    public MasterResponse() {
    }


}
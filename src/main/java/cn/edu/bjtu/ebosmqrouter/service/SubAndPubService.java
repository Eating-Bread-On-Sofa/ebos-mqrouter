package cn.edu.bjtu.ebosmqrouter.service;

import org.springframework.stereotype.Service;

@Service
public class SubAndPubService {
    private static final String url_01 = "http://localhost:8081/api/device/subscribe";
    private static final String url_02 = "http://localhost:8082/api/command/subscribe";
    private static final String url_03 = "http://localhost:8083/api/subscribe";
    private static final String url_04 = "http://localhost:8085/api/service/subscribe";
    private static final String url_05 = "http://localhost:8086/api/subscribe";
    private static final String url_06 = "http://localhost:8089/api/gateway/subscribe";
    private static final String url_07 = "http://localhost:8090/api/instance/subscribe";
    private static final String url_08 = "http://localhost:8091/api/profile/subscribe";
    private static final String url_09 = "http://localhost:8092/api/scenario/subscribe";
    private static final String url_11 = "http://localhost:8081/api/device/publish";
    private static final String url_12 = "http://localhost:8082/api/command/publish";
    private static final String url_13 = "http://localhost:8083/api/publish";
    private static final String url_14 = "http://localhost:8085/api/service/publish";
    private static final String url_15 = "http://localhost:8086/api/publish";
    private static final String url_16 = "http://localhost:8089/api/gateway/publish";
    private static final String url_17 = "http://localhost:8090/api/instance/publish";
    private static final String url_18 = "http://localhost:8091/api/profile/publish";
    private static final String url_19 = "http://localhost:8092/api/scenario/publish";

    public String getSubUrl(String serviceName){
        switch (serviceName){
            case "设备管理":
                return url_01;
            case "指令管理":
                return url_02;
            case "规则引擎":
                return url_03;
            case "服务管理":
                return url_04;
            case "运维监控":
                return url_05;
            case "网关管理":
                return url_06;
            case "网关实例":
                return url_07;
            case "模板管理":
                return url_08;
            default:
                return url_09;
        }
    }

    public String getPubUrl(String serviceName){
        switch (serviceName){
            case "设备管理":
                return url_11;
            case "指令管理":
                return url_12;
            case "规则引擎":
                return url_13;
            case "服务管理":
                return url_14;
            case "运维监控":
                return url_15;
            case "网关管理":
                return url_16;
            case "网关实例":
                return url_17;
            case "模板管理":
                return url_18;
            default:
                return url_19;
        }
    }

}

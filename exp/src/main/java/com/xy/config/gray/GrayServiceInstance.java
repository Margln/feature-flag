package com.xy.config.gray;

import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author margln
 * @date 2024/3/15
 */
@Data
public class GrayServiceInstance {


    List<ServiceInstance> all;

    List<ServiceInstance> grayList;

    List<ServiceInstance> normalList;


    public GrayServiceInstance() {
        all = new ArrayList<>();
        grayList = new ArrayList<>();
        normalList= new ArrayList<>();
    }

    public static boolean isGray(Request request) {

        Object context = request.getContext();
        if (context instanceof RequestDataContext) {

            List<String> grayTag = ((RequestDataContext) context).getClientRequest().getHeaders().get("x-gray");
            if (grayTag != null && !grayTag.isEmpty()) {
                return true;
            }

        }

        return false;
    }

    public static boolean isPrd(Request request) {

        Object context = request.getContext();
        if (context instanceof RequestDataContext) {

            List<String> grayTag = ((RequestDataContext) context).getClientRequest().getHeaders().get("x-prd");
            if (grayTag != null && !grayTag.isEmpty()) {
                return true;
            }

        }

        return false;
    }

    public void add(String envTag, ServiceInstance instance) {

        all.add(instance);

        if ("uat".equals(envTag)) {
            grayList.add(instance);
        }
        if ("normal".equals(envTag)) {
            normalList.add(instance);
        }
    }


}

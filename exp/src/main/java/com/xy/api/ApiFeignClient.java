package com.xy.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author margln
 * @date 2024/3/15
 */
@FeignClient(name = "testapp")
public interface ApiFeignClient {

    @RequestMapping(value = "/api/getinstance",consumes = MediaType.APPLICATION_JSON_VALUE)
    String getInstance();


}

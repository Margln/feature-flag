package com.xy.gateway.filter;

import com.xy.gateway.UserContext;
import com.xy.gateway.gray.GrayData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author margln
 * @date 2024/3/16
 */
@Component
@Order(3)
@Slf4j
public class GrayFilter implements GlobalFilter{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Map<String, Object> userInfo = UserContext.getUserInfo();

        if (GrayData.isGray("grayUat", userInfo, false)) {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.add("x-gray", userInfo.get("userNo").toString());
        }

        if (GrayData.isPrd("grayUat", false)) {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.add("x-prd", userInfo.get("userNo").toString());
        }
        return chain.filter(exchange);
    }


}

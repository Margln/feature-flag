package com.xy.gateway.filter;

import com.xy.gateway.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author margln
 * @date 2024/3/16
 */
@Component
@Order(2)
@Slf4j
public class UserInitFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<HttpCookie> userTokens = exchange.getRequest().getCookies().get("user-token");

        Map<String, Object> userinfo;
        if (userTokens != null && !userTokens.isEmpty()) {
            HttpCookie cookie = userTokens.get(0);
            String token = cookie.getValue();
            userinfo = transferToUserInfo(token);
        }else  {
            userinfo = transferToUserInfo("");
        }
        UserContext.setUserInfo(userinfo);
        Mono<Void> filter = chain.filter(exchange);
        return filter.then(Mono.fromRunnable(UserContext::clear));


    }

    private Map<String, Object> transferToUserInfo(String token) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("userNo", "00001");
        return map;
    }
}

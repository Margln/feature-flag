package com.xy.filter;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * @author margln
 * @date 2024/3/16
 */
@Component
@Order(1)
public class InitFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        List<String> traceIds = exchange.getRequest().getHeaders().get("traceId");
        String traceId = null;
        if(traceIds != null && !traceIds.isEmpty()) {
            traceId = traceIds.get(0);
        }
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        exchange.getRequest().mutate().header("traceId", traceId).build();
        MDC.put("traceId", traceId);
        return chain.filter(exchange);
    }

}

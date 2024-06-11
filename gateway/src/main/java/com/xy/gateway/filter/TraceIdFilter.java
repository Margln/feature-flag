package com.xy.gateway.filter;

import com.xy.gateway.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * @author margln
 * @date 2024/3/16
 */
@Slf4j
@Component
@Order(1)
public class TraceIdFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> traceIds = headers.get("traceId");
        String traceId = null;
        if(traceIds != null && !traceIds.isEmpty()){
            traceId = traceIds.get(0);
        }
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
            MDC.put("traceId", traceId);
            exchange.getRequest().mutate().header("traceId", traceId).build();
        }
        log.info("traceId:{}", MDC.get("traceId"));
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> MDC.remove("traceId")));
    }

}

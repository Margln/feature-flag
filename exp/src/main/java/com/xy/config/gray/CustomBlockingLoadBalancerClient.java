package com.xy.config.gray;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: ekko
 * @Description: 自定义CustomBlockingLoadBalancerClient.java
 * @Date: 2023/9/20 16:16
 */
public class CustomBlockingLoadBalancerClient extends BlockingLoadBalancerClient {
    private final ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerClientFactory;

    public CustomBlockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory) {
        super(loadBalancerClientFactory);
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    @Override
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        ReactiveLoadBalancer<ServiceInstance> loadBalancer = loadBalancerClientFactory.getInstance(serviceId);
        if (loadBalancer == null) {
            return null;
        }
        CompletableFuture<Response<ServiceInstance>> f = CompletableFuture.supplyAsync(() -> Mono.from(loadBalancer.choose(request)).block());
        Response<ServiceInstance> loadBalancerResponse;
        try {
            loadBalancerResponse = f.get();
        } catch (Exception e) {
            return null;
        }
        return loadBalancerResponse.getServer();
    }
}

package com.xy.config.gray;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.DiscoveryClientServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * @author margln
 * @date 2024/3/15
 */
@Component
@LoadBalancerClients(defaultConfiguration = GrayConfig.class)
public class GrayConfig {

    @Value("${eureka.instance.metadata-map.envTag}")
    private String envTag;

    @Resource
    LoadBalancerClientFactory loadBalancerClientFactory;

    @Bean
    public GrayServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {

        DiscoveryClient discoveryClient = context.getBean(DiscoveryClient.class);
        //ObjectProvider<LoadBalancerCacheManager> cacheManager =context.getBeanProvider(LoadBalancerCacheManager.class);
        DiscoveryClientServiceInstanceListSupplier listSupplier = new DiscoveryClientServiceInstanceListSupplier(discoveryClient, context.getEnvironment());
        return new GrayServiceInstanceListSupplier(listSupplier, envTag);

    }


    @Bean
    public LoadBalancerClient blockingLoadBalancerClient() {
        return new CustomBlockingLoadBalancerClient(loadBalancerClientFactory);
    }


    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }



}

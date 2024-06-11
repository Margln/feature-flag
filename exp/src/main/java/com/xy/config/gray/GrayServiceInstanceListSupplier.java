package com.xy.config.gray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;


/**
 * @author margln
 * @date 2024/3/15
 */
@Slf4j
public class GrayServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    Flux<GrayServiceInstance> grayServiceInstance;

    private String envTag;

    public GrayServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        super(delegate);
    }

    public GrayServiceInstanceListSupplier(ServiceInstanceListSupplier listSupplier, LoadBalancerCacheManager cacheManager) {
        super(listSupplier);
        this.grayServiceInstance = delegate.get().take(1).map(l -> {
            GrayServiceInstance grayServiceInstance = new GrayServiceInstance();
            for (ServiceInstance serviceInstance : l) {
                grayServiceInstance.add(serviceInstance.getMetadata().get("envTag"), serviceInstance);
            }
            return grayServiceInstance;
        });
    }

    public GrayServiceInstanceListSupplier(ServiceInstanceListSupplier delegate, String envTag) {
        super(delegate);
        this.envTag = envTag;
        this.grayServiceInstance = delegate.get().take(1).map(l -> {
            GrayServiceInstance grayServiceInstance = new GrayServiceInstance();
            for (ServiceInstance serviceInstance : l) {
                grayServiceInstance.add(serviceInstance.getMetadata().get("envTag"), serviceInstance);
            }
            return grayServiceInstance;
        });
    }

    @Override
    public Flux<List<ServiceInstance>> get() {

        log.debug("envTag:{}", envTag);
        if("uat".equals(this.envTag)){
            return grayServiceInstance.map(GrayServiceInstance::getGrayList);
        }
        if("normal".equals(envTag)){
            return grayServiceInstance.map(GrayServiceInstance::getNormalList);
        }

        return grayServiceInstance.map(GrayServiceInstance::getAll);
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {


        if(GrayServiceInstance.isGray(request)){
            return grayServiceInstance.map(g -> {
                if (g.grayList.isEmpty()) {
                    return Collections.emptyList();
                }else {
                    return g.getGrayList();
                }
            });
        }

        if(GrayServiceInstance.isPrd(request)){
            return grayServiceInstance.map(g -> {
                if (g.grayList.isEmpty()) {
                    return Collections.emptyList();
                }else {
                    return g.getNormalList();
                }
            });
        }

        log.debug("envTag:{}", envTag);
        if("uat".equals(this.envTag)){
            return grayServiceInstance.map(GrayServiceInstance::getGrayList);
        }
        if("normal".equals(envTag)){
            return grayServiceInstance.map(GrayServiceInstance::getNormalList);
        }


        return grayServiceInstance.map(GrayServiceInstance::getAll);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }
}

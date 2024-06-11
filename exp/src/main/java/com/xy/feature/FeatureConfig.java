package com.xy.feature;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author margln
 * @date 2024/4/17
 */
@Slf4j
@Configuration
public class FeatureConfig {

    @Bean(name = "cliGroup")
    NioEventLoopGroup cliGroup(){
        return new NioEventLoopGroup();
    }

}

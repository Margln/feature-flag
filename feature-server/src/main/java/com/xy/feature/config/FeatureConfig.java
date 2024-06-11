package com.xy.feature.config;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author margln
 * @date 2024/4/17
 */
@Slf4j
@Configuration
public class FeatureConfig {


    @Bean(name = "serBoss")
    NioEventLoopGroup serBoss(){
        return new NioEventLoopGroup(1);
    }


    @Bean(name = "serWorker")
    NioEventLoopGroup serWorker(){
        return new NioEventLoopGroup();
    }


    @Bean("appIpChannelMap")
    Map<String, Map<SocketAddress, Channel>> appIpChannelMap(){
        return new ConcurrentHashMap<>(32);
    }

    @Bean("ipAppMap")
    Map<SocketAddress, String> ipAppMap(){
        return new ConcurrentHashMap<>(32);
    }

}

package com.xy.feature.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author margln
 * @date 2024/4/16
 */
@Slf4j
//@Component
@Order
public class FeatureClient {

    private final AtomicInteger getServersCount = new AtomicInteger(0);

    @Value("${featureServerPort:9100}")
    private Integer serPort;

    Bootstrap bootstrap = new Bootstrap();
    NioEventLoopGroup client = new NioEventLoopGroup(1);

    @PostConstruct
    public void start() {
        connect();
    }

    @PreDestroy
    public void stop(){
        client.shutdownGracefully();
    }

    private List<String> getServers() {
        return Arrays.asList("127.0.0.1");
    }

    void connect() {

        try {
            ChannelFuture fch = bootstrap.group(client)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(getRemoteAddress())
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                        }
                    })
                    .connect()
                    .sync()
                    .addListener(future -> {

                    });

            fch.channel().closeFuture().addListener(
                    ch -> {
                        if (ch.isSuccess()) {
                            connect();
                        }
                    }
            ).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private InetSocketAddress getRemoteAddress() {
        List<String> servers = null;

        while (servers == null || servers.isEmpty()) {
            servers = getServers();
            if (getServersCount.incrementAndGet() == 10) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    log.error("getServers error {}", e.getMessage(), e);
                }
                getServersCount.compareAndSet(10,0);
            }
        }
        return new InetSocketAddress(servers.get(0), serPort);

    }




}

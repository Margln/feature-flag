package com.xy.feature.client;

import com.xy.feature.msg.MessageProtocol;
import com.xy.feature.msg.StringMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 心跳检测服务端 对应的服务端在netty-server heartbeat 包下的NettyClient
 */
@Component
@Slf4j
public class NettyClient implements Runnable{

    private final AtomicInteger getServersCount = new AtomicInteger(0);

    @Value("${spring.application.name}")
    String appKey;

    @Resource(name = "cliGroup")
    NioEventLoopGroup group;

    @Resource
    NettyClientChannelInitializer initializer;


    @Resource
    DiscoveryClient discoveryClient;



    private ChannelFuture connect;

    private Bootstrap bootstrap;



    public void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress("", 100)
                .handler(initializer);
        //连接
        connect();
    }


    /**
     * 重连方法
     */
    public void connect() {
        try {
            if (getServersCount.incrementAndGet() > 10) {
                Thread.sleep(5000);
                getServersCount.compareAndSet(getServersCount.get(), 0);
            }

            List<ServiceInstance> instances = discoveryClient.getInstances("feature-server");
            if (instances == null || instances.isEmpty()) {
                log.warn("feature-server not ready!");
                Thread.sleep(5000);
                connect();
            }
            for (ServiceInstance instance : instances) {
                String host = instance.getHost();
                connect = bootstrap.connect(host, 8787).sync();
                if (connect.isSuccess()) {
                    log.info("服务端链接成功... send init msg ");
                    StringMsg init = StringMsg.getInitMsg(appKey);
                    connect.channel().writeAndFlush(new MessageProtocol(init));
                    connect.channel().closeFuture().sync().addListener(cf -> {
                        log.info("close future ....  do connect ");
                        if (cf.isSuccess()) {
                            connect();
                        }
                    });
                    break;
                }
            }

        } catch (Exception e) {
            log.error(" connect error: {}", e.getMessage(), e);
            //再重连
            connect();
        }
    }

    public void stop() {
        connect.channel().close();
        group.shutdownGracefully();
    }


    @Override
    public void run() {
        start();
    }
}

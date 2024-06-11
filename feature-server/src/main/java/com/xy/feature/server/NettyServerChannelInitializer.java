package com.xy.feature.server;

import com.xy.feature.msg.MessageDecoder;
import com.xy.feature.msg.MessageEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    /**
     * NioSocketChannel，异步的客户端TCP Socket连接
     */
    @Resource(name = "appIpChannelMap")
    Map<String, Map<SocketAddress, Channel>> appIpChannelMap;

    @Resource(name = "ipAppMap")
    Map<SocketAddress, String> ipAppMap;

    @Resource
    NettyServerHandler nettyServerHandler;


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                //空闲状态的处理器
                .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                .addLast(new MessageDecoder())
                .addLast(new MessageEncoder())
                .addLast(nettyServerHandler);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("有新连接加入了++++...... id:{}, ip:{}", channel.id(), channel.remoteAddress());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        log.info("exceptionCaught ...... id:{}, ip:{}, msg:{}",
                channel.id(), socketAddress, cause.getMessage());
        channel.close();
        String appKey = ipAppMap.get(socketAddress);
        if (appIpChannelMap.containsKey(appKey)) {
            appIpChannelMap.get(appKey).remove(socketAddress);
        }
    }
}

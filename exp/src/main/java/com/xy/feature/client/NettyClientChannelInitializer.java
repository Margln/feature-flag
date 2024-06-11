package com.xy.feature.client;

import com.xy.feature.msg.MessageDecoder;
import com.xy.feature.msg.MessageEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Resource
    NettyClientHandler clientHandler;

    @Lazy
    @Resource
    NettyClient nettyClient;


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                //空闲状态的handler
                .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                .addLast(new MessageDecoder())
                .addLast(new MessageEncoder())
                .addLast(clientHandler);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info(" {} ---- exceptionCaught --- {}, ",
                ctx.channel().remoteAddress(), cause.getMessage(), cause);

        ctx.channel().eventLoop().schedule(()-> nettyClient.connect(), 3, TimeUnit.SECONDS);

    }
}

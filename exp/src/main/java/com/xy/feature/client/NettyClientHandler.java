package com.xy.feature.client;

import com.xy.feature.msg.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends MessageTransferHandler<ObjectMsg> {

    @Value("${spring.application.name}")
    String appKey;

    //@Lazy
    //@Resource
    //NettyClient nettyClient;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接激活 == {} ", ctx.channel().id());

    }

    /*@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断线了......{} ", ctx.channel().id());
        ctx.channel().eventLoop().schedule(() -> {
            log.info("断线重连...... ");
            //重连
            nettyClient.connect();
        }, 3L, TimeUnit.SECONDS);
    }*/


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ObjectMsg msg) throws Exception {

        if (MsgType.UPDATE == msg.getType()) {
            Object data = msg.getData();
            log.info("msg type: {}, all data: {} ", msg.getType(), msg.getData());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果是空闲状态事件
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
                log.info("空闲......{}, send ping msg ", ctx.channel().id());
                //发送ping 保持心跳链接
                StringMsg ping = StringMsg.getPingMsg(appKey);
                ctx.writeAndFlush(new MessageProtocol(ping));
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

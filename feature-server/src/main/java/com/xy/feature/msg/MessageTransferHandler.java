package com.xy.feature.msg;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author margln
 * @date 2024/4/17
 */
@Slf4j
public abstract class MessageTransferHandler<M extends Message<?>> extends ChannelInboundHandlerAdapter {

    private final TypeParameterMatcher matcher;

    protected MessageTransferHandler() {
        this.matcher = TypeParameterMatcher.find(this, MessageTransferHandler.class, "M");

    }

    public boolean acceptInboundMessage(Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof MessageProtocol)) {
            ctx.fireChannelRead(msg);
            return;
        }
        String s = new String(((MessageProtocol) msg).getContent(), StandardCharsets.UTF_8);
        log.info("msg: {}", s);
        Message message = JSON.parseObject(s, Message.class);
        int type = message.getType();

        if (MsgType.INIT == type || MsgType.PING == type) {
            channelRead0(ctx, (M) JSON.parseObject(s, StringMsg.class));
            return;
        }
        if (MsgType.UPDATE == type) {
            channelRead0(ctx, (M) JSON.parseObject(s, ObjectMsg.class));
            return;
        }
        log.error("unknow msg type: {}, s:{}", type, s);
    }

    protected void channelRead0(ChannelHandlerContext ctx, M msg) throws Exception {
    }


}

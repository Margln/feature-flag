package com.xy.feature.msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author margln
 * @date 2024/4/16
 */
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol ptl, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(ptl.getLen());
        byteBuf.writeBytes(ptl.getContent());
    }
}

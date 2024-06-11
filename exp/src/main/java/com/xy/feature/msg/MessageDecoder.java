package com.xy.feature.msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author margln
 * @date 2024/4/16
 */
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readInt();
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        MessageProtocol ptl = new MessageProtocol();
        ptl.setLen(len);
        ptl.setContent(bytes);
        ctx.fireChannelRead(ptl);
    }
}

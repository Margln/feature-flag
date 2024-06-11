package com.xy.feature.server;

import com.xy.feature.msg.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author margln
 */
@Slf4j
@ChannelHandler.Sharable
//@Component
public class NettyServerHandler extends MessageTransferHandler<StringMsg> {


    @Resource(name = "appIpChannelMap")
    Map<String, Map<SocketAddress, NioSocketChannel>> appIpChannelMap;

    @Resource(name = "ipAppMap")
    Map<SocketAddress, String> ipAppMap;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("NettyServerHandler channelActive ..... {}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, StringMsg msg) throws Exception {
        Channel channel = ctx.channel();
        if (msg.getType() == MsgType.PING) {
            log.info("msg-data: {}", msg.getData());
        }

        if (msg.getType() == MsgType.INIT) {
            log.info("msg-data: {}, do init ", msg.getData());

            String appKey = msg.getAppKey();
            SocketAddress socketAddress = channel.remoteAddress();
            ipAppMap.put(socketAddress, appKey);
            if (appIpChannelMap.containsKey(appKey)) {
                appIpChannelMap.get(appKey).put(socketAddress, (NioSocketChannel) channel);
            }else {
                Map<SocketAddress, NioSocketChannel> ipChannelMap = new ConcurrentHashMap<>(8);
                ipChannelMap.put(socketAddress, (NioSocketChannel)channel);
                appIpChannelMap.put(appKey, ipChannelMap);
            }
            log.info("do send init data ....");
            MessageProtocol allData = new MessageProtocol(new ObjectMsg(MsgType.UPDATE, "all data"));
            channel.writeAndFlush(allData);
        }
    }

}

package com.xy.feature.server;

import com.xy.feature.msg.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
@Component
public class NettyServerHandler extends MessageTransferHandler<StringMsg> {


    @Resource(name = "appIpChannelMap")
    Map<String, Map<SocketAddress, Channel>> appIpChannelMap;

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
                appIpChannelMap.get(appKey).put(socketAddress, channel);
            }else {
                Map<SocketAddress, Channel> ipChannelMap = new ConcurrentHashMap<>(8);
                ipChannelMap.put(socketAddress, channel);
                appIpChannelMap.put(appKey, ipChannelMap);
            }
            log.info("do send init data ....");
            MessageProtocol allData = new MessageProtocol(new ObjectMsg(MsgType.UPDATE, "all data"));
            channel.writeAndFlush(allData);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        //空闲状态的事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            log.info("ip: {} >>>> IdleStateEvent: {}", socketAddress, event.state());
            //已经10秒钟没有读时间了
            if (event.state().equals(IdleState.READER_IDLE)){
                // 心跳包丢失，10秒没有收到客户端心跳 (断开连接)
                ctx.channel().close().sync();
                log.warn("已与: {} 断开连接", socketAddress);
                String appKey = ipAppMap.get(socketAddress);
                ipAppMap.remove(socketAddress);
                if (appIpChannelMap.containsKey(appKey)) {
                    appIpChannelMap.get(appKey).remove(socketAddress);
                }
            }
        }else {
            ctx.fireUserEventTriggered(evt);
        }
    }

}

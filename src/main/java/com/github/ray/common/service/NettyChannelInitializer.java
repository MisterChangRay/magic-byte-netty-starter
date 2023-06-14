package com.github.ray.common.service;

import com.github.ray.common.service.handler.DataDecoderHandler;
import com.github.ray.common.service.handler.ExceptionHandler;
import com.github.ray.common.service.handler.IpSecurityHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Scope("prototype")
@Component
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private IpSecurityHandler ipSecurityHandler;
    @Autowired
    private DataDecoderHandler dataDecoderHandler;
    @Autowired
    private ExceptionHandler exceptionHandler;


    @Override
    protected void initChannel(SocketChannel nioDatagramChannel) throws Exception {
        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
        // TCP连接安全识别
        pipeline.addLast(ipSecurityHandler);
        // 限制每个连接每秒发送和接收流量不能大于1024字节
        pipeline.addLast(new ChannelTrafficShapingHandler(1024,1024,1000));
        // 超过60s没有上报数据,则抛出异常; 详见 ExceptionHandler.IdleStateEvent
        pipeline.addLast("handler",new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(dataDecoderHandler);
        pipeline.addLast(exceptionHandler);


    }
}

package com.github.ray.common.service.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Scope("prototype")
@ChannelHandler.Sharable
@Component
public class IpSecurityHandler extends AbstractRemoteAddressFilter<InetSocketAddress> {
    private final static Logger logger = LoggerFactory.getLogger(IpSecurityHandler.class);

    @Override
    protected boolean accept(ChannelHandlerContext channelHandlerContext, InetSocketAddress inetSocketAddress) throws Exception {
        // 这里可以对设备的ip进行监控或者拦截处理

        final InetAddress remoteIp = inetSocketAddress.getAddress();
        logger.info("远程地址 {} 接入成功", remoteIp);
        return false;
    }
}

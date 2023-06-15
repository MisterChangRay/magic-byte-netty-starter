package com.github.ray.common.service;

import com.github.ray.common.utils.DefaultThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NettyServer {
    private static int coreSize = Runtime.getRuntime().availableProcessors();
    private final static Logger logger = LoggerFactory.getLogger(NettyServer.class);
    @Autowired
    NettyChannelInitializer nettyChannelInitializer;
    @Value("${server.netty.startPort:7510}")
    private int startPort;

    public void start() {
        // 接入线程默认使用10个
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(10);
        // 任务线程默认使用核心数*8
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(coreSize * 8
                , new DefaultThreadFactory("magic-byte-netty"));

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(nettyChannelInitializer)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option((ChannelOption.SO_BACKLOG), 1024)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(512))
                    .option(ChannelOption.SO_RCVBUF, 512)
                    .option(ChannelOption.SO_SNDBUF, 512)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
            ;

            ChannelFuture channelFuture = null;
            for(int i=startPort, j = startPort + 10; i<=j; i++) {
                channelFuture = bootstrap.bind(i).sync();
                channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> {
                    //通过回调关闭自己监听的channel
                    future.channel().close();
                });
            }
            logger.info("Netty Server Started Success! PortStart: {}, PortEnd:{}", startPort, startPort + 10);

//            Channel sender = bootstrap.bind(port).sync().channel();
//            sender.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Netty Server Started Fail!", e);
        } finally {
//            bossGroup.shutdownGracefully();
        }
    }



}

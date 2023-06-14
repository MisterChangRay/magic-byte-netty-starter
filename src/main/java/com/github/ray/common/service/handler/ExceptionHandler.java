package com.github.ray.common.service.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * 全局异常处理
 */
@Scope("prototype")
@Component
public class ExceptionHandler extends ChannelDuplexHandler {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.READER_IDLE){
                logger.info("连接长时间没有发送数据");
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

    /**
     * 全局处理消息接收异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(null != cause) {
            logger.error("发生异常, 异常描述", cause);
        }



    }


    /**
     * 全局处理消息发送异常
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                logger.error("数据发送失败, {}",  future.cause());
            }
        }));
    }
}
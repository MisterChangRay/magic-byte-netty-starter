package com.github.ray.common.service.handler;

import com.github.misterchangray.core.MagicByte;
import com.github.ray.common.consts.NettyAttrKeys;
import com.github.ray.common.protocol.Head;
import com.github.ray.common.service.EquipmentManagerService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.ByteOrder;
import java.util.Base64;


/**
 * tcp数据解包器
 *
 * 主要负责按照协议将tcp数据包解包，
 *
 *
 */
@Scope("prototype")
@Component()
public class DataDecoderHandler extends LengthFieldBasedFrameDecoder {
    private final static Logger logger = LoggerFactory.getLogger(DataDecoderHandler.class);
    @Autowired
    EquipmentManagerService equipmentManagerService;


    /**
     * 这里假设报文格式为一下格式：
     *
     * |报文头|报文长度|报文类型|设备编号|报文体|校验和
     *
     */
    public DataDecoderHandler() {
        super(ByteOrder.BIG_ENDIAN, 100, 6,
                2, -8, 0, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 启用netty的溢出检测, 线上观察一段时间后最好重新发包并注释此行
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

        String equipmentSn = null;
        if(ctx.channel().hasAttr(NettyAttrKeys.EQUIPMENT_SN)) {
            equipmentSn = ctx.channel().attr(NettyAttrKeys.EQUIPMENT_SN).get().toString();
        }

        //读取body
        ByteBuf message = (ByteBuf) super.decode(ctx,in);
        if(null == message || in.readerIndex() < 8) {
            return null;
        }
        // 获取上行字节数据, 这里是最原始的数据, 如果加密了请自行解密
        byte[] upLinkBytes = ByteBufUtil.getBytes(message);
        //释放 buffer
        message.release();


        if(!ctx.channel().hasAttr(NettyAttrKeys.INIT_ONLINE)) {
            // 这里做设备上线初始化操作，比如上线通知或者记录

            // 也可以做设备黑白名单
            boolean equipmentIsDisable = false;
            if(equipmentIsDisable) {
                ctx.channel().close();
            }

            ctx.channel().attr(NettyAttrKeys.INIT_ONLINE).set(true);

        }

        // 解析报文头
        Head head = MagicByte.pack(upLinkBytes, Head.class);

        // 设置设备编号到channel
        if(!ctx.channel().hasAttr(NettyAttrKeys.EQUIPMENT_SN)) {
            ctx.channel().attr(NettyAttrKeys.EQUIPMENT_SN).setIfAbsent(head.getEquipmentSn());
        }


        logger.info("received device uplink message: {}, {}, readIndex:{}, writerIndex:{}, cap: {}, refcnt: {}, rawData: {},", head.getEquipmentSn(), ctx.channel().remoteAddress(),
                in.readerIndex(), in.writerIndex(), in.capacity(), in.refCnt(), Base64.getEncoder().encodeToString(upLinkBytes));

        return upLinkBytes;
    }



}
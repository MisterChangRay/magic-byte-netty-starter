package com.github.ray.common.protocol;

import com.github.misterchangray.core.annotation.MagicClass;
import com.github.misterchangray.core.annotation.MagicField;


/**
 * 这里假设报文格式如下：
 * |报文头|报文长度|报文类型|设备编号|报文体|校验和
 */
@MagicClass
public class Head {
    // 字节头
    @MagicField(order = 1)
    private byte head;
    // 设备编号
    @MagicField(order = 3)
    private int equipmentSn;
    // 字节长度
    @MagicField(order = 5)
    private short len;
    // 命令类型
    @MagicField(order = 7)
    private byte cmd;


    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public short getLen() {
        return len;
    }

    public void setLen(short len) {
        this.len = len;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public int getEquipmentSn() {
        return equipmentSn;
    }

    public void setEquipmentSn(int equipmentSn) {
        this.equipmentSn = equipmentSn;
    }
}

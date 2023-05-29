package com.github.ray.common.protocol;

import com.github.misterchangray.core.annotation.MagicClass;
import com.github.misterchangray.core.annotation.MagicField;
import com.github.misterchangray.core.enums.TimestampFormatter;

import java.util.Date;

/**
 * 一条测试命令
 *
 * 把接受的数据原样返回
 */
@MagicClass
public class EchoCMD {
    // 报文头
    @MagicField(order = 1)
    private Head head;
    // 传输数据
    @MagicField(order = 3, size = 10)
    private String txt;
    // 时间戳
    @MagicField(order = 5, timestampFormat = TimestampFormatter.TO_TIMESTAMP_MILLIS)
    private Date  timestamp;
    // 校验和
    @MagicField(order = 7)
    private byte checkCode;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public byte getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(byte checkCode) {
        this.checkCode = checkCode;
    }
}

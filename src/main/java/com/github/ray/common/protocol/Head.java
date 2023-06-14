package com.github.ray.common.protocol;

import com.github.misterchangray.core.annotation.MagicClass;
import com.github.misterchangray.core.annotation.MagicField;

@MagicClass
public class Head {
    // 字节头
    @MagicField(order = 1)
    private byte head;
    // 字节长度
    @MagicField(order = 3)
    private short len;
    // 命令类型
    @MagicField(order = 5)
    private byte cmd;
}

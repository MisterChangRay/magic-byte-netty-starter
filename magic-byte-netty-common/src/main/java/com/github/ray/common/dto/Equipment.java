package com.github.ray.common.dto;

/**
 * 这是业务层的实体类
 *
 * 主要是提供给业务方使用
 *
 *
 */
public class Equipment {
    private String snId;
    // 其他业务字段
    private String timestamp;

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

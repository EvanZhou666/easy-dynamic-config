package com.easyconfig.storage;

import java.time.LocalDateTime;

public class ConfigItem {
    /**
     * id
     */
    Long id;
    /**
     * 配置标签
     */

    String cfTag;
    /**
     * 配置名称
     */
    String cfName;

    /**
     * 配置详情
     */
    String cfValue;

    /**
     * 配置描述
     */
    String cfDescription;

    /**
     * 创建时间
     */
    LocalDateTime cfCreateTime;

    /**
     * 修改时间
     */
    LocalDateTime cfUpdateTime;

    public ConfigItem() {
    }

    public ConfigItem(Long id, String cfTag, String cfName, String cfValue, String cfDescription, LocalDateTime cfCreateTime, LocalDateTime cfUpdateTime) {
        this.id = id;
        this.cfTag = cfTag;
        this.cfName = cfName;
        this.cfValue = cfValue;
        this.cfDescription = cfDescription;
        this.cfCreateTime = cfCreateTime;
        this.cfUpdateTime = cfUpdateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCfTag() {
        return cfTag;
    }

    public void setCfTag(String cfTag) {
        this.cfTag = cfTag;
    }

    public String getCfName() {
        return cfName;
    }

    public void setCfName(String cfName) {
        this.cfName = cfName;
    }

    public String getCfValue() {
        return cfValue;
    }

    public void setCfValue(String cfValue) {
        this.cfValue = cfValue;
    }

    public String getCfDescription() {
        return cfDescription;
    }

    public void setCfDescription(String cfDescription) {
        this.cfDescription = cfDescription;
    }

    public LocalDateTime getCfCreateTime() {
        return cfCreateTime;
    }

    public void setCfCreateTime(LocalDateTime cfCreateTime) {
        this.cfCreateTime = cfCreateTime;
    }

    public LocalDateTime getCfUpdateTime() {
        return cfUpdateTime;
    }

    public void setCfUpdateTime(LocalDateTime cfUpdateTime) {
        this.cfUpdateTime = cfUpdateTime;
    }
}

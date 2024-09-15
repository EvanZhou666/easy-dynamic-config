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

    String cf_tag;
    /**
     * 配置名称
     */
    String cf_name;

    /**
     * 配置详情
     */
    String cf_value;

    /**
     * 配置描述
     */
    String cf_description;

    /**
     * 创建时间
     */
    LocalDateTime cf_create_time;

    /**
     * 修改时间
     */
    LocalDateTime cf_update_time;

    public ConfigItem() {
    }

    public ConfigItem(Long id, String cf_tag, String cf_name, String cf_value, String cf_description, LocalDateTime cf_create_time, LocalDateTime cf_update_time) {
        this.id = id;
        this.cf_tag = cf_tag;
        this.cf_name = cf_name;
        this.cf_value = cf_value;
        this.cf_description = cf_description;
        this.cf_create_time = cf_create_time;
        this.cf_update_time = cf_update_time;
    }
}

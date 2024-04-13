package com.demo.Properties;

import com.alibaba.fastjson.JSONObject;

public class Deserializer_DemoProps extends DemoProps{

    // 方案一：
    // 1. 实现一个反序列化器Deserializer_DemoProps，（调用构造器方法实例化DemoProps），调用get方法为其赋值。
//    private DemoProps delegate;

    public Object deserialze(String configValue) {
        DemoProps demoProps = new DemoProps();

        // 查询数据库配置，得到描述信息。FieldName FieldType FieldValue
      /*  for (field: Fields) {
            if (field.name.equals("name")) {
                demoProps.setName(getValue());
            }
        }*/
        // 用FastJSON反序列化 岂不是更简单？？？ 不需要自己拼接GET/SET方法
        // 还有个问题，如何更新demoProps实例对象的值呢？
        return JSONObject.parseObject(configValue, DemoProps.class);
    }
}

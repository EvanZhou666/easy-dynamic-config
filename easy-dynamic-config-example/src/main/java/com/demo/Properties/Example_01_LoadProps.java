package com.demo.Properties;

import com.easyconfig.ECBuilders;

public class Example_01_LoadProps {

    public static void main(String[] args) {
        DemoProps demoProps = ECBuilders.kernel().build().getProps(DemoProps.class);
        System.out.println(demoProps);
        System.out.println(demoProps.getName());
        System.out.println(demoProps.getAge());
    }

}
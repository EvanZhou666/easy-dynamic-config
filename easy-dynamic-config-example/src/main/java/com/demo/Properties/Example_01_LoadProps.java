package com.demo.Properties;

import com.easyconfig.ECBuilders;
import com.easyconfig.Kernel;

public class Example_01_LoadProps {

    public static void main(String[] args) {
        Kernel kernel = ECBuilders.kernel().build();
        DemoConfig demoConfig = kernel.getProps(DemoConfig.class);
        System.out.println(demoConfig);
        DemoConfig demoConfig2 = kernel.getProps(DemoConfig.class);
        System.out.println(demoConfig2);
        System.out.println(demoConfig == demoConfig2);
    }

}
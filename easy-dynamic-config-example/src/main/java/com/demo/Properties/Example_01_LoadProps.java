package com.demo.Properties;

import com.easyconfig.ECBuilders;
import com.easyconfig.Kernel;

public class Example_01_LoadProps {

    public static void main(String[] args) {
        Kernel kernel = ECBuilders.kernel().build();
        DemoProps demoProps1 = kernel.getProps(DemoProps.class);
        System.out.println(demoProps1);
        DemoProps demoProps2 = kernel.getProps(DemoProps.class);
        System.out.println(demoProps2);
        System.out.println(demoProps1 == demoProps2);
    }

}
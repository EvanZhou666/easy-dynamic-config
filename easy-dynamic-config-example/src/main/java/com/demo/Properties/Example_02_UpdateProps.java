package com.demo.Properties;

import com.easyconfig.ECBuilders;
import com.easyconfig.Kernel;

public class Example_02_UpdateProps {

    public static void main(String[] args) throws InterruptedException {
        Kernel kernel = ECBuilders.kernel().build();
        DemoConfig demoConfig = kernel.getProps(DemoConfig.class);
        System.out.println(demoConfig);
//        Thread.sleep(15000L);
        // if you update database, execute sql: UPDATE ek.ek_dynamic_config t SET t.cf_value = '淡绿色' WHERE t.id = 4
        kernel.updateProperties(DemoConfig.class, "skus[0].color");
        DemoConfig demoConfig2 = kernel.getProps(DemoConfig.class);
        System.out.println(demoConfig2);
    }

}
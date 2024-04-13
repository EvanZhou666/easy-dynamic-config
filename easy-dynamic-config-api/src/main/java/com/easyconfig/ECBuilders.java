package com.easyconfig;

import com.easyconfig.builders.BuildersSingleton;
import com.easyconfig.builders.kernel.KernelBuilder;

public class ECBuilders {

    public static KernelBuilder kernel() {
        return BuildersSingleton.INST.getInstance(KernelBuilder.class);
    }

}

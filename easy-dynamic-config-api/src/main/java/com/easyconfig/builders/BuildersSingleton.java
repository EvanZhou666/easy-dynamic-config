package com.easyconfig.builders;

import com.easyconfig.builders.kernel.KernelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum BuildersSingleton {

    INST;

    // Fallback classes in case the META-INF/services directory is missing
    // Keep this list in alphabetical order by fallback variable name

    private static final String FALLBACK_KERNEL_BUILDER_CLASS =
            "com.easyconfig.builders.DefaultKernelBuilder";

    private final Map<Class<? extends ECBuilder<?>>, Supplier<? extends Buildable>>
            builders = new HashMap<>();

    BuildersSingleton() {
        try {
            // Keep this list in alphabetical order by fallback variable name

            registerBuilder(KernelBuilder.class, FALLBACK_KERNEL_BUILDER_CLASS);

        } catch (Throwable e) {
            Logger LOGGER = LoggerFactory.getLogger(BuildersSingleton.class);
            LOGGER.error("Failed to discover Semantic Kernel Builders", e);
            LOGGER.error(
                    "This is likely due to:\n\n"
                            + "- The Semantic Kernel implementation (typically provided by"
                            + " easy-dynamic-config-core) is not present on the classpath at runtime, ensure"
                            + " that this dependency is available at runtime. In maven this would be"
                            + " achieved by adding:\n"
                            + "\n"
                            + "        <dependency>\n"
                            + "            <groupId>com.easyconfig</groupId>\n"
                            + "            <artifactId>easy-dynamic-config-core</artifactId>\n"
                            + "            <version>${skversion}</version>\n"
                            + "            <scope>runtime</scope>\n"
                            + "        </dependency>\n\n"
                            + "- The META-INF/services files that define the service loading have been"
                            + " filtered out and are not present within the running application\n\n"
                            + "- The class names have been changed (for instance shaded) preventing"
                            + " discovering the classes");

            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private <U extends Buildable, T extends ECBuilder<U>> void registerBuilder(
            Class<T> clazz, String fallbackClassName) {
        builders.put(
                clazz,
                (Supplier<? extends Buildable>)
                        ServiceLoadUtil.findServiceLoader(clazz, fallbackClassName));
    }

    @SuppressWarnings("unchecked")
    public <U extends Buildable, T extends ECBuilder<U>> T getInstance(Class<T> clazz) {
        return (T) builders.get(clazz).get();
    }

}

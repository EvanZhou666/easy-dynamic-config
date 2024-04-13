package com.easyconfig.builders;

import com.easyconfig.ECBuilders;

/**
 * Interface for all builders that create Buildable objects. Typically, these builders can be
 * obtained via {@link ECBuilders}
 *
 * @param <T>
 */
public interface ECBuilder <T extends Buildable>{

    /**
     * Build the object.
     *
     * @return a constructed object.
     */
    T build();

}

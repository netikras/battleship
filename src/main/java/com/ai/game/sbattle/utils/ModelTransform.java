package com.ai.game.sbattle.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/**
 * Created by netikras on 17.5.15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, LOCAL_VARIABLE})
public @interface ModelTransform {

    /**
     * Name of the DTO's field this value should be reflected on
     * @return
     */
    String dtoFieldName();

    /**
     * If nulls should be allowed in dto
     * @return
     */
    boolean dtoAllowNull() default true;

    /**
     * If DTO is allowed to change this value using automatic model updater
     * @return
     */
    boolean dtoUpdatable() default true;

    /**
     * Will extract value of model's field provided as this value<br/>
     * and assign it to DTO's respective field
     * @return
     */
    String dtoValueExtractField() default "";

}
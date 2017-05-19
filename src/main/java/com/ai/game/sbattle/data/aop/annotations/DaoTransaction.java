package com.ai.game.sbattle.data.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by netikras on 17.3.11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DaoTransaction {

    String daoBeanName() default "";

//    Class<? extends GenericDao> daoClass();



}

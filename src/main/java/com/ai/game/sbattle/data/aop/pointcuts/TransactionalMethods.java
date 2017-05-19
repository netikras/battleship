package com.ai.game.sbattle.data.aop.pointcuts;

import com.ai.game.sbattle.data.aop.annotations.DaoTransaction;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by netikras on 17.3.11.
 */

@Component
@Aspect
public class TransactionalMethods {

//    @Pointcut("execution(@com.unifier.coreservices.data.aop.annotations.DaoTransaction * *(..))")
    @Pointcut("execution(public * com.ai.game.sbattle..*.*(..)) && @annotation(daoTransaction)")
    public void transactionalMethods(final DaoTransaction daoTransaction) {

    }

    @Pointcut("execution(public * com.ai.game.sbattle.data.dao.GameDao+.*(..))")
    public void daoMethods() {

    }


}

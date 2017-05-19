package com.ai.game.sbattle.data.aop.advices;

import com.ai.game.sbattle.data.aop.annotations.DaoTransaction;
import com.ai.game.sbattle.data.dao.GameDao;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by netikras on 17.3.11.
 */
@Aspect
@Component
public class TransactionalMethodsAdvice implements ApplicationContextAware {

    @Resource
    private ApplicationContext context;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("Duplicates")
    @Around("com.ai.game.sbattle.data.aop.pointcuts.TransactionalMethods.transactionalMethods(daoTransaction)")
    public Object performTransaction(ProceedingJoinPoint joinPoint, DaoTransaction daoTransaction) {

        Object result = null;
        GameDao dao = null;
        Session session = null;
        Transaction transaction = null;

        try {

            logger.debug("Preparing transaction");

            if (!daoTransaction.daoBeanName().isEmpty()) {
                dao = (GameDao) context.getBean(daoTransaction.daoBeanName());
            } else {
                dao = (GameDao) joinPoint.getTarget();
            }

//            session = dao.getCurrentSession();
            session = dao.getSessionFactory().openSession();

            try {
                logger.debug("Starting transaction");
                transaction = session.beginTransaction();
                dao.setCurrentSession(session);
                result = joinPoint.proceed();
                logger.debug("Committing transaction");
                transaction.commit();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                logger.error("{}", throwable);
                if (transaction != null) {
                    logger.debug("Rolling back transaction");
                    transaction.rollback();
                }
//                throw throwable;
            } finally {
                session.close();
            }
        } catch (Exception e) {
            // ClassCastExc, NPE, etc.
            logger.error("{}", e);
        } finally {
            if (dao != null && session != null) {
                dao.setCurrentSession(null);
            }
            return result;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}

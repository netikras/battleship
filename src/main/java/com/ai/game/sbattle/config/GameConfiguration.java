package com.ai.game.sbattle.config;

import com.ai.game.sbattle.data.model.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;

import java.io.IOException;

/**
 * Created by netikras on 17.5.15.
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.ai.game.sbattle"})
public class GameConfiguration {



    @Bean(name = "sessionFactory")
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public SessionFactory getSessionFactory() throws IOException {
        SessionFactory factory = null;

        org.hibernate.cfg.Configuration dbConfig = new org.hibernate.cfg.Configuration();


        dbConfig.addProperties(PropertiesLoader.loadDbProperties());

        dbConfig.addAnnotatedClass(Coordinates.class);
        dbConfig.addAnnotatedClass(GameBoard.class);
        dbConfig.addAnnotatedClass(GameMatch.class);
        dbConfig.addAnnotatedClass(Player.class);
        dbConfig.addAnnotatedClass(Ship.class);
        dbConfig.addAnnotatedClass(Square.class);

        factory = dbConfig.buildSessionFactory();
        System.out.println("Creating bean: sessionFactory: " + factory);
        return factory;
    }


}

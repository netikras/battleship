package com.ai.game.sbattle.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by netikras on 17.5.15.
 */
public class Initializer implements WebApplicationInitializer {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String CONTEXT_CONFIG_LOCATION = GameConfiguration.class.getPackage().getName();
    public static final String[] MAPPING_URLS = new String[]{
            "/game/*",
            "/play/"
    };


    ServletRegistration.Dynamic dispatcher = null;
    ServletRegistration dispatcherServlet = null;
    WebApplicationContext webAppContext = null;
    ServletContext servletContext = null;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        logger.info("Beginning component initialization");
        logger.debug("Context config location: {}", CONTEXT_CONFIG_LOCATION);

        this.servletContext = servletContext;


        dispatcherServlet = servletContext.getServletRegistration("DispatcherServlet");


        webAppContext = getWebApplicationContext();

        dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(webAppContext));
        if (dispatcher != null) {
            dispatcher.setLoadOnStartup(1);
            addMappings(dispatcher);
        }


    }

    private AnnotationConfigWebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONTEXT_CONFIG_LOCATION);
        return context;
    }

    private void addMappings(ServletRegistration.Dynamic dispatcher) {
        for (String mapping : MAPPING_URLS) {
            logger.debug("Adding URL mapping: [{}]", mapping);
            dispatcher.addMapping(mapping);
        }
    }


}

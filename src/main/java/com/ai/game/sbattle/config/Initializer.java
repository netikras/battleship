package com.ai.game.sbattle.config;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.sql.SQLException;

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
    Server dbServer = null;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

//        try {
//            startDatabase();
//            servletContext.addListener(new ServletShutdownListener());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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



    private void startDatabase() throws SQLException, ClassNotFoundException {
        logger.info("Starting up database server");
        dbServer = Server.createWebServer().start();
        dbServer = Server.createTcpServer("-tcpAllowOthers", "-web", "-browser").start();
        logger.info("Database server started");
        Class.forName("org.h2.Driver");

//        Runtime.getRuntime().addShutdownHook(new Thread(dbServer::stop));

    }


    private class ServletShutdownListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent sce) {

        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
            logger.info("Stopping database server");
            dbServer.stop();
        }

    }

}

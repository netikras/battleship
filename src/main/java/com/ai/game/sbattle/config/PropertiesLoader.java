package com.ai.game.sbattle.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by netikras on 17.5.19.
 */
public class PropertiesLoader {

    private static final String propName = "dbSettings";

    private static final Properties defaultDbProps;
    static {
        defaultDbProps = new Properties();
        defaultDbProps.put("hibernate.connection.url", "jdbc:h2:mem:sbattle_db;TRACE_LEVEL_FIle=4;TRACE_LEVEL_SYSTEM_OUT=4");
        defaultDbProps.put("hibernate.connection.url", "jdbc:h2:tcp://localhost/~/received/sbattle_db;TRACE_LEVEL_FIle=4;TRACE_LEVEL_SYSTEM_OUT=4");
//        defaultDbProps.put("hibernate.connection.url", "jdbc:h2:mem:sbattle_db");
        defaultDbProps.put("hibernate.connection.username", "sbattle");
        defaultDbProps.put("hibernate.connection.password", "sbattle");
        defaultDbProps.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        defaultDbProps.put("hibernate.connection.driver_class", "org.h2.Driver");
        defaultDbProps.put("hibernate.hbm2ddl.auto", "create-drop");
        defaultDbProps.put("hibernate.connection.autocommit", "true");
        defaultDbProps.put("hibernate.hbm2ddl.import_files", "/dbinit_preload_coordinates.sql");
        defaultDbProps.put("hibernate.max_fetch_depth", "5");
    }


    public static final Map<String, String> DB_PROPERTIES_NAMES;
    static {
        DB_PROPERTIES_NAMES = new HashMap<>();
        DB_PROPERTIES_NAMES.put("db_url",      "hibernate.connection.url");
        DB_PROPERTIES_NAMES.put("db_username", "hibernate.connection.username");
        DB_PROPERTIES_NAMES.put("db_password", "hibernate.connection.password");
        DB_PROPERTIES_NAMES.put("db_dialect",  "hibernate.dialect");
        DB_PROPERTIES_NAMES.put("db_driver",   "hibernate.connection.driver_class");
    }



    public static Properties loadDbProperties() throws IOException {
        Object object = System.getProperty(propName);
        if (object == null) {
            return defaultDbProps;
        }

        Properties dbProps = new Properties();
        dbProps.load(new FileInputStream((String) object));

        for (Map.Entry entry : dbProps.entrySet()) {
            String key = (String) entry.getKey();
            String hibernateValue = DB_PROPERTIES_NAMES.get(key);

            if (hibernateValue != null) {
                dbProps.put(hibernateValue, entry.getValue());
            }
        }

        return dbProps;
    }

}

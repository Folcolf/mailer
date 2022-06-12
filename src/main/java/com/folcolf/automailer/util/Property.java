package com.folcolf.automailer.util;

import java.util.Properties;

public class Property {

    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(Property.class.getClassLoader().getResourceAsStream(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

}

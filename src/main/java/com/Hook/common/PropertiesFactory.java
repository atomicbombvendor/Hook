package com.Hook.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ZZ on 2017/8/17.
 */
public class PropertiesFactory {
    private static Properties properties = new Properties();

    public  static Properties getInstance(String fileName) {
        try {
            InputStream is = PropertiesFactory.class.getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException();
            }
            properties.load(is);
            is.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not find" + e);
        } catch (IOException e) {
            System.out.println("IO Exception" + e);
        }
        return properties;
    }

    public static String getValue(String key) {
        if (properties != null) {
            return properties.getProperty(key);
        } else {
            return null;
        }
    }
}

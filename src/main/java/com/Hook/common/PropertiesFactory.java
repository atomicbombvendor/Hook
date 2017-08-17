package com.Hook.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ZZ on 2017/8/17.
 */
public class PropertiesFactory {
    private static volatile Properties properties;

    //TODO read multi files
    public synchronized static Properties getInstance() {
        if (properties == null) {

            synchronized (Properties.class) {
                if (properties == null) {
                    try {
                        properties = new Properties();
                        InputStream is = PropertiesFactory.class.getClassLoader().getResourceAsStream("keycode.properties");
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
                } else {
                    return properties;
                }
            }
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

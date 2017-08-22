package com.Hook.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZZ on 2017/8/17.
 */
public class KeyBoard {

    private static String fileName = "keycode.properties";
    /**
     * Get mapping between vkCode(key board button code) and keyEvent(KeyBoard button)
     * @return Map
     */
    public static Map<Integer, String> vkCodeToKeyEvent(){
        Map<Integer, String> vCode = new HashMap<>();
        PropertiesFactory.getInstance(fileName);
        int endIndex = 300;
        int j = 0;
        for(int i=0; i<endIndex; i++){
            String value = PropertiesFactory.getValue(String.valueOf(i));
            if(value!=null){
                vCode.put(i, value);
            }
        }
        return vCode;
    }

}

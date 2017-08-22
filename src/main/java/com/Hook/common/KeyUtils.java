package com.Hook.common;

/**
 * Created by eli9 on 8/22/2017.
 */
public class KeyUtils {
    private boolean[] on_off = {true, true, true};
    private volatile static KeyUtils key;

    private KeyUtils(){}

    public static KeyUtils getInstance(){
        if(key == null){
            synchronized (KeyUtils.class){
                if(key == null){
                    key = new KeyUtils();
                }
            }
        }
        return key;
    }

    public synchronized void setKey(int index, boolean flag){
        on_off[index] = flag;
    }

    public synchronized boolean[] getOn_off(){
        return on_off;
    }

    public void print(){
        for (boolean b: on_off) {
            System.out.print(" value: "+b);
        }
        System.out.println("\n");
    }

}

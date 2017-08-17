package com.Hook.common;

import com.sun.jna.examples.win32.User32;

/**
 * Created by ZZ on 2017/8/17.
 */
public class CommonFunction {

    public static void printMsg(User32 lib, User32.MSG msg){
        int result;
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
    }
}

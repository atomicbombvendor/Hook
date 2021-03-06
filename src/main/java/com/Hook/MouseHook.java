package com.Hook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Hook.common.CommonFunction;
import com.Hook.common.KeyUtils;
import com.sun.jna.Structure;
import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.User32.HHOOK;
import com.sun.jna.examples.win32.User32.MSG;
import com.sun.jna.examples.win32.W32API.HMODULE;
import com.sun.jna.examples.win32.W32API.LRESULT;
import com.sun.jna.examples.win32.W32API.WPARAM;
import com.sun.jna.examples.win32.User32.HOOKPROC;

/**
 * 实现监听鼠标的代码
 * http://blog.csdn.net/qq_27099139/article/details/73530614
 * Created by ZZ on 2017/8/17.
 */
public class MouseHook implements Runnable {
    public static String name = "MouseHook";

    public static final int WM_MOUSEMOVE = 512;
    private static HHOOK hhk;
    private static LowLevelMouseProc mouseHook;
    final static User32 lib = User32.INSTANCE;
    private volatile boolean[] on_off = null;

    public MouseHook(boolean[] on_off) {
        this.on_off = on_off;
    }

    public interface LowLevelMouseProc extends HOOKPROC {
        LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
    }

    public static class MOUSEHOOKSTRUCT extends Structure {
        public static class ByReference extends MOUSEHOOKSTRUCT implements
                Structure.ByReference {
        }

        public User32.POINT pt;
        public int wHitTestCode;
        public User32.ULONG_PTR dwExtraInfo;
    }

    @Override
    public void run() {
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        mouseHook = (LowLevelMouseProc) (nCode, wParam, info) -> {
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName = df1.format(new Date());
            String time = df2.format(new Date());
            BufferedWriter bw1 = null;
            BufferedWriter bw2 = null;
            try {
                File mouse = FileUtils.createFile(".//log//" + fileName + "_Mouse.txt");
                File common = FileUtils.createFile(".//log//" + fileName + "_Common.txt");
                bw1 = new BufferedWriter(new FileWriter(mouse, true));
                bw2 = new BufferedWriter(new FileWriter(common, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!KeyUtils.getInstance().getOn_off()[2]) {
                System.out.println("End mouse");
                System.exit(0);
            }
            if (nCode >= 0) {
                switch (wParam.intValue()) {
                    case MouseHook.WM_MOUSEMOVE:
                        try {
                            bw1.write(time + "  ####  " + "x=" + info.pt.x
                                    + " y=" + info.pt.y + "\r\n");
                            bw2.write(time + "  ####  " + "x=" + info.pt.x
                                    + " y=" + info.pt.y + "\r\n");
                            bw1.flush();
                            bw2.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
            return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
        };
        hhk = lib.SetWindowsHookEx(User32.WH_MOUSE_LL, mouseHook, hMod, 0);
        MSG msg = new MSG();
        CommonFunction.printMsg(lib, msg);
        lib.UnhookWindowsHookEx(hhk);
    }

}
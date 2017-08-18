package com.Hook;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Console;
import java.util.Scanner;

/**
 * 开启上述线程的类
 * http://blog.csdn.net/qq_27099139/article/details/73530614
 * Created by ZZ on 2017/8/17.
 */
public class Monitor {
    private static Thread processInfo;
    private static Thread keyboardHook;
    private static Thread mouseHook;

    public Monitor() {
        boolean[] on_off = {true};
        //create three thread
        processInfo = new Thread(new ProcessInfo(on_off));
        keyboardHook = new Thread(new KeyboardHook(on_off));
        mouseHook = new Thread(new MouseHook(on_off));
    }

    public void start() {
        //create three thread
        (processInfo).start();
        (keyboardHook).start();
        (mouseHook).start();
        final TrayIcon trayIcon;

        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(".//lib//monitor.png");

            ActionListener exitListener = e -> {
                System.out.println("Exiting...");
                System.exit(0);
            };

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "monitor", popup);

            ActionListener actionListener = e -> trayIcon.displayMessage("Action Event",
                    "An Action Event Has Been Performance!",
                    TrayIcon.MessageType.INFO);

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);

            try {
                tray.add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void interruptThread() {
        if (!processInfo.isInterrupted()) {
            processInfo.interrupt();
        }
        if (!keyboardHook.isInterrupted()) {
            keyboardHook.interrupt();
        }
        if (!mouseHook.isInterrupted()) {
            mouseHook.interrupt();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome! Program has started!");
        //System.out.println("Input 0 to End!");
        Monitor monitor = new Monitor();
        monitor.start();
//        Scanner sc = new Scanner(System.in);
//        String st = sc.nextLine();//获取输入信息
//        if (st.equals("0"))
//            monitor.interruptThread();
//        System.out.println("End!");
    }
}

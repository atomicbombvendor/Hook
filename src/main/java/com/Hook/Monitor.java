package com.Hook;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
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
    private static volatile boolean []on_off={true,true,true};
    public Monitor() {
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

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome! Program has started!");
        System.out.print("Input 'Y' to start monitor tool>>");
        Scanner s = new Scanner(System.in);
        String value = s.nextLine();
        while(value != null) {
            if (value.equalsIgnoreCase("Y")) {
                Monitor monitor = new Monitor();
                monitor.start();
                System.out.println("Start finished!");
                break;
            }else {
                System.out.print("Input 'Y' to start monitor tool>>");
                value = s.nextLine();
            }
        }

        System.out.println("Input any key to exit....");
        value = s.nextLine();
        while(value!=null){
            return;
        }
    }
}

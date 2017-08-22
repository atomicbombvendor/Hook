package com.Hook;

import com.Hook.common.KeyUtils;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
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
        //(processInfo).start();
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

    public synchronized void setOnOff(int key){
        switch (key){
            case 0: on_off[0] = false; break;
            case 1: on_off[1] = false; break;
            case 2: on_off[2] = false; break;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome! Program has started!");
        System.out.println("Input 'Y' to start monitor tool");
        Monitor monitor = new Monitor();
        monitor.start();
        System.out.println("Input p to end process");
        System.out.println("Input k to end keyboard");
        System.out.println("Input m to end mouse");
        System.out.println("Input s to stop monitor");
        Scanner s = new Scanner(System.in);
        String value = s.nextLine();
        //value = "p";
        while(value != null){
            System.out.println("Input is:" + value);
            if (value.equalsIgnoreCase("k")){
                System.out.println("Start end keyboard");
                KeyUtils.getInstance().setKey(1,false);
            }
            if (value.equalsIgnoreCase("m")){
                System.out.println("Start end mouse");
                KeyUtils.getInstance().setKey(2,false);
            }
            if (value.equalsIgnoreCase("s")){
                return;
            }
            System.out.println("Input>> ");
            value = s.nextLine();
        }
    }
}

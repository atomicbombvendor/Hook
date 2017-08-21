package com.Hook;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

/**
 * 开启上述线程的类
 * http://blog.csdn.net/qq_27099139/article/details/73530614
 * Created by ZZ on 2017/8/17.
 */
public class Monitor {
    private static ProcessInfo processInfo;
    private static KeyboardHook keyboardHook;
    private static MouseHook mouseHook;

    public Monitor() {
        boolean[] on_off = {true};
        //create three thread
        processInfo = new ProcessInfo(on_off);
        keyboardHook = new KeyboardHook(on_off);
        mouseHook = new MouseHook(on_off);
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

    public void exitProcessInfo(){
        boolean[] on_off = {false};
        processInfo.setOnOff(on_off);
    }

    public void exitKeyboard(){
        boolean[] on_off = {false};
        processInfo.setOnOff(on_off);
    }

    public void exitMouseHook(){
        boolean[] on_off = {false};
        processInfo.setOnOff(on_off);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome! Program has started!");
        System.out.println("Input 'Y' to start monitor tool");
        Monitor monitor = new Monitor();
        monitor.start();
        System.out.println("Enter 1 to end mouse hook");
        Scanner sc = new Scanner(System.in);
        System.out.println("input>> ");
        String read = sc.nextLine();
        if(read.equalsIgnoreCase("1")){
            System.out.println(read);
            monitor.exitProcessInfo();
        }
        System.out.println(read);
    }
}

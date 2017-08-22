package com.Hook;


import com.Hook.common.KeyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 最后是获取进程信息的代码
 * http://blog.csdn.net/qq_27099139/article/details/73530614
 * Created by ZZ on 2017/8/17.
 */
public class ProcessInfo implements Runnable{
    public static String name = "ProcessInfo";
    private volatile boolean [] on_off = null;

    public ProcessInfo(boolean [] on_off){
        this.on_off = on_off;
    }

    @Override
    public void run() {
        BufferedReader input = null;
        Process process;
        BufferedWriter bw=null;
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName;
        String time;
        try {
            System.out.println("111"+KeyUtils.getInstance().getOn_off()[0]);
            while(KeyUtils.getInstance().getOn_off()[0]){//flag
                System.out.println("222"+KeyUtils.getInstance().getOn_off()[0]);
                fileName=df1.format(new Date());
                time=df2.format(new Date());
                File processInfo = FileUtils.createFile(".//log//"+fileName+"_ProcessInfo.txt");
                bw=new BufferedWriter(new FileWriter(processInfo,true));
                Thread.sleep(60000);
                process = Runtime.getRuntime().exec("cmd.exe   /c   tasklist");
                input =new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                String line;
                int i=0;
                input.readLine();
                input.readLine();
                input.readLine();
                while ((line = input.readLine()) != null) {
                    bw.write(time+"  ####  "+line+"\r\n");
                    bw.flush();
                    i++;
                }
            }
            System.out.println("333"+KeyUtils.getInstance().getOn_off()[0]);
            if(!KeyUtils.getInstance().getOn_off()[0]){
                System.out.println("process end");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                bw.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
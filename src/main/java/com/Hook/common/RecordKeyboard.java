package com.Hook.common;

import com.Hook.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eli9 on 8/21/2017.
 */
public class RecordKeyboard {
    private static String fileName = "keyboardCount.properties";
    private static volatile RecordKeyboard instance;
    private static volatile Map<Integer, Integer> keyboardCount;
    private static Map<Integer, String> vCode = KeyBoard.vkCodeToKeyEvent();
    private static int pressCount = 0;
    private static int flagTime = 10;
    private RecordKeyboard(){}

    public static RecordKeyboard getInstance(){
        if(instance == null){
            synchronized (RecordKeyboard.class){
                if(instance == null){
                    instance = new RecordKeyboard();
                    keyboardCount = initCountMap();
                }
            }
        }
        return instance;
    }

    /**
     * Get map about vkCode(key board button code) and count
     * @return Map
     */
    private static Map<Integer, Integer> initCountMap(){
        Map<Integer, Integer> vCode = new HashMap<>();
        PropertiesFactory.getInstance(fileName);
        int endIndex = 300;
        for(int i=0; i<endIndex; i++){
            String v = PropertiesFactory.getValue(String.valueOf(i));
            if(v!=null){
                Integer value = Integer.valueOf(v);
                vCode.put(i, value);
            }
        }
        return vCode;
    }

    public void keyDown(Integer keyInfo){
        Integer count = 0;
        if(keyboardCount.containsKey(keyInfo)){
            count = keyboardCount.get(keyInfo);
        }
        //(++count)/2 because when key pressed, it have two signal
        keyboardCount.put(keyInfo, (++count));
        ++pressCount;
        //when key was pressed 200 times, start to refresh file.
        if((pressCount>=flagTime) && (pressCount%flagTime==0)) {
            write();
        }
    }

    /**
     * if program is restart, it will count the time that key was pressed.
     * And file will be refresh from zero.
     */
    private void write(){
        FileWriter writer = null;
        try {
            String content = getContent();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            String fileName=df1.format(new Date());
            df1.format(new Date());
            String filePath = ".//log//"+fileName+"_KeyCount.txt";
            File countFile = FileUtils.createFile(filePath);
            writer = new FileWriter(countFile, false);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(content);
            bw.flush();//need flush to clean buffer
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getContent(){
        StringBuilder sb = new StringBuilder(String.format("Keyboard has been pressed %d times\r\n",
                ((pressCount%2>0)?(pressCount/2)+1:(pressCount/2))))
                .append("The every key of the keyboard is pressed\n ");
        keyboardCount.forEach((k,v) ->{
            String keyName = vCode.get(k);
            sb.append(String.format("%s(%d) -> %d ", keyName, k, ((v%2>0)?(v/2)+1:(v/2)))).append("\r\n");
        });
        return sb.toString();
    }

    public Integer getValue(Integer key){
        return keyboardCount.get(key);
    }
}

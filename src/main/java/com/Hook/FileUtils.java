package com.Hook;

import java.io.File;
import java.io.IOException;

/**
 * Created by ZZ on 2017/8/17.
 */
public class FileUtils {

    public static File createFile(String path){
        File file = null;
        try {
            file = new File(path);
            File parent = file.getParentFile();
            if(!parent.exists()){
                parent.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Create file exception\n"+e);
        }
        return file;
    }
}

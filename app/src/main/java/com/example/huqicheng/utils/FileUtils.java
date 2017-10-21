package com.example.huqicheng.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by huqicheng on 2017/10/20.
 */

public class FileUtils {
    public static String readFromFile(String sFileName) throws IOException {

        StringBuffer msg = new StringBuffer();

        File file = new File("mnt/sdcard/"+sFileName);
        if(file.exists()){
            Log.d("debug: ", sFileName+" exists");

        }else{
            Log.d("debug: ", sFileName+" not exists");

            return "";
        }


        FileReader r = new FileReader(file);
        BufferedReader br = new BufferedReader(r);


        String s;

        while ((s = br.readLine()) != null) {
            msg = msg.append(s+'\n'); // add \n to the last index of one line
        }

        return msg.toString();

    }

    public static void writeToFile(String sFileName,String content,Context context) throws IOException {

//        FileOutputStream fos = new FileOutputStream(new File("/mnt/sdcard/"+sFileName));
//        fos.write(content.getBytes());
//        fos.close();

        Log.d("debug: ", "write "+content + "to "+sFileName);
        String myPath="mnt/sdcard/"+ sFileName;

        File myFile = new File(myPath);
        if(myFile.exists()){

            Log.d("debug: ", sFileName+" exists [out]");

        }else{
            Log.d("debug: ", sFileName+" creates [out]");
            myFile.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(myFile);
        outputStream.write(content.getBytes());

        outputStream.close();


    }
}

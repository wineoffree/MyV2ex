package com.example.administrator.v2exapp.save;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/7/8.
 */
public class SaveToFile {

    FileOutputStream out=null;
    String filepath;
    Bitmap bitmap;
    String content;
    String configure;
    int index;
    String imgUrl;
    public SaveToFile(int index) {
        this.index=index;

        filepath =Environment.getExternalStorageDirectory().getAbsolutePath()+"/myv2ex";
        makeRootDirectory(filepath);
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;

    }
    public void setpath(String imgUrl){

        this.imgUrl=imgUrl;
    }
    public void setContent(String content){

        this.content=content;
    }
    public void setConfigure(String configure){

        this.configure=configure;
    }
    // 生成主文件夹
    public  void makeRootDirectory(String filepath) {
        File files = null;
        try {
            files = new File(filepath);
            if (!files.exists()) {
                files.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        };

    }
    public void saveBitmap() {
        File file;
        file = new File(filepath+"/"+imgUrl+".png");
        try {
            if(!file.createNewFile()) {
                //file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("File already exists");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        String pa="yes";
        try {
            out = new FileOutputStream(file);
            pa=file.getAbsoluteFile().toString();
        }
        catch (Exception e){e.printStackTrace();}

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        //Log.d("imama",filepath.toString()+"/ima"+ String.valueOf(position)+".png");
        try {
            out.flush();
            out.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void  saveContent(){

        File file;

        file = new File(filepath+"/save"+String.valueOf(index)+"content.txt");
        BufferedWriter writer=null;
        try {
            if(!file.createNewFile()) {
                //file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("File already exists");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        }
        catch (Exception e){e.printStackTrace();}
        try {
            out.flush();
            out.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void saveConfigure(){

        File file;

        file = new File(filepath+"/configure.txt");
        BufferedWriter writer=null;

        try {

            if(!file.createNewFile()) {
                //file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("File already exists");

            }

        } catch (IOException ex) {
            System.out.println(ex);
        }


        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(configure.getBytes());
        }
        catch (Exception e){e.printStackTrace();}




        try {
            out.flush();
            out.close();
        }
        catch (Exception e){e.printStackTrace();}

    }
}

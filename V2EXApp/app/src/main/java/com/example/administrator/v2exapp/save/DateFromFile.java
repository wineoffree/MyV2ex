package com.example.administrator.v2exapp.save;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
public class DateFromFile {
    String filepath =Environment.getExternalStorageDirectory().getAbsolutePath()+"/myv2ex";
    List<Map<String, String>> list;
    int index;
    String imaUrl;
    Bitmap bitmap;
    String configure;
    String []strings={"全部","创意","好玩","apple","酷工作","交易","城市","问与答","最热","技术","R2",};
    public void setIndex(int index){
        this.index=index;
    }
    public void setImaUrl(String imaUrl){
        this.imaUrl=imaUrl;
    }
    public   void getList( List<Map<String,String>> list) {
        Log.d("bababa",String.valueOf(index));
        String resultStr = null;
        try {
            File filecontent = new File(filepath+"/save"+String.valueOf(index)+"content.txt");
            FileInputStream fileInputStream = new FileInputStream(filecontent);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            resultStr = new String(b);
            if(fileInputStream != null){
                fileInputStream.close();
            }
            JSONArray jsonArray=new JSONArray(resultStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String ,String> map = new HashMap<String, String>();
                map.put("newUrl",jsonObject2.getString("newUrl"));
                map.put("content",jsonObject2.getString("content"));
                map.put("name",jsonObject2.getString("name"));
                map.put("type",jsonObject2.getString("type"));
                map.put("time",jsonObject2.getString("time") );
                map.put("showId",jsonObject2.getString("showId"));
                map.put("imaUrl",jsonObject2.getString("imaUrl"));
                list.add(map);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    // post方法访问服务器，返回集合数据
    public  List<Map<String,String>> getRequestList(){

        List<Map<String,String>> list = new ArrayList<Map<String ,String>>();

        getList(list);

        return list;

    }

    public Bitmap getBitmap(){
        bitmap=BitmapFactory.decodeFile(filepath+"/"+imaUrl+".png");
        return bitmap;
    }

    public void getConfigure( List<Map<String,String>> list){
        String resultStr = null;
        try {
            File filecontent = new File(filepath + "/save" + "Configure.txt");
            if(filecontent.exists()){
            FileInputStream fileInputStream = new FileInputStream(filecontent);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            resultStr = new String(b);
        if(fileInputStream != null){
            fileInputStream.close();
        }
        JSONArray jsonArray=new JSONArray(resultStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            Map<String ,String> map = new HashMap<String, String>();
            map.put("tab",jsonObject2.getString("tab"));
            map.put("value",jsonObject2.getString("value"));
            list.add(map);
        }}
            else {
                for (int i = 0; i < 11; i++) {
                    Map<String ,String> map = new HashMap<String, String>();
                    map.put("tab",strings[i]);
                    map.put("value",String.valueOf(i));
                    list.add(map);
            }
            }
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }

    public  List<Map<String,String>> getRequestConfigureList(){
        List<Map<String,String>> list = new ArrayList<Map<String ,String>>();
        getConfigure(list);
        return list;
    }
}

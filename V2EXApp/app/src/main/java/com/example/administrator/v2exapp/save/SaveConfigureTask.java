package com.example.administrator.v2exapp.save;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/22.
 */
public class SaveConfigureTask extends AsyncTask<String, Integer, List<Map<String,String>>> {
    String configure;
    Handler handler;
    public SaveConfigureTask(String Configure, Handler handler){
        this.handler=handler;
        this.configure=Configure;
    }
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Map<String, String>> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        Message message = Message.obtain();
        handler.sendMessage(message);
    }

    @Override
    protected List<Map<String,String>> doInBackground(String... params) {
        List<Map<String,String>> list=null;
        //存储到文件
        SaveToFile saveToFile=new SaveToFile();
        saveToFile.setConfigure(configure);
        saveToFile.saveConfigure();
        return list;
    }
}

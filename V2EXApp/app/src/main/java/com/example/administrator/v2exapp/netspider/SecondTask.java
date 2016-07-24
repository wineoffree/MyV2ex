package com.example.administrator.v2exapp.netspider;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.v2exapp.R;
import com.example.administrator.v2exapp.downloadimg.DownImageTask;
import com.example.administrator.v2exapp.save.CacheImage;
import com.example.administrator.v2exapp.save.DateFromFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 * 对第二层爬取
 */
public class SecondTask extends AsyncTask<String, Integer, List<Map<String,String>>> {
    ProgressDialog progressDialog;
    LinearLayout linearLayout;
    Context context;
    int i;
    String url;
    String topicContent;
    TextView textView;
    DateFromFile dateFromFile;
    int index;
    String imaUrl;
    ImageView imageView;
    Bitmap bitmap;
    ArrayList<View>views=new ArrayList<View>();
    public SecondTask(ProgressDialog progressDialog ,Context context, String url, TextView textView,LinearLayout linearLayout,int index,String imaUrl,ImageView imageView){
        this.progressDialog=progressDialog;
        this.context=context;
        this.url=url;
        this.textView=textView;
        this.linearLayout=linearLayout;
        this.index=index;
        this.imaUrl=imaUrl;
        this.imageView=imageView;
    }
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<Map<String, String>> result) {
        // TODO Auto-generated method stub

        super.onPostExecute(result);
        imageView.setImageBitmap(bitmap);
        Log.d("hahaha","show:615156");
        textView.setText(Html.fromHtml(topicContent));
        LinearLayout layout = new LinearLayout(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        for(int i=0;i<result.size();i++) {
            View view = inflater.inflate(R.layout.layout_item, null);
            TextView content = (TextView)(view.findViewById(R.id.content));
            content.setText(result.get(i).get("content").toString());
            TextView time = (TextView)(view.findViewById(R.id.time));
            time.setText(result.get(i).get("time").toString());
            TextView name = (TextView)(view.findViewById(R.id.name));
            name.setText(result.get(i).get("name").toString());
            ImageView img = (ImageView)(view.findViewById(R.id.ima));
            new DownImageTask(img,false,0,0).execute(result.get(i).get("ima"));
            linearLayout.addView(view);
        }

        progressDialog.dismiss();
    }

    @Override
    protected List<Map<String,String>> doInBackground(String... params) {
        //通过内存找
        Bitmap abitmap = CacheImage.getBitmap(imaUrl.replaceAll("/","").replace(".png?m=","").replace("_",""));
        if(abitmap!=null)
            this.bitmap=abitmap;
        else {//通过文件找
            this.bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myv2ex/"+imaUrl.replaceAll("/","").replace(".png?m=","").replace("_","")+".png");
        }
        List<Map<String,String>> list=null;
        DrawDate drawDate=new DrawDate(url);
        drawDate.connectSecond();
        topicContent= drawDate.getTopicContent();
        list=drawDate.getSecondRequestList();
        return list;
    }
}

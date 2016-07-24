package com.example.administrator.v2exapp.netspider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.administrator.v2exapp.listadapter.ListWithNetAdapter;
import com.example.administrator.v2exapp.mycomponents.RefreshListview;
import com.example.administrator.v2exapp.save.SaveTask;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 * 爬取第一层内容
 */
public class FirstTask extends AsyncTask<String, Integer, List<Map<String,String>>> {
    ProgressBar progressBar ;
    ListWithNetAdapter adapter;
    ListWithNetAdapter spareAdapter=null;//设置两个为了防止刷新时点击
    RefreshListview listView;//下拉框
    Context context;
    int i;
    int index;
    String content;
    public FirstTask(ProgressBar progressBar, ListWithNetAdapter adapter,ListWithNetAdapter spareAdapter,RefreshListview listView, Context context, int index){
        this.progressBar=progressBar;
        this.adapter=adapter;
        this.listView=listView;
        this.context=context;
        this.index=index;
        this.spareAdapter=spareAdapter;
        adapter.setIfFinishDownload(false);
        spareAdapter.setIfFinishDownload(false);
    }
    public FirstTask(ProgressBar progressBar, ListWithNetAdapter adapter,RefreshListview listView, Context context, int index){
        this.progressBar=progressBar;
        this.adapter=adapter;
        adapter.setIfFinishDownload(false);
        this.listView=listView;
        this.context=context;
        this.index=index;
    }
    @Override
    protected void onPreExecute() {
        Log.d("wiwiwi","ewedasdasdas");
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<Map<String, String>> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        adapter.setData(result);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(spareAdapter!=null){
            spareAdapter.setData(result);
            spareAdapter.setIfFinishDownload(true);
            spareAdapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        adapter.setIfFinishDownload(true);
        SaveTask saveTask=new SaveTask(index,content);
        saveTask.execute();
    }

    @Override
    protected List<Map<String,String>> doInBackground(String... params) {
        List<Map<String,String>> list=null;
        DrawDate drawDate=new DrawDate(index);
        drawDate.connectFirst();
        list=drawDate.getRequestList();
        content=drawDate.content;
        return list;
    }
}

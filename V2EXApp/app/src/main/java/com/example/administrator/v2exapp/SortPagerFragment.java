package com.example.administrator.v2exapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.administrator.v2exapp.mycomponents.DragListView;
import com.example.administrator.v2exapp.save.SaveConfigureTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/20.
 */
public class SortPagerFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    DragListView sortListView;
    static public boolean ifCancelOntouch=false;
    Button sure,cancel;
    ArrayList<String> tabList;
    ArrayList<String> valueList;
    List<Map<String,String>> Lists;
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        View view=inflater.inflate(R.layout.fragment_sort_pager,container,false);
        sortListView=(DragListView)view.findViewById(R.id.sortlistview);
        Lists=mainActivity.tabList;
        tabList=new ArrayList<String>();
        for(int i=0;i<11;i++){
            tabList.add(mainActivity.tabList.get(i).get("tab"));
        }
        valueList=new ArrayList<String>();
        for(int i=0;i<11;i++){
            valueList.add(mainActivity.tabList.get(i).get("value"));
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,tabList);
        sortListView.setList(tabList,valueList);
        sortListView.setAdapter(adapter);
        sure=(Button)view.findViewById(R.id.sortsure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray=new JSONArray();
                try {
                    Lists = new ArrayList<Map<String ,String>>();
                    for (int i = 0; i < 11; i++) {
                        JSONObject jsonObject=new JSONObject();
                        Map<String ,String> map = new HashMap<String, String>();
                        map.put("tab",sortListView.getTabList().get(i));jsonObject.put("tab",sortListView.getTabList().get(i));
                        map.put("value",sortListView.getvalueList().get(i));jsonObject.put("value",sortListView.getvalueList().get(i));
                        Lists.add(map);
                        jsonArray.put(jsonObject);
                    }
                    mainActivity.tabList=Lists;
                }
                catch (Exception e){e.printStackTrace();}
                SaveConfigureTask saveConfigureTask=new SaveConfigureTask(jsonArray.toString(),mainActivity.handler);
                saveConfigureTask.execute();
                ifCancelOntouch=false;
                getFragmentManager().beginTransaction()
                        .remove(SortPagerFragment.this)
                        .commit();
            }
        });
         cancel=(Button)view.findViewById(R.id.sortcancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction()
                        .remove(SortPagerFragment.this)
                        .commit();
                ifCancelOntouch=false;
            }
        });
        return view;
    }



}
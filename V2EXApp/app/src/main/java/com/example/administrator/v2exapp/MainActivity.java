package com.example.administrator.v2exapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.v2exapp.downloadimg.DownImage;
import com.example.administrator.v2exapp.downloadimg.DownImageTask;
import com.example.administrator.v2exapp.listadapter.ListWithNetAdapter;
import com.example.administrator.v2exapp.listadapter.ListWithoutNetAdapter;
import com.example.administrator.v2exapp.listadapter.MyPagerAdapter;
import com.example.administrator.v2exapp.netspider.FirstTask;
import com.example.administrator.v2exapp.save.CacheImage;
import com.example.administrator.v2exapp.save.FileShowTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //记录当前指示条和VIEW页面所在位置
    int currentIndex=0;
    //记录选择的指示条和VIEW页面所在位置
    int selectIndex;
    RadioButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11;
    HorizontalScrollView horizontalScrollView;
    // view数组
    private List<View> viewList;
    //viewPager
    ViewPager vp;
    //listview
    ListView listView;
    //listview适配器
    ListWithNetAdapter listWithNetAdapter;ListWithoutNetAdapter listWithoutNetAdapter;
    //进度条
    ProgressDialog progressDialog;
    //是否有网
    boolean ifHasNet;
    //监听当前的 index
    int mainIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CacheImage cacheImage=new CacheImage();
        InitRadioButton();
        InitialPager();
        MyPagerAdapter adapter = new MyPagerAdapter(viewList);
        //刷新按钮
        Button refresh=(Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWithNetAdapter = new ListWithNetAdapter(MainActivity.this,mainIndex);
                FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, mainIndex);
                downloadTheLastTask.execute();
            }
        });
        //设定viewPager适配器
        vp = (ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new MyPageChangeListener());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在下载....");
        //初始化界面
        //listWithNetAdapter=new ListWithNetAdapter(this,0);
        //FirstTask firstTask=new  FirstTask(progressDialog,listWithNetAdapter,listView,MainActivity.this,0);
        //firstTask.execute();
        //无网时
        //文件是否存在
        ifHasNet=isNetworkAvailable(MainActivity.this);
        if(ifHasNet){
            listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 0);
            FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 0);
            downloadTheLastTask.execute();
        }
        else {
            listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,0);
            FileShowTask fileShowTask=new FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,0);
            fileShowTask.execute();
        }

    }
    //判断是否有网
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //恢复原有按钮颜色
    public void setColor(int index){
        if(index==1){btn1.setTextColor(Color.rgb(248,248,255));
            btn1.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==2){btn2.setTextColor(Color.rgb(248,248,255));
            btn2.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==3){btn3.setTextColor(Color.rgb(248,248,255));
            btn3.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==4){btn4.setTextColor(Color.rgb(248,248,255));
            btn4.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==5){btn5.setTextColor(Color.rgb(248,248,255));
            btn5.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==6){btn6.setTextColor(Color.rgb(248,248,255));
            btn6.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==7){btn7.setTextColor(Color.rgb(248,248,255));
            btn7.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==8){btn8.setTextColor(Color.rgb(248,248,255));
            btn8.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==9){btn9.setTextColor(Color.rgb(248,248,255));
            btn9.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==10){btn10.setTextColor(Color.rgb(248,248,255));
            btn10.setBackgroundColor(Color.rgb(211,211,211));}
        if(index==11){btn11.setTextColor(Color.rgb(248,248,255));
            btn11.setBackgroundColor(Color.rgb(211,211,211));}

    }
    //判断文件是否存在
    public boolean fileIsExists(int index){
        try{
            File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myv2ex"+"/save"+String.valueOf(index)+"content.txt");
            if(!f.exists()){
                return false;
            }

        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
    //初始化button
    private void InitRadioButton() {
        horizontalScrollView=(HorizontalScrollView)findViewById(R.id.scrollView) ;
        btn1 = (RadioButton) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=0;
                setColor(currentIndex);
                btn1.setTextColor(Color.rgb(0,0,0));
                btn1.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=1;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(0).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                ifHasNet=isNetworkAvailable(MainActivity.this);
                if( ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 0);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 0);
                    downloadTheLastTask.execute();
                }
                else {
                    listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,0);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,0);
                    fileShowTask.execute();
                }
            }
        });
        btn2 = (RadioButton) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=1;
                setColor(currentIndex);
                btn2.setTextColor(Color.rgb(0,0,0));
                btn2.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=2;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(1).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());

                // ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this,1);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 1);
                    downloadTheLastTask.execute();
                }
                else {
                    listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,1);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,1);
                    fileShowTask.execute();
                }
            }
        });
        btn3 = (RadioButton) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=2;
                setColor(currentIndex);
                btn3.setTextColor(Color.rgb(0,0,0));
                btn3.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=3;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(2).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                //ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 2);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 2);
                    downloadTheLastTask.execute();
                }
                else { listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,2);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,2);
                    fileShowTask.execute();
                }
            }
        });
        btn4 = (RadioButton) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=3;
                setColor(currentIndex);
                btn4.setTextColor(Color.rgb(0,0,0));
                btn4.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=4;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(3).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                // ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 3);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 3);
                    downloadTheLastTask.execute();
                }
                else { listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,3);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,3);
                    fileShowTask.execute();

                }
            }
        });
        btn5 = (RadioButton) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=4;
                setColor(currentIndex);
                btn5.setTextColor(Color.rgb(0,0,0));
                btn5.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=5;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(4).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                // ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 4);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 4);
                    downloadTheLastTask.execute();
                }
                else {listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,4);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,4);
                    fileShowTask.execute();

                }
            }
        });
        btn6 = (RadioButton) findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=5;
                setColor(currentIndex);
                btn6.setTextColor(Color.rgb(0,0,0));
                btn6.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=6;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(5).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                //ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 5);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 5);
                    downloadTheLastTask.execute();
                }
                else { listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,5);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,5);
                    fileShowTask.execute();

                }
            }
        });
        btn7 = (RadioButton) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=6;
                setColor(currentIndex);
                btn7.setTextColor(Color.rgb(0,0,0));
                btn7.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=7;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(6).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                // ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 6);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 6);
                    downloadTheLastTask.execute();
                }
                else {listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,6);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,6);
                    fileShowTask.execute();

                }
            }
        });
        btn8 = (RadioButton) findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=7;
                setColor(currentIndex);
                btn8.setTextColor(Color.rgb(0,0,0));
                btn8.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=8;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(7).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                //ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 7);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 7);
                    downloadTheLastTask.execute();
                }
                else {  listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,7);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,7);
                    fileShowTask.execute();

                }
            }
        });
        btn9 = (RadioButton) findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=8;
                setColor(currentIndex);
                btn9.setTextColor(Color.rgb(0,0,0));
                btn9.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=9;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(8).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                // ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 8);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 8);
                    downloadTheLastTask.execute();
                }
                else {listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,8);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,8);
                    fileShowTask.execute();

                }
            }
        });
        btn10 = (RadioButton) findViewById(R.id.btn10);
        btn10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=9;
                setColor(currentIndex);
                btn10.setTextColor(Color.rgb(0,0,0));
                btn10.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=10;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(9).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                //ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 9);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 9);
                    downloadTheLastTask.execute();
                }
                else { listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,9);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,9);
                    fileShowTask.execute();

                }
            }
        });
        btn11 = (RadioButton) findViewById(R.id.btn11);
        btn11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainIndex=10;
                setColor(currentIndex);
                btn11.setTextColor(Color.rgb(0,0,0));
                btn11.setBackgroundColor(Color.rgb(248,248,255));
                currentIndex=11;
                vp.setCurrentItem(currentIndex-1);
                listView=(ListView)viewList.get(10).findViewById(R.id.list);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                // ifHasNet=isNetworkAvailable(MainActivity.this);
                if( ifHasNet){
                    listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 10);
                    FirstTask downloadTheLastTask = new FirstTask(progressDialog, listWithNetAdapter, listView, MainActivity.this, 10);
                    downloadTheLastTask.execute();
                }
                else { listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,10);
                    FileShowTask fileShowTask=new  FileShowTask(progressDialog,listWithoutNetAdapter,listView,MainActivity.this,10);
                    fileShowTask.execute();

                }
            }
        });
        btn1.setTextColor(Color.rgb(248,248,255));
        btn1.setBackgroundColor(Color.rgb(211,211,211));



    }


    //初始化viewpager
    public void InitialPager() {
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        LayoutInflater inflater = getLayoutInflater();

        for(int i=0;i<11;i++) {
            View view = inflater.inflate(R.layout.layout_viewofpager, null);
            viewList.add(view);
        }
        listView=(ListView)  viewList.get(0).findViewById(R.id.list);
        listView.setOnItemClickListener(new MyListViewClicklistener());
    }
    //viewpager的页面改变监听器
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            if(arg0==0){btn1.performClick();horizontalScrollView.scrollTo(0,0);}
            if(arg0==1){btn2.performClick();horizontalScrollView.scrollTo(0,0);}
            if(arg0==2){btn3.performClick();horizontalScrollView.scrollTo(0,0);}
            if(arg0==3){btn4.performClick();horizontalScrollView.scrollTo(0,0);}
            if(arg0==4){btn5.performClick();horizontalScrollView.scrollTo(150,0);}
            if(arg0==5){btn6.performClick();horizontalScrollView.scrollTo(150,0);}
            if(arg0==6){btn7.performClick();horizontalScrollView.scrollTo(200,0);}
            if(arg0==7){btn8.performClick();horizontalScrollView.scrollTo(250,0);}
            if(arg0==8){btn9.performClick();horizontalScrollView.scrollTo(320,0);}
            if(arg0==9){btn10.performClick();horizontalScrollView.scrollTo(800,0);}
            if(arg0==10){btn11.performClick();horizontalScrollView.scrollTo(800,0);}

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    //listview items监听器
    class MyListViewClicklistener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("haha","qweqwrewrwe");
            Date date=new Date(listWithNetAdapter.getList().get(position).get("newUrl"),
                    listWithNetAdapter.getList().get(position).get("name"),
                    listWithNetAdapter.getList().get(position).get("type"),
                    listWithNetAdapter.getList().get(position).get("showId"),
                    listWithNetAdapter.getList().get(position).get("time"),
                    listWithNetAdapter.getList().get(position).get("content"),
                    listWithNetAdapter.getIndex(),
                    listWithNetAdapter.getList().get(position).get("ima"));
            Bundle dates=new Bundle();
            dates.putSerializable("dates",date);
            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
            intent.putExtras(dates);
            startActivity(intent);
        }
    }
    //页面listview适配器




}



package com.example.administrator.v2exapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.example.administrator.v2exapp.listadapter.ListWithNetAdapter;
import com.example.administrator.v2exapp.listadapter.ListWithoutNetAdapter;
import com.example.administrator.v2exapp.listadapter.MyPagerAdapter;
import com.example.administrator.v2exapp.mycomponents.MyDrawerLayout;
import com.example.administrator.v2exapp.mycomponents.MyToolbar;
import com.example.administrator.v2exapp.netspider.FirstTask;
import com.example.administrator.v2exapp.save.DateFromFile;
import com.example.administrator.v2exapp.save.FileShowTask;
import com.example.administrator.v2exapp.save.SaveToFile;
import com.example.administrator.v2exapp.search.SearchChooseActivity;
import com.example.netlibrary.CacheImage;
import com.example.administrator.v2exapp.mycomponents.RefreshListview;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SetTabsTask setTabsTask;
    List<Map<String,String>> tabList;
    Handler handler;
    //记录当前指示条和VIEW页面所在位置
    int currentIndex;
    ArrayList<RadioButton> buttons=new ArrayList<RadioButton>();
    RadioButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11;
    ArrayList<RadioButton> radioButtons=new ArrayList<RadioButton>();
    ArrayList<String> valueList=new ArrayList<String>();
    HorizontalScrollView horizontalScrollView;
    // view数组
    private List<View> viewList;
    //viewPager
    ViewPager viewPagerp;
    //listview
    RefreshListview listView;
    //listview适配器
    ListWithNetAdapter listWithNetAdapter=null;ListWithoutNetAdapter listWithoutNetAdapter=null;
    //进度条
    ProgressBar progressBar;
    ProgressBar reProgressBar;
    //是否有网
    boolean ifHasNet=false;
    //监听当前的 index
    int mainIndex;
    private MyToolbar toolbar;
    //drawer里面按钮
    Button delete,sortpager;
    private MyDrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    //进度条初始位置 上次位置
    float originY;
    //屏蔽判断
    boolean ifCancelOntouch;
    SortPagerFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentIndex=0;
        mainIndex=0;
        CacheImage cacheImage=new CacheImage();
        InitialPager();
        InitRadioButton();
        final MyPagerAdapter adapter = new MyPagerAdapter(viewList);
        //设定viewPager适配器
        viewPagerp = (ViewPager)findViewById(R.id.viewpager);
        viewPagerp.setAdapter(adapter);
        viewPagerp.addOnPageChangeListener(new MyPageChangeListener());
        progressBar = (ProgressBar)findViewById(R.id.bar);
        progressBar .setVisibility(View.VISIBLE);
        reProgressBar=(ProgressBar)findViewById(R.id.rebar);
        reProgressBar.setVisibility(View.INVISIBLE);
        originY=progressBar.getY();
        //初始化界面
        //无网时
        ifHasNet=isNetworkAvailable(MainActivity.this);
        if(ifHasNet){
            listWithNetAdapter = new ListWithNetAdapter(MainActivity.this, 0);
            //listView.setOnScrollListener(new myListViewlistener());
            listView.initial(originY,progressBar,reProgressBar,mainIndex);
            FirstTask downloadTheLastTask = new FirstTask(progressBar ,listWithNetAdapter, listView, MainActivity.this, 0);
            downloadTheLastTask.execute();
        }
        else {
            listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,0);
            listView.initial(originY,progressBar,reProgressBar,mainIndex);
            FileShowTask fileShowTask=new  FileShowTask(progressBar,listWithoutNetAdapter,listView,MainActivity.this,0);
            fileShowTask.execute();
        }
        toolbar = (MyToolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("浏览");
        drawOnclick();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (MyDrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) ;
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ifCancelOntouch=false;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                refresh();
            }

        };
    }
    //重启Activity
    public void refresh() {
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
    //drawerLayout中点击事件
    public void drawOnclick(){
        delete=(Button)findViewById(R.id.delete);
        sortpager=(Button)findViewById(R.id.sortpager);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveToFile saveToFile=new SaveToFile(MainActivity.this);
                saveToFile.deleteFile();
            }
        });
        sortpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new  SortPagerFragment();
                // 使用fragment替换book_detail_container容器当前显示的Fragment
                getFragmentManager().beginTransaction()
                        .replace(R.id.frage, fragment)
                        .commit();
                SortPagerFragment.ifCancelOntouch=true;
            }
        });
    }
    //menu布局
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = new MenuInflater(this);
        //装填R.menu.my_menu对应的菜单，并添加到menu中
        inflator.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //自定义TAB按钮点击事件
    public class MyOnClickListener implements View.OnClickListener {
        int i;
        public MyOnClickListener(int i){
            this.i=i;
        }
        @Override
        public void onClick(View view) {
            if(currentIndex!=i){
                radioButtons.get(i).setTextColor(Color.rgb(0,0,0));
                radioButtons.get(i).setBackgroundColor(Color.rgb(248,248,255));
                mainIndex=Integer.parseInt(valueList.get(i));
                radioButtons.get(currentIndex).setTextColor(Color.rgb(248,248,255));
                radioButtons.get(currentIndex).setBackgroundColor(Color.rgb(211,211,211));
                currentIndex=i;
                viewPagerp.setCurrentItem(currentIndex);
                listView=(RefreshListview) viewList.get(i).findViewById(R.id.list);
                listView.initial(originY,progressBar,reProgressBar,mainIndex);
                listView.setOnItemClickListener(new MyListViewClicklistener());
                ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    ListWithNetAdapter listWithNetAdapter2=new ListWithNetAdapter(MainActivity.this,i);
                    FirstTask downloadTheLastTask = new FirstTask(progressBar, listWithNetAdapter2,listWithNetAdapter, listView, MainActivity.this,mainIndex);
                    downloadTheLastTask.execute();
                }
                else {
                    listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this,i);
                    FileShowTask fileShowTask=new  FileShowTask(progressBar,listWithoutNetAdapter,listView,MainActivity.this,mainIndex);
                    fileShowTask.execute();
                }}
        }
    }
    // 菜单项被单击后的回调方法
    public boolean onOptionsItemSelected(MenuItem mi)
    {   reProgressBar.clearAnimation();
        if(mi.isCheckable())
        {
            // 勾选该菜单项
            mi.setChecked(true);  // ②
        }
        //判断单击的是哪个菜单项，并有针对性地作出响应
        switch (mi.getItemId())
        {
            case R.id.search:
                Intent intent=new Intent(MainActivity.this,SearchChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh:
                ifHasNet=isNetworkAvailable(MainActivity.this);
                if(ifHasNet){
                    ListWithNetAdapter listWithNetAdapter2=new ListWithNetAdapter(MainActivity.this,mainIndex);
                    listView.initial(originY,progressBar,reProgressBar,mainIndex);
                    FirstTask downloadTheLastTask = new FirstTask(progressBar, listWithNetAdapter2,listWithNetAdapter, listView, MainActivity.this, mainIndex);
                    downloadTheLastTask.execute();
                }
                else {
                    listWithoutNetAdapter=new ListWithoutNetAdapter(MainActivity.this, mainIndex);
                    listView.initial(originY,progressBar,reProgressBar,mainIndex);
                    FileShowTask fileShowTask=new  FileShowTask(progressBar,listWithoutNetAdapter,listView,MainActivity.this, mainIndex);
                    fileShowTask.execute();
                }
                break;
        }
        return true;
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
    //初始化button
    private void InitRadioButton() {
        horizontalScrollView=(HorizontalScrollView)findViewById(R.id.scrollView) ;
        btn1 = (RadioButton) findViewById(R.id.btn1);
        radioButtons.add(btn1);
        btn2 = (RadioButton) findViewById(R.id.btn2);
        radioButtons.add(btn2);
        btn3 = (RadioButton) findViewById(R.id.btn3);
        radioButtons.add(btn3);
        btn4 = (RadioButton) findViewById(R.id.btn4);
        radioButtons.add(btn4);
        btn5 = (RadioButton) findViewById(R.id.btn5);
        radioButtons.add(btn5);
        btn6 = (RadioButton) findViewById(R.id.btn6);
        radioButtons.add(btn6);
        btn7 = (RadioButton) findViewById(R.id.btn7);
        radioButtons.add(btn7);
        btn8 = (RadioButton) findViewById(R.id.btn8);
        radioButtons.add(btn8);
        btn9 = (RadioButton) findViewById(R.id.btn9);
        radioButtons.add(btn9);
        btn10 = (RadioButton) findViewById(R.id.btn10);
        radioButtons.add(btn10);
        btn11 = (RadioButton) findViewById(R.id.btn11);
        radioButtons.add(btn11);
        btn1.setTextColor(Color.rgb(0,0,0));
        btn1.setBackgroundColor(Color.rgb(248,248,255));
        setTabsTask=new SetTabsTask();
        setTabsTask.execute();
    }
    //初始化viewpager
    public void InitialPager() {
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        LayoutInflater inflater = getLayoutInflater();
        for(int i=0;i<11;i++) {
            View view = inflater.inflate(R.layout.layout_viewofpager, null);
            viewList.add(view);
        }
        listView=(RefreshListview)viewList.get(0).findViewById(R.id.list);
        listView.initial(originY,progressBar,reProgressBar,mainIndex);
        listView.setOnItemClickListener(new MyListViewClicklistener());
    }
    //viewpager的页面改变监听器
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            listWithNetAdapter.setIfFinishDownload(false);
            reProgressBar.clearAnimation();
            radioButtons.get(arg0).performClick();horizontalScrollView.scrollTo((arg0-3)*150,0);
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
            ifHasNet=isNetworkAvailable(MainActivity.this);
            if(ifHasNet){Log.d("pipi",listWithNetAdapter.getList().get(position).get("newUrl"));
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
                startActivity(intent);}
        }
    }
    //显示TABS
    class SetTabsTask extends AsyncTask<String, Integer, List<Map<String,String>>> {
        int j;
        public  SetTabsTask(){
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
            if (tabList==null){
                tabList=result;}
            valueList=new ArrayList<String>();
            for(int i=0;i<tabList.size();i++){
                radioButtons.get(i).setText(tabList.get(i).get("tab"));
                valueList.add(tabList.get(i).get("value"));
            }
            for(int j=0;j<11;j++){
                radioButtons.get(j).setOnClickListener(new MyOnClickListener(j));
            }
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

        @Override
        protected List<Map<String,String>> doInBackground(String... params) {
            List<Map<String,String>> list=null;
            if(tabList==null){
                DateFromFile dateFromFile=new DateFromFile();
                list=dateFromFile.getRequestConfigureList();}
            return list;
        }
    }

}
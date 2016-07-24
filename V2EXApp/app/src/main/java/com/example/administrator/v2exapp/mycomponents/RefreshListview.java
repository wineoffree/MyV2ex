package com.example.administrator.v2exapp.mycomponents;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.administrator.v2exapp.R;
import com.example.administrator.v2exapp.SortPagerFragment;
import com.example.administrator.v2exapp.listadapter.ListWithNetAdapter;
import com.example.administrator.v2exapp.netspider.FirstTask;
import com.example.netlibrary.DownImageTask;

/**
 * Created by Administrator on 2016/7/22.
 */
public class RefreshListview extends ListView {
    //进度条初始位置 上次位置
    float originY;float lastY;
    Animation animation = null;
    //上次手势位置
    float lastGestureY;
    //记录初始手势位置
    float originGestureY;
    ProgressBar progressBar;
    ProgressBar reProgressBar;
    Context context;
    int mainIndex;
    boolean ifHasNet;
    public RefreshListview(Context context) {
        super(context);
        this.context=context;
    }
    public RefreshListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }
    public  void initial(float originY,ProgressBar progressBar,ProgressBar reprogressBar,int mainIndex){
        this.setOnScrollListener(new myListViewlistener());
        this.originY=originY;
        this.progressBar=progressBar;
        this.reProgressBar=reprogressBar;
        this.mainIndex=mainIndex;
        ifHasNet = isNetworkAvailable(context);
    }
    public boolean onTouchEvent(MotionEvent event){
        if (getFirstVisiblePosition()==0) {
            if (getAdapter() != null) {
                if (ifHasNet) {
                    ListWithNetAdapter listWithNetAdapter = (ListWithNetAdapter) getAdapter();
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        lastY = originY;
                        originGestureY = event.getRawY();
                        lastGestureY = event.getRawY();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (listWithNetAdapter.getIfFinishDownload()) {
                            if ((event.getRawY() - originGestureY) > 300) {
                                {
                                    ListWithNetAdapter listWithNetAdapter2 = new ListWithNetAdapter(context, mainIndex);
                                    this.setOnScrollListener(new myListViewlistener());
                                    FirstTask downloadTheLastTask = new FirstTask(progressBar, listWithNetAdapter2, listWithNetAdapter, this, context, mainIndex);
                                    downloadTheLastTask.execute();
                                    reProgressBar.clearAnimation();
                                    reProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                }
                            } else {
                                if ((lastY - originY) > 3) {
                                    animation = new TranslateAnimation(0, 0, lastY, originY);
                                    animation.setDuration(400);
                                    reProgressBar.startAnimation(animation);
                                }
                            }
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if ((event.getRawY() - lastGestureY) > 3)
                            if (listWithNetAdapter.getIfFinishDownload())
                                if (event.getRawY() > lastGestureY)
                                    if (getFirstVisiblePosition() == 0) {
                                        if (lastY < 400) {
                                            AnimationSet animationSet = new AnimationSet(true);
                                            reProgressBar.setVisibility(View.VISIBLE);
                                            TranslateAnimation animation = new TranslateAnimation(0, 0, lastY, lastY + event.getRawY() - lastGestureY);
                                            animation.setFillAfter(true);
                                            animation.setDuration(400);
                                            animationSet.addAnimation(animation);
                                            float from, to;
                                            from = (lastY - originY) / (350 - originY);
                                            to = (lastY + event.getRawY() - lastGestureY - originY) / (350 - originY);
                                            AlphaAnimation alphaAnimation = new AlphaAnimation(from, to);
                                            alphaAnimation.setFillAfter(true);
                                            alphaAnimation.setDuration(400);
                                            if (from <= 1)
                                                animationSet.addAnimation(alphaAnimation);
                                            reProgressBar.startAnimation(animationSet);
                                            lastY = lastY + event.getRawY() - lastGestureY;
                                            lastGestureY = event.getRawY();
                                        }
                                        if (lastY > 400) {
                                            reProgressBar.setVisibility(View.VISIBLE);
                                            animation = new TranslateAnimation(0, 0, lastY, lastY);
                                            animation.setDuration(1000);
                                            reProgressBar.startAnimation(animation);
                                        }
                                    }
                        reProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //判断是否有网
    public boolean isNetworkAvailable(Context context)
    {
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
    //自定义listview滚动监听
    public class myListViewlistener implements OnScrollListener
    {

        // 标记上次滑动位置
        private int lastPosition;
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            if(ifHasNet) {
                ListWithNetAdapter listWithNetAdapter=(ListWithNetAdapter)getAdapter();
                switch (scrollState) {

                    case OnScrollListener.SCROLL_STATE_IDLE://停止滚动
                    {   try {
                        //设置为停止滚动
                        listWithNetAdapter.setScrollState(false);
                    }
                    catch (Exception e){e.printStackTrace();}
                        //当前屏幕中listview的子项的个数
                        int count = absListView.getChildCount();
                        Log.e("MainActivity", count + "");
                        for (int i = 0; i < count; i++) {

                            //获取到item的头像
                            ImageView ima = (ImageView) absListView.getChildAt(i).findViewById(R.id.ima);


                            if (!ima.getTag().equals("1")) {//!="1"说明需要加载数据
                                Log.d("imamamama",ima.toString());
                                String image_url = ima.getTag().toString();//直接从Tag中取出我们存储的数据image——url
                                new DownImageTask(ima).execute(image_url);
                                ima.setTag("1");//设置为已加载过数据
                            }
                        }
                        break;
                    }
                    case OnScrollListener.SCROLL_STATE_FLING://滚动做出了抛的动作
                    {
                        //设置为正在滚动
                        try {

                            listWithNetAdapter.setScrollState(true);

                        }
                        catch (Exception e){e.printStackTrace();}
                        break;
                    }

                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动
                    { try {

                        //设置为正在滚动
                        listWithNetAdapter.setScrollState(true);

                    }
                    catch (Exception e){e.printStackTrace();}
                        break;
                    }
                }

            }

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {


        }
    }
}

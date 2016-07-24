package com.example.administrator.v2exapp.mycomponents;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.administrator.v2exapp.SortPagerFragment;

/**
 * Created by Administrator on 2016/7/22.
 */

public class MyDrawerLayout extends DrawerLayout{
    public MyDrawerLayout(Context context) {
        super(context);

    }
    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(SortPagerFragment.ifCancelOntouch)
            return true;
        return super.dispatchTouchEvent(ev);
    }
}

package com.example.administrator.v2exapp.mycomponents;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;

import com.example.administrator.v2exapp.SortPagerFragment;

/**
 * Created by Administrator on 2016/7/22.
 */
public class MyToolbar extends Toolbar{
    public MyToolbar(Context context) {
        super(context);

    }
    public MyToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}

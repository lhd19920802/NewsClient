package com.lhd.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lhd.news.base.BasePager;

/**
 * Created by lihuaidong on 2017/11/30 15:01.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class SettingPager extends BasePager
{
    private static final String TAG = SettingPager.class.getSimpleName();
    private Context mContext;
    public SettingPager(Context context)
    {
        super(context);
        mContext=context;
    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "设置的数据被初始化了");
        tv_titlebar.setText("设置");
        ib_titlebar.setVisibility(View.GONE);
        TextView textView = new TextView(mContext);
        textView.setText("这是设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        fl_basepager.addView(textView);
    }
}

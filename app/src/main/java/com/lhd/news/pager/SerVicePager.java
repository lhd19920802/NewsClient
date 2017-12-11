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
public class SerVicePager extends BasePager
{
    private static final String TAG = SerVicePager.class.getSimpleName();
    private Context mContext;
    public SerVicePager(Context context)
    {
        super(context);
        mContext=context;
    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "服务的数据被初始化了");
        tv_titlebar.setText("服务");
        ib_titlebar.setVisibility(View.GONE);
        TextView textView = new TextView(mContext);
        textView.setText("这是服务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        fl_basepager.addView(textView);
    }
}

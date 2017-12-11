package com.lhd.news.pager.detail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lhd.news.base.MenuDetailBasePager;

/**
 * Created by lihuaidong on 2017/12/1 9:32.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：组图详情页面
 */
public class PhotosDetailPager extends MenuDetailBasePager
{

    private TextView textView;
    public PhotosDetailPager(Context context)
    {
        super(context);

    }

    @Override
    public View initView()
    {
        textView = new TextView(mContext);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData()
    {
        super.initData();
        System.out.println("组图详情页面数据被初始化了...");
        textView.setText("组图详情页面的内容");
    }
}

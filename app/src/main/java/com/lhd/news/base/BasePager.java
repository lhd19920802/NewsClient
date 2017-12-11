package com.lhd.news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lhd.news.R;

/**
 * Created by lihuaidong on 2017/11/30 14:45.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class BasePager
{
    public Context mContext;
    public View rootView;

    public ImageButton ib_titlebar;
    public TextView tv_titlebar;
    public FrameLayout fl_basepager;
    public BasePager(Context context)
    {
        mContext=context;
        rootView=initView();
    }

    public View initView()
    {
        View view = View.inflate(mContext, R.layout.base_pager, null);
        ib_titlebar = (ImageButton) view.findViewById(R.id.ib_titlebar);
        tv_titlebar = (TextView) view.findViewById(R.id.tv_titlebar);
        fl_basepager = (FrameLayout) view.findViewById(R.id.fl_basepager);

        return view;
    }

    public void initData()
    {

    }
}

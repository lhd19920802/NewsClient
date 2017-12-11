package com.lhd.news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by lihuaidong on 2017/12/1 9:00.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：详情页面菜单的抽取类
 */
public abstract class MenuDetailBasePager
{
    public Context mContext;

    public View rootView;

    private ImageButton ib_titlebar;
    private TextView tv_titlebar;

    private FrameLayout fl_basepager;
    public MenuDetailBasePager(Context context)
    {
        this.mContext = context;
        rootView=initView();
    }

    public abstract View initView();


//        View view = View.inflate(mContext, R.layout.menudetail_basepager, null);
//        ib_titlebar = (ImageButton) view.findViewById(R.id.ib_titlebar);
//        tv_titlebar = (TextView) view.findViewById(R.id.tv_titlebar);
//        fl_basepager = (FrameLayout) view.findViewById(R.id.fl_basepager);


    public void initData()
    {

    }


}

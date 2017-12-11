package com.lhd.news.pager.detail;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lhd.news.MainActivity;
import com.lhd.news.R;
import com.lhd.news.base.MenuDetailBasePager;
import com.lhd.news.bean.NewsCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihuaidong on 2017/12/1 9:32.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：专题详情页面
 */
public class TopicDetailPager extends MenuDetailBasePager
{

    private static final String TAG = TopicDetailPager.class.getSimpleName();
    private final List<NewsCenterBean.DataBean.ChildrenBean> children;
    private ViewPager vp_detail_pager;

    private TabLayout tablayout_topic_detail;

    private ImageButton ib_detail_next;

    //页签页面的数据集合
    private List<MenuDetailBasePager> tabDetailPagerList;

    public TopicDetailPager(Context context, List<NewsCenterBean.DataBean.ChildrenBean> children)
    {
        super(context);
        this.children = children;

    }

    @Override
    public View initView()
    {
        View view = View.inflate(mContext, R.layout.topic_detail_pager, null);
        vp_detail_pager = (ViewPager) view.findViewById(R.id.vp_detail_pager);
        ib_detail_next = (ImageButton) view.findViewById(R.id.ib_detail_next);

        tablayout_topic_detail = (TabLayout) view.findViewById(R.id.tablayout_topic_detail);


        ib_detail_next.setOnClickListener(new MyOnClickListener());
        return view;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener

    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            if (position == 0)
            {
                isEnableSlidingMenu(true);
            }
            else
            {
                isEnableSlidingMenu(false);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }


    /**
     * 是否可以滑出左侧菜单
     *
     * @param isEnableSliding
     */
    private void isEnableSlidingMenu(boolean isEnableSliding)
    {
        MainActivity mainActivity = (MainActivity) mContext;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (isEnableSliding)
        {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
        else
        {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class MyOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            vp_detail_pager.setCurrentItem(vp_detail_pager.getCurrentItem() + 1);
        }
    }

    @Override
    public void initData()
    {
        super.initData();
        System.out.println("专题详情页面数据被初始化了...");

        //准备数据
        tabDetailPagerList = new ArrayList<>();
        for (int i = 0; i < children.size(); i++)
        {
            tabDetailPagerList.add(new TabDetailPager(mContext, children.get(i)));
        }

        MyPagerAdapter mAdapter = new MyPagerAdapter();
        vp_detail_pager.setAdapter(mAdapter);
        //        tablayout_topic_detail.setViewPager(vp_detail_pager);

        //TabLayout关联ViewPager
        tablayout_topic_detail.setupWithViewPager(vp_detail_pager);
        //一定要设置设置滚动模式
        tablayout_topic_detail.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_detail_pager.setOnPageChangeListener(new MyOnPageChangeListener());


        for (int i = 0; i < tablayout_topic_detail.getTabCount(); i++)
        {
            TabLayout.Tab tab = tablayout_topic_detail.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
    }

    class MyPagerAdapter extends PagerAdapter

    {
        @Override
        public int getCount()
        {
            return tabDetailPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            Log.e(TAG, "instantiateItem==");
            MenuDetailBasePager menuDetailBasePager = tabDetailPagerList.get(position);
            container.addView(menuDetailBasePager.rootView);
            menuDetailBasePager.initData();
            return menuDetailBasePager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return children.get(position).getTitle();

        }

        //自定义TAb的效果
        public View getTabView(int position)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item, null);
            TextView tv = (TextView) view.findViewById(R.id.textView);
            tv.setText(children.get(position).getTitle());
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            img.setImageResource(R.drawable.dot_focus);
            return view;
        }
    }
}

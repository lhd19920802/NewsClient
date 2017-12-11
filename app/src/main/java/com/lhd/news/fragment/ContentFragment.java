package com.lhd.news.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lhd.news.MainActivity;
import com.lhd.news.R;
import com.lhd.news.base.BaseFragment;
import com.lhd.news.base.BasePager;
import com.lhd.news.pager.GovAffairPager;
import com.lhd.news.pager.HomePager;
import com.lhd.news.pager.NewsCenterPager;
import com.lhd.news.pager.SerVicePager;
import com.lhd.news.pager.SettingPager;
import com.lhd.news.view.NoScrollViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihuaidong on 2017/11/30 9:52.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class ContentFragment extends BaseFragment
{

    //    使用XUtils3初始化布局
    @ViewInject(R.id.vp_content_fragment)
    private NoScrollViewPager vp_content_fragment;
    @ViewInject(R.id.rg_content_fragment)
    private RadioGroup rg_content_fragment;


    private List<BasePager> basePagerList;

    @Override
    public View initView()
    {
        View view = View.inflate(mContext, R.layout.content_fragment, null);
        basePagerList = new ArrayList<>();
        x.view().inject(this, view);
        return view;

    }

    @Override
    public void initData()
    {
        super.initData();
        System.out.println("正文页面的数据被初始化了");

        //默认选中第一个
        rg_content_fragment.check(R.id.rb_home);

        //准备数据
        basePagerList.add(new HomePager(mContext));
        basePagerList.add(new NewsCenterPager(mContext));
        basePagerList.add(new SerVicePager(mContext));
        basePagerList.add(new GovAffairPager(mContext));
        basePagerList.add(new SettingPager(mContext));

        vp_content_fragment.setAdapter(new MyPagerAdapter());

        //监听RadionGroup的状态改变页面
        rg_content_fragment.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //        vp_content_fragment.addOnPageChangeListener(new MyOnPageChangeListener());

        //提前设置首页不可以滑动
        isEnableSlidingMenu(false);
    }

    public NewsCenterPager getNewsCenterPager()
    {
        return (NewsCenterPager) basePagerList.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            switch (position)
            {
                case 0:
                    rg_content_fragment.check(R.id.rb_home);
                    break;
                case 1:
                    rg_content_fragment.check(R.id.rb_newscenter);
                    break;
                case 2:
                    rg_content_fragment.check(R.id.rb_smartservice);
                    break;
                case 3:
                    rg_content_fragment.check(R.id.rb_govaffair);
                    break;
                case 4:
                    rg_content_fragment.check(R.id.rb_setting);
                    break;
            }
        }

        @Override
        public void onPageSelected(int position)
        {

        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            int position = 0;
            switch (checkedId)
            {
                case R.id.rb_home:
                    position = 0;
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_newscenter:
                    position = 1;
                    isEnableSlidingMenu(true);
                    break;
                case R.id.rb_smartservice:
                    position = 2;
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_govaffair:
                    position = 3;
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_setting:
                    position = 4;
                    isEnableSlidingMenu(false);
                    break;
            }

            vp_content_fragment.setCurrentItem(position, false);
            //防止预加载下一页面的数据
            //            basePagerList.get(position).initData();
        }
    }

    /**
     * 是否可以滑出左侧菜单
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





    class MyPagerAdapter extends PagerAdapter

    {

        @Override
        public int getCount()
        {
            return basePagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            BasePager basePager = basePagerList.get(position);
            View view = basePager.rootView;
            container.addView(view);
            //分别调用孩子的initData()方法初始化数据
            basePager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }
}

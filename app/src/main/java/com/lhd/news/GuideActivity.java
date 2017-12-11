package com.lhd.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lhd.news.utils.CacheUtils;
import com.lhd.news.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity
{

    private ViewPager vp_guide;
    private Button btn_guide;
    private LinearLayout ll_guide;
    private List<ImageView> imageViewList;
    private ImageView iv_red_guide;
    private int distance;

    private int screenPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();


        initData();

        //设置适配器
        vp_guide.setAdapter(new MyPagerAdapter());


        //屏幕适配 显示都是以像素值进行显示的，把10当成dp，在不同的手机上转换成不同的像素值
        screenPoint = DensityUtil.dip2px(this, 10);
        //添加向导点
        addPoint();

        //移动向导红点
        vp_guide.addOnPageChangeListener(new MyOnPageChangeListener());


    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener

    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            //            distance*positionOffset
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenPoint,
                    screenPoint);
            params.leftMargin = (int) (distance * (positionOffset + position));
            iv_red_guide.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position)
        {
            if (position == imageViewList.size() - 1)
            {
                //显示
                btn_guide.setVisibility(View.VISIBLE);
                btn_guide.setOnClickListener(new MyOnClickListener());
            }
            else
            {
                btn_guide.setVisibility(View.GONE);
            }
        }


        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    /**
     * 立即体验按钮的监听
     */
    class MyOnClickListener implements View.OnClickListener

    {
        @Override
        public void onClick(View v)
        {
            CacheUtils.putBoolean(GuideActivity.this,"isEnter",true);
            startActivity(new Intent(GuideActivity.this,MainActivity.class));
            finish();
        }
    }

    private void addPoint()
    {
        for (int i = 0; i < imageViewList.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.gray_point_shape);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenPoint,
                    screenPoint);
            if (i != 0)
            {

                params.leftMargin = screenPoint;
            }
            imageView.setLayoutParams(params);
            ll_guide.addView(imageView);
        }

        iv_red_guide.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener
                ());

    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener

    {
        @Override
        public void onGlobalLayout()
        {
            //在这里面才可以测量间距
            distance = ll_guide.getChildAt(1).getLeft() - ll_guide.getChildAt(0).getLeft();

        }
    }

    class MyPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }

    private void initData()
    {
        int[] images = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        for (int i = 0; i < images.length; i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images[i]);
            imageViewList.add(imageView);
        }
    }

    private void initView()
    {
        setContentView(R.layout.activity_guide);
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        btn_guide = (Button) findViewById(R.id.btn_guide);
        ll_guide = (LinearLayout) findViewById(R.id.ll_guide);
        iv_red_guide = (ImageView) findViewById(R.id.iv_red_guide);
        imageViewList = new ArrayList<>();

    }
}

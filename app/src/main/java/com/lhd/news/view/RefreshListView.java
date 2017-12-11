package com.lhd.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lhd.news.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lihuaidong on 2017/12/7 9:33.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class RefreshListView extends ListView
{
    private ImageView iv_refresh_header;
    private ProgressBar pb_regresh_header;
    private TextView tv_refresh_pull;
    private TextView tv_refresh_date;
    private LinearLayout topViews;
    //下拉刷新控件
    private View pullDownHeader;
    private int measuredHeight;
    //顶部轮播图的实例
    private View topViewPager;
    private int mListViewOnScreenY = -1;
    private int topViewPagerOnScreenY;


    public static final int PULL_DOWN_REFRESH = 0;
    public static final int RELEASE_REFRESH = 1;
    public static final int REFRESHING = 2;

    private int currentStatus = PULL_DOWN_REFRESH;
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;
    private int footViewHeight;
    private View footView;
    private boolean isLoadMore=false;

    public RefreshListView(Context context)
    {
        super(context);
        initHeaderView(context);
        initFooterView(context);

        initAnimation();

    }

    /**
     * 初始化加载更多控件
     * @param context
     */
    private void initFooterView(Context context)
    {
        footView = View.inflate(context, R.layout.footer_view, null);
        footView.measure(0, 0);
        footViewHeight = footView.getMeasuredHeight();
        addFooterView(footView);

        //隐藏加载更多控件
        footView.setPadding(0, -footViewHeight, 0, 0);

        //设置监听listView的滚动 当滚动到最后一条的时候显示加载更多控件
        setOnScrollListener(new MyOnScrollListener());


    }

    class MyOnScrollListener implements OnScrollListener
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
            //当listView由惯性滚动或静止并且滑动到最后一个时候
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)
            {
                if(getAdapter().getCount()-1==getLastVisiblePosition()) {
                    //显示加载更多控件
                    footView.setPadding(0,0,0,0);


                    isLoadMore=true;
                    //回调接口 联网加载更多
                    if(onRefreshListener!=null) {
                        onRefreshListener.onLoad();
                    }

                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {

        }
    }


    /**
     * 初始化动画
     */
    private void initAnimation()
    {
        upAnimation = new RotateAnimation(0, -180, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        upAnimation.setDuration(200);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        downAnimation.setDuration(200);
        downAnimation.setFillAfter(true);

    }

    public RefreshListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initHeaderView(context);
        initFooterView(context);
        initAnimation();
    }

    private void initHeaderView(Context context)
    {
        topViews = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        iv_refresh_header = (ImageView) topViews.findViewById(R.id.iv_refresh_header);
        pb_regresh_header = (ProgressBar) topViews.findViewById(R.id.pb_regresh_header);
        tv_refresh_pull = (TextView) topViews.findViewById(R.id.tv_refresh_pull);
        tv_refresh_date = (TextView) topViews.findViewById(R.id.tv_refresh_date);

        pullDownHeader = topViews.findViewById(R.id.ll_refresh_header);


        pullDownHeader.measure(0, 0);
        measuredHeight = pullDownHeader.getMeasuredHeight();
        pullDownHeader.setPadding(0, -measuredHeight, 0, 0);
        //必须要把总的view加进去
        this.addHeaderView(topViews);


    }

    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                float distanceY = endY - startY;

                boolean isDisplayTopNews = isDisplayTopNews();

                if (!isDisplayTopNews)
                {

                    break;

                }

                if(currentStatus==REFRESHING)
                {
                    break;
                }

                //动态显示顶部刷新控件
                if (distanceY > 0)
                {
                    float top = -measuredHeight + distanceY;

                    if (top >= 0 && currentStatus != RELEASE_REFRESH)
                    {
                        currentStatus=RELEASE_REFRESH;
                        System.out.println("手松刷新...");
                        refreshHeaderViewStatus();
                    }
                    else if (top < 0 && currentStatus != PULL_DOWN_REFRESH)
                    {
                        currentStatus=PULL_DOWN_REFRESH;
                        System.out.println("下拉刷新...");
                        refreshHeaderViewStatus();
                    }

                    pullDownHeader.setPadding(0, (int) (top), 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:

                //定义接口，方便外界请求网络
                if(currentStatus==PULL_DOWN_REFRESH)
                {
                    pullDownHeader.setPadding(0, (int) (-measuredHeight), 0, 0);
                }
                else if(currentStatus==RELEASE_REFRESH) {
                    //切换成正在刷新状态
                    pullDownHeader.setPadding(0,0,0,0);
                    currentStatus=REFRESHING;
                    refreshHeaderViewStatus();

                    //回调接口 请求网络
                    if(onRefreshListener!=null) {
                        onRefreshListener.onPullDownRefresh();
                    }

                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onFinshRefresh(boolean isSuccess)
    {
        if(isLoadMore) {
            //恢复初始状态
            footView.setPadding(0,-footViewHeight,0,0);
            isLoadMore=false;
        }
        else
        {

            currentStatus=PULL_DOWN_REFRESH;
            pb_regresh_header.setVisibility(View.GONE);
            iv_refresh_header.setVisibility(View.VISIBLE);
            iv_refresh_header.clearAnimation();
            tv_refresh_pull.setText("下拉刷新...");

            //下拉刷新控件隐藏
            pullDownHeader.setPadding(0,-measuredHeight,0,0);


            if(isSuccess) {
                tv_refresh_date.setText(getSystemTime());
            }
        }
    }

    /**
     * 得到系统时间
     * @return
     */
    private String getSystemTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 定义接口
     *
     */
    public interface OnRefreshListener
    {
        public void onPullDownRefresh();
        public void onLoad();
    }

    private OnRefreshListener onRefreshListener;

    /**
     * lhd
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener)
    {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * 更新刷新状态
     */

    private void refreshHeaderViewStatus()
    {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH :
                tv_refresh_pull.setText("下拉刷新...");
                iv_refresh_header.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH :
                tv_refresh_pull.setText("手松刷新...");
                iv_refresh_header.startAnimation(upAnimation);
                break;
            case REFRESHING :

                iv_refresh_header.clearAnimation();
                iv_refresh_header.setVisibility(View.GONE);
                tv_refresh_pull.setText("正在刷新...");
                pb_regresh_header.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean isDisplayTopNews()
    {
        int[] location = new int[2];


        if (mListViewOnScreenY == -1)
        {
            this.getLocationOnScreen(location);
            mListViewOnScreenY = location[1];
        }

        topViewPager.getLocationOnScreen(location);
        topViewPagerOnScreenY = location[1];

        return mListViewOnScreenY <= topViewPagerOnScreenY;
    }

    public void addTopNews(View topViewPager)
    {
        this.topViewPager = topViewPager;
        topViews.addView(topViewPager);
    }
}

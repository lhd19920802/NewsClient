package com.lhd.news.pager.detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lhd.news.R;
import com.lhd.news.base.MenuDetailBasePager;
import com.lhd.news.bean.NewsCenterBean;
import com.lhd.news.bean.TabDetailPagerBean;
import com.lhd.news.utils.CacheUtils;
import com.lhd.news.utils.ConstantUtils;
import com.lhd.news.utils.DensityUtil;
import com.lhd.news.view.HorizontalScrollViewPager;
import com.lhd.news.view.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by lihuaidong on 2017/12/3 18:12.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：页签页面
 */
public class TabDetailPager extends MenuDetailBasePager
{
    private static final String TAG = TabDetailPager.class.getSimpleName();
    //每个页签页面的动态数据
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    @ViewInject(R.id.vp_tab_detail)
    private HorizontalScrollViewPager vp_tab_detail;
    @ViewInject(R.id.tv_tab_detail)
    private TextView tv_tab_detail;
    @ViewInject(R.id.ll_tab_detail)
    private LinearLayout ll_tab_detail;

    @ViewInject(R.id.lv_tab_detail)
    private RefreshListView lv_tab_detail;
    //顶部新闻的数据
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    //记录上一次页面的位置
    private int prePosition;

    //列表新闻的数据
    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private String more;
    //加载更多的链接
    private String moreUrl;

    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;
    private MyBaseAdapter myBaseAdapter;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean)
    {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView()
    {
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        x.view().inject(this, view);

        View headerView = View.inflate(mContext, R.layout.tab_listview_header, null);
        x.view().inject(this, headerView);

//        lv_tab_detail.addHeaderView(headerView);

        //把顶部轮播图的实例传给ListView
        lv_tab_detail.addTopNews(headerView);

        //设置刷新的监听
        lv_tab_detail.setOnRefreshListener(new MyOnRefreshListener());
        return view;
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener
    {

        @Override
        public void onPullDownRefresh()
        {
            getDataFromNet();
        }

        @Override
        public void onLoad()
        {
            if(TextUtils.isEmpty(moreUrl)) {
                lv_tab_detail.onFinshRefresh(false);
                isLoadMore=false;
            }else
            {

                getMoreDataFromNet();
            }
        }


    }

    private void getMoreDataFromNet()
    {
        RequestParams entity = new RequestParams(moreUrl);
        entity.setConnectTimeout(5000);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e(TAG, "联网成功==" + result);
                CacheUtils.putString(mContext, ConstantUtils.BASE_URL + childrenBean.getUrl(),
                        result);
                //解析并显示数据
                processData(result);
                isLoadMore=true;
                //联网成功后状态还原
                lv_tab_detail.onFinshRefresh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e(TAG, "联网失败==" + ex.getMessage());

                //联网失败后状态还原
                lv_tab_detail.onFinshRefresh(false);

            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                Log.e(TAG, "onCancelled==" + cex.getMessage());

            }

            @Override
            public void onFinished()
            {

                Log.e(TAG, "onFinished==");
            }
        });
    }

    @Override
    public void initData()
    {
        super.initData();

        String saveJson = CacheUtils.getString(mContext, ConstantUtils.BASE_URL + childrenBean
                .getUrl());
        if (!TextUtils.isEmpty(saveJson))
        {
            processData(saveJson);
        }
        getDataFromNet();

    }

    private void getDataFromNet()
    {
        RequestParams entity = new RequestParams(ConstantUtils.BASE_URL + childrenBean.getUrl());
        entity.setConnectTimeout(5000);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e(TAG, "联网成功==" + result);
                CacheUtils.putString(mContext, ConstantUtils.BASE_URL + childrenBean.getUrl(),
                        result);
                //解析并显示数据
                processData(result);

                //联网成功后状态还原
                lv_tab_detail.onFinshRefresh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e(TAG, "联网失败==" + ex.getMessage());

                //联网失败后状态还原
                lv_tab_detail.onFinshRefresh(false);

            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                Log.e(TAG, "onCancelled==" + cex.getMessage());

            }

            @Override
            public void onFinished()
            {

                Log.e(TAG, "onFinished==");
            }
        });
    }

    private void processData(String json)
    {
        TabDetailPagerBean tabDetailPagerBean = parseJson(json);
        Log.e(TAG, "数据解析成功===" + tabDetailPagerBean.getData().getTopnews().get(1).getTitle());


        //加载更多的链接
        more = tabDetailPagerBean.getData().getMore();
        if(TextUtils.isEmpty(more)) {
            moreUrl = "";
        }
        else
        {
            moreUrl=ConstantUtils.BASE_URL+more;
        }

        if(!isLoadMore) {
            topnews = tabDetailPagerBean.getData().getTopnews();
            //设置viewPager适配器
            vp_tab_detail.setAdapter(new TabDetailPagerAdapter());
            //根据数据添加红点

            addPoint();

            //监听页面的变化 改变文字和红点
            vp_tab_detail.addOnPageChangeListener(new MyOnPageChangeListener());

            tv_tab_detail.setText(topnews.get(prePosition).getTitle());


            //列表数据
            news = tabDetailPagerBean.getData().getNews();
            myBaseAdapter = new MyBaseAdapter();
            lv_tab_detail.setAdapter(myBaseAdapter);
        }
        else
        {
            isLoadMore=false;

            //将更多数据添加到原来的集合中  tabDetailPagerBean.getData().getNews()是使用moreUrl获取到的数据
            news.addAll(tabDetailPagerBean.getData().getNews());
                //刷新适配器显示数据
            myBaseAdapter.notifyDataSetChanged();
        }

    }


    class MyBaseAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return news.size();
        }

        @Override
        public Object getItem(int position)
        {
            return news.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder;
            if(convertView==null)
            {
                viewHolder=new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_list, null);
                viewHolder.iv_item_list = (ImageView) convertView.findViewById(R.id.iv_item_list);
                viewHolder.tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
                viewHolder.tv_item_date = (TextView) convertView.findViewById(R.id.tv_item_date);

                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tv_item_title.setText(newsBean.getTitle());
            viewHolder.tv_item_date.setText(newsBean.getPubdate());

            viewHolder.iv_item_list.setBackgroundResource(R.drawable.news_pic_default);
            String imageUrl = newsBean.getListimage().replace("10.0.2.2", "192.168.191.1");
            x.image().bind(viewHolder.iv_item_list,imageUrl);


            return convertView;
        }
    }

    static class ViewHolder
    {
        ImageView iv_item_list;
        TextView tv_item_title;
        TextView tv_item_date;
    }

    private void addPoint()
    {
        //processData调用两次 所以linearlayout要先移除所有再添加

        ll_tab_detail.removeAllViews();
        for (int i = 0; i < topnews.size(); i++)
        {
            ImageView point = new ImageView(mContext);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 8), DensityUtil.dip2px(mContext, 8));
            point.setLayoutParams(params);
            if(i==0)
            {
                point.setEnabled(true);
            }
            else
            {
                point.setEnabled(false);
                params.leftMargin=DensityUtil.dip2px(mContext, 8);
            }


            ll_tab_detail.addView(point);
        }
    }



    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            tv_tab_detail.setText(topnews.get(position).getTitle());

            //让上一个页面的点变灰
            ll_tab_detail.getChildAt(prePosition).setEnabled(false);

            //让当前页面的点变红
            ll_tab_detail.getChildAt(position).setEnabled(true);
            //保存当前页面的位置
            prePosition = position;


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

    class TabDetailPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            String topimage = topnews.get(position).getTopimage();
            String topimageUrl = topimage.replace("10.0.2.2", "192.168.191.1");

            Log.e(TAG, "topimageUrl===" + topimageUrl);
            x.image().bind(imageView, topimageUrl);


            container.addView(imageView);


            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }

    private TabDetailPagerBean parseJson(String json)
    {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }
}

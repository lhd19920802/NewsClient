package com.lhd.news.pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lhd.news.MainActivity;
import com.lhd.news.base.BasePager;
import com.lhd.news.base.MenuDetailBasePager;
import com.lhd.news.bean.NewsCenterBean;
import com.lhd.news.fragment.LeftMenuFragment;
import com.lhd.news.pager.detail.InteracDetailPager;
import com.lhd.news.pager.detail.NewsDetailPager;
import com.lhd.news.pager.detail.PhotosDetailPager;
import com.lhd.news.pager.detail.TopicDetailPager;
import com.lhd.news.utils.CacheUtils;
import com.lhd.news.utils.ConstantUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihuaidong on 2017/11/30 15:01.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class NewsCenterPager extends BasePager
{
    private static final String TAG = NewsCenterPager.class.getSimpleName();
    //左侧菜单的数据
    private List<NewsCenterBean.DataBean> leftMenuData;
    //左侧菜单对应的详情页面
    private List<MenuDetailBasePager> menuDetailBasePagerList;

    public NewsCenterPager(Context context)
    {
        super(context);
    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e(TAG, "新闻中心的数据被初始化了");
        tv_titlebar.setText("新闻中心");
        ib_titlebar.setVisibility(View.VISIBLE);
        ib_titlebar.setOnClickListener(new MyOnClickListener());

        String json = CacheUtils.getString(mContext, ConstantUtils.NEWSCENTER_URL);
        if (!TextUtils.isEmpty(json))
        {
            processData(json);
        }
        getDataFromNet();

    }

    class MyOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.getSlidingMenu().toggle();
        }
    }


    private void getDataFromNet()
    {
        RequestParams entity = new RequestParams(ConstantUtils.NEWSCENTER_URL);
        x.http().get(entity, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e(TAG, "网络请求成功==" + result);

                //数据缓存
                CacheUtils.putString(mContext, ConstantUtils.NEWSCENTER_URL, result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e(TAG, "网络请求错误==" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                Log.e(TAG, "网络请求错误onCancelled()" + cex.getMessage());

            }

            @Override
            public void onFinished()
            {
                Log.e(TAG, "onFinished()");
            }
        });
    }

    private void processData(String json)
    {
        NewsCenterBean newsCenterBean = parseJson(json);
        Log.e(TAG, "使用Gson解析成功了==" + newsCenterBean.getData().get(0).getChildren().get(2)
                .getTitle());
        //左侧菜单的数据
        leftMenuData = newsCenterBean.getData();
        menuDetailBasePagerList = new ArrayList<>();
        //将四个详情页面添加到list中
        MainActivity mainActivity = (MainActivity) mContext;
        menuDetailBasePagerList.add(new NewsDetailPager(mainActivity, leftMenuData.get(0)
                .getChildren()));
        menuDetailBasePagerList.add(new TopicDetailPager(mainActivity, leftMenuData.get(0).getChildren()));
        menuDetailBasePagerList.add(new PhotosDetailPager(mainActivity));
        menuDetailBasePagerList.add(new InteracDetailPager(mainActivity));

        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //将数据利用方法传递给左侧页面
        leftMenuFragment.setData(leftMenuData);

    }

    private NewsCenterBean parseJson(String json)
    {

        return new Gson().fromJson(json, NewsCenterBean.class);
    }

    public void switchPager(int position)
    {

        //设置标题
        tv_titlebar.setText(leftMenuData.get(position).getTitle());
        MenuDetailBasePager menuDetailBasePager = menuDetailBasePagerList.get(position);
        View rootView = menuDetailBasePager.rootView;
        menuDetailBasePager.initData();

        fl_basepager.removeAllViews();
        fl_basepager.addView(rootView);
    }
}

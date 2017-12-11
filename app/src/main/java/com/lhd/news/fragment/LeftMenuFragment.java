package com.lhd.news.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lhd.news.MainActivity;
import com.lhd.news.R;
import com.lhd.news.base.BaseFragment;
import com.lhd.news.bean.NewsCenterBean;
import com.lhd.news.pager.NewsCenterPager;
import com.lhd.news.utils.DensityUtil;

import java.util.List;

/**
 * Created by lihuaidong on 2017/11/30 9:52.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class LeftMenuFragment extends BaseFragment
{

    private List<NewsCenterBean.DataBean> leftMenuData;

    private ListView listView;

    private int selectPosition;
    private MyBaseAdapter myBaseAdapter;

    @Override
    public View initView()
    {
        listView = new ListView(mContext);
        listView.setPadding(0, DensityUtil.dip2px(mContext, 40), 0, 0);
        listView.setBackgroundColor(Color.BLACK);//设置ListView的背景
        listView.setDividerHeight(0);
        listView.setSelector(android.R.color.transparent);//把ListVeiw中某一个条按下效果屏幕
        return listView;

    }

    @Override
    public void initData()
    {
        super.initData();
        //        textView.setText("我是左侧菜单页面");
        System.out.println("左侧菜单页面的数据被初始化了");

        //设置item的点击监听
        listView.setOnItemClickListener(new MyOnItemClickListener());

    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //让item变色
            selectPosition = position;
            myBaseAdapter.notifyDataSetChanged();//调用getCount--getView方法

            //点击item后自动收起菜单
            MainActivity mainActivity= (MainActivity) mContext;
            mainActivity.getSlidingMenu().toggle();

            //切换页面
            switchPager(selectPosition);
        }
    }

    private void switchPager(int position)
    {
        MainActivity mainActivity = (MainActivity) mContext;
        NewsCenterPager newsCenterPager = mainActivity.getContentFragment().getNewsCenterPager();
        newsCenterPager.switchPager(position);
    }

    public void setData(List<NewsCenterBean.DataBean> leftMenuData)
    {
        this.leftMenuData = leftMenuData;

        myBaseAdapter = new MyBaseAdapter();
        listView.setAdapter(myBaseAdapter);

        //设置打开菜单时有默认选中的状态
        switchPager(selectPosition);
    }

    class MyBaseAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return leftMenuData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return leftMenuData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            TextView textView = (TextView) View.inflate(mContext, R.layout.left_menu_item, null);
            textView.setText(leftMenuData.get(position).getTitle());

            if (selectPosition == position)
            {
                textView.setEnabled(true);
            }
            return textView;
        }
    }
}

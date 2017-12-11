package com.lhd.news;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lhd.news.fragment.ContentFragment;
import com.lhd.news.fragment.LeftMenuFragment;
import com.lhd.news.utils.DensityUtil;
import com.lhd.news.utils.SystemBarTintManager;

public class MainActivity extends SlidingFragmentActivity
{

    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String MAIN_TAG = "main_tag";
    //标记是否退出
    private boolean isExit = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        { //系统版本大于19
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.common_title_bg);//设置标题栏颜色，此颜色在color中声明
        setBehindContentView(R.layout.left_menu);


        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        slidingMenu.setBehindOffset(DensityUtil.dip2px(this, 200));
        initFragments();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on)
    {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
        {
            winParams.flags |= bits;        // a|=b的意思就是把a和b按位或然后赋值给a
            // 按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
        }
        else
        {
            winParams.flags &= ~bits;        //&是位运算里面，与运算  a&=b相当于 a = a&b  ~非运算符
        }
        win.setAttributes(winParams);


    }

    private void initFragments()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG);
        ft.replace(R.id.fl_main, new ContentFragment(), MAIN_TAG);

        ft.commit();
    }

    //得到左侧菜单LeftMenuFragment

    public LeftMenuFragment getLeftMenuFragment()
    {

        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);

    }


    //得到正文ContentFragment
    public ContentFragment getContentFragment()
    {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_TAG);
    }

    //点击两次返回键退出应用


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (!isExit)
        {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    isExit = false;
                }
            }, 2000);

            return true;
        }


        //退出应用
        return super.onKeyUp(keyCode, event);
    }


}

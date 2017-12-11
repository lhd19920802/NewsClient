package com.lhd.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.lhd.news.utils.CacheUtils;

public class SplashActivity extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //设置欢迎动画
        startAnimation();
    }

    private void startAnimation()
    {
        //开启动画
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        aa.setFillAfter(true);
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(sa);
        animationSet.addAnimation(aa);
        animationSet.addAnimation(ra);

        findViewById(R.id.rl_main).startAnimation(animationSet);

        //监听动画播放完成，进入向导页面
        animationSet.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener
    {

        @Override
        public void onAnimationStart(Animation animation)
        {

        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            //进入向导页面
            boolean isEnter = CacheUtils.getBoolean(SplashActivity.this, "isEnter");
            if(isEnter)
            {
                //进入主页面
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                startActivity(intent);
            }


            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {

        }
    }
}

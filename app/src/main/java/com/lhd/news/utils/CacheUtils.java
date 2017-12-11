package com.lhd.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lihuaidong on 2017/11/28 17:19.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class CacheUtils
{
    public static void putBoolean(Context context,String key,boolean value)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
    public static boolean getBoolean(Context context,String key)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putString(Context context, String key, String value)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
    public static String getString(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}

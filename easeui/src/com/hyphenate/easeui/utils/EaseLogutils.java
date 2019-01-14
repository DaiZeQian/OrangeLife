package com.hyphenate.easeui.utils;

import android.util.Log;

/**
 * Created by admin on 2016/6/17.
 */
public class EaseLogutils {


    public static boolean isOpenDebug = true;
    public static boolean isOpenWarn = true;

    public static void d(String tag, String msg) {
        if (isOpenDebug) {
            // Log.d(ManagerUtils.getCurrentClassName()+"_"+ManagerUtils.getCurrentMethodName()+"_"+tag, msg);
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isOpenWarn) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isOpenWarn) {
            Log.e(tag, msg);
        }
    }


    public static void server(String tag, String msg) {
        if (isOpenDebug) {
            Log.d("server_" + tag, msg);
        }
    }
}

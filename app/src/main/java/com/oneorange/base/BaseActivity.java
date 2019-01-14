package com.oneorange.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.oneorange.utils.LogUtil;
import com.oneorange.utils.NetWorkUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.volley.RequestQueue;

import java.util.Stack;

/**
 * Created by admin on 2016/3/21.
 * <p>
 * 基础框架
 */
public class BaseActivity extends FragmentActivity {


    /*activity 生命周期*/
    private static final String TAG = "ActivityLife";
    /**
     * 用来保存一打开的Activity
     */
    private static Stack<Activity> onLineActivityList = new Stack<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LogUtil.d(TAG, getClass().getSimpleName() + "onCreate");
        onLineActivityList.push(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG, getClass().getSimpleName() + "onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, getClass().getSimpleName() + "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, getClass().getSimpleName() + "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG, getClass().getSimpleName() + "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, getClass().getSimpleName() + "onDestory");
        if (onLineActivityList.contains(this)) {
            onLineActivityList.remove(this);
        }
    }

    /**
     * 关闭前台所有的acitivity
     */
    protected static void finishall() {
        int len = onLineActivityList.size();
        for (int i = 0; i < len; i++) {
            Activity activity = onLineActivityList.pop();
            activity.finish();
        }
    }


    /**
     * 获取BaseApplication类中的唯一一个请求队列(Volley)
     */
    public RequestQueue getVolleyRequestQueue() {
        BaseApplication application = (BaseApplication) getApplication();
        return application.getRequestQueue();
    }

    /**---------------Activity启动控制------------------**/
    /**
     * open activity without data
     *
     * @param targetActivityClass
     */
    public void openActivity(Class<?> targetActivityClass) {
        openActivity(targetActivityClass, null);
    }

    /**
     * open  activity with  data
     *
     * @param targetActivityClass
     * @param bundle
     */
    public void openActivity(Class<?> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(this, targetActivityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * open activity and  close this activity with data
     *
     * @param targetActivityClass
     * @param bundle
     */
    public void openActivityAndCloseThis(Class<?> targetActivityClass, Bundle bundle) {
        openActivity(targetActivityClass, bundle);
        this.finish();
    }

    /**
     * open activity and close this  activity without data
     *
     * @param targetActivityClass
     */
    public void openActivityAndCloseThis(Class<?> targetActivityClass) {
        openActivity(targetActivityClass);
        this.finish();
    }


    /**
     * 判断是否有网络并打开
     *
     * @param targetActivityClass
     */
    public void isNetWorkOpenActivity(Class<?> targetActivityClass, String tip) {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            openActivity(targetActivityClass);
        } else {
            if (tip != null) {
                ToastUtil.show(this, tip, 200);
            }
        }
    }

    public void isNetWorkOpenActivity(Class<?> targetActivityClass) {
        isNetWorkOpenActivity(targetActivityClass, null, null);
    }


    public void isNetWorkOpenActivity(Class<?> targetActivityClass, Bundle bundle, String tip) {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            openActivity(targetActivityClass, bundle);
        } else {
            if (tip != null) {
                ToastUtil.show(this, tip, 200);
            }
        }
    }


    public void isNetWorkOpenActivity(Class<?> targetActivityClass, Bundle bundle) {
        isNetWorkOpenActivity(targetActivityClass, bundle, null);
    }

    /**
     * 判断是否有网络打开并关闭
     *
     * @param targetActivityClass
     */
    public void isNetWorkOpenAndCloseActivity(Class<?> targetActivityClass, String tip) {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            openActivityAndCloseThis(targetActivityClass);
            this.finish();
        } else {
            if (tip != null) {
                ToastUtil.show(this, tip, 200);
            }
        }
    }

    public void isNetWorkOpenAndCloseActivity(Class<?> targetActivityClass) {
        isNetWorkOpenAndCloseActivity(targetActivityClass, null);
    }


    /**
     * -------------fragment管理----------------*
     */
    private Fragment currentFragment;

    /**
     * smart Fragment replace (隐藏当前的 ，显示现在的，用过的将不会destory与create)
     *
     * @param target     tag值
     * @param toFragment 目标fragment
     */
    public void smartFragmentReplace(int target, Fragment toFragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //如有当前在使用的->隐藏当前的
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        String toClassName = toFragment.getClass().getSimpleName();
        //toFragment 之前添加使用过->显示出来
        if (manager.findFragmentByTag(toClassName) != null) {
            transaction.show(toFragment);
        } else {//toFragment 还没有添加使用过->添加上去
            transaction.add(target, toFragment, toClassName);
        }
        transaction.commit();
        currentFragment = toFragment;
    }

    /**
     * Fragment 替换 当前的destory 新的creat
     *
     * @param target
     * @param toFragment
     * @param backStack
     */
    public void replaceFragment(int target, Fragment toFragment, boolean backStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String toClassName = currentFragment.getClass().getSimpleName();
        if (manager.findFragmentByTag(toClassName) == null) {
            transaction.replace(target, toFragment, toClassName);
            if (backStack) {
                transaction.addToBackStack(toClassName);
            }
            transaction.commit();
        }
    }


}

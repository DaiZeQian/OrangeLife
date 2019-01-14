package com.oneorange.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneorange.utils.LogUtil;
import com.oneorange.volley.RequestQueue;


/**
 * Created by admin on 2016/3/21.
 * Fragment 基类
 */
public abstract class   BaseFragment extends Fragment {

    private String TAG = "FragmentLife";

    private View currentView;

    private FragmentItemOnclickListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        String fName = getClass().getSimpleName();//此fragment名
        String aName = activity.getClass().getSimpleName();//引用此fragment的activity的类名
        LogUtil.d(TAG, fName + "onAttach() 到" + aName);
        if (activity instanceof FragmentItemOnclickListener) {
            listener = (FragmentItemOnclickListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (currentView == null) {
            currentView = getAndInitView(inflater, container, savedInstanceState);
            laodData();
            LogUtil.d(TAG, getClass().getSimpleName() + "onCreateView()->getAndInitView() AND loadData()");
        }
        resetData();
        LogUtil.d(TAG, getClass().getSimpleName() + "onCreateView -> resetData()");
        return currentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(TAG, getClass().getSimpleName() + "onStart");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resetData();
            LogUtil.d(TAG, getClass().getSimpleName() + "onHiddenChanged()[show]->resetData()");
            onResume();
        } else {
            onPause();
        }
    }


    /**
     * 获取BaseApplication类中的唯一一个请求队列(Volley)
     */
    public RequestQueue getVolleyRequestQueue() {
        BaseApplication application = (BaseApplication) getActivity().getApplication();
        return application.getRequestQueue();
    }

    /**
     * 当前Fragment的布局，只有在第一次create的时候才会触发
     *
     * @param inflater
     * @param container
     * @param saveInstanceState
     * @return
     */
    public abstract View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState);

    /**
     * 当前Fragment的数据加载，只有在第一次create时才会触发
     */
    public abstract void laodData();

    /**
     * 当前Fragment的布局数据重置，每次onCreatView 和onHiddenChanged(false) 都会触发
     */
    public abstract void resetData();

    protected void callbackByFragmentId(int eventTag) {
        if (listener != null) {
            listener.onFragmentItemClick(this.getClass().getSimpleName(), eventTag, null);
        }
    }

    protected void callbackByFragmentId(int eventTag, Object data) {
        if (listener != null) {
            listener.onFragmentItemClick(this.getClass().getSimpleName(), eventTag, data);
        }
    }


    protected void callbackByFragmentId(String tag, int eventTag, Object data) {
        if (listener != null) {
            listener.onFragmentItemClick(tag, eventTag, data);
        }
    }


    public static interface FragmentItemOnclickListener {
        /**
         * 点击当前fragment上组件掉用
         *
         * @param tag      当前fragment的标签，默认dagnqianfragment的类名
         * @param eventTag 当前事件标签
         * @param data     携带数据，无数据可以为null
         */
        public void onFragmentItemClick(String tag, int eventTag, Object data);
    }
}

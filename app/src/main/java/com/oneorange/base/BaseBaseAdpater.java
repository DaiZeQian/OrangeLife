package com.oneorange.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/21.
 * 基础适配器 整合适用于常规listview与gridview使用
 */
public abstract class BaseBaseAdpater<E> extends BaseAdapter {

    public ArrayList<E> adapterDatas = new ArrayList<>();
    protected LayoutInflater layoutInflater;//布局加载器
    protected Context context;

    public BaseBaseAdpater(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<E> getAdapterDatas() {
        return adapterDatas;
    }


    public void clearAdapterData() {
        adapterDatas.clear();
    }

    /**
     * 添加数据
     *
     * @param e
     */
    public void addDatatoAdapter(E e) {
        if (e != null) {
            adapterDatas.add(e);
        }
    }
    public void addDatatoAdapter(int index,E e) {
        if (e != null) {
            adapterDatas.add(index,e);
        }
    }
    public void addDatatoAdapter(List<E> e) {
        if (e != null) {
            adapterDatas.addAll(e);
        }
    }

    public void addDatatoAdapterHeader(List<E> e) {
        if (e != null) {
            adapterDatas.addAll(0, e);
        }
    }

    public void addDatatoAdapterEnd(List<E> e) {
        if (e != null) {
            adapterDatas.addAll(adapterDatas.size(), e);
        }
    }

    public void setAdapterDatas(List<E> e) {
        adapterDatas.clear();
        if (e != null) {
            adapterDatas.addAll(e);
        }
    }

    public void removeDataFromAdapter(E e) {
        adapterDatas.remove(e);
    }

    public void removeDataFromAdapter(int index) {
        adapterDatas.remove(index);
    }


    @Override
    public int getCount() {
        return adapterDatas == null ? 0 : adapterDatas.size();
    }

    @Override
    public E getItem(int position) {
        return adapterDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}

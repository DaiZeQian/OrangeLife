package com.oneorange.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 2015/8/12.
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> titles = new ArrayList<String>();

    private FragmentManager fm;

    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragmentToAdapter(Fragment fragment) {
        if (fragment != null) {
            fragments.add(fragment);
        }
    }

    public void addFragmentToAdapter(String title, Fragment fragment) {
        if (fragment != null) {
            fragments.add(fragment);
        }
        if (title != null) {
            titles.add(title);
        }
    }


    public void setFragmentToAdapter(Fragment fragment, int position) {
        if (fragment != null) {
            fragments.set(position, fragment);
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return fragments.get(position);
    }

    //默认返回return POSITION_UNCHANGED  为了让Fragment重新绘制，必须重载FragmentPagerAdapter的getItemPositon方法并修改为return POSITION_NONE。这样之前所有的Fragment都会被detach掉。
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragments.size();
    }
}

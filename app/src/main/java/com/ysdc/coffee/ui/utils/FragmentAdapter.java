package com.ysdc.coffee.ui.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ysdc.coffee.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final List<BaseFragment> fragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Our custom method that populates this Adapter with Fragments
    public void addFragments(BaseFragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public BaseFragment getItemAtPosition(int position) {
        return fragments.get(position);
    }

}
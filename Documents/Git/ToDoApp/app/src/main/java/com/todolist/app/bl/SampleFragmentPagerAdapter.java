package com.todolist.app.bl;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.todolist.app.fragments.AllTasksFragment;
import com.todolist.app.fragments.WaitingTasksFragment;

/**
 * Created by Hila on 3/1/16.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    public static final String ARG_PAGE = "ARG_PAGE";
    private String tabTitles[] = new String[] { "Waiting","all"};
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WaitingTasksFragment();
            case 1:
                return  new AllTasksFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
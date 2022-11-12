package com.folkus.ui.login.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.folkus.ui.login.fragments.CompletedInspectionsFragment;
import com.folkus.ui.login.fragments.PendingInspectionsFragment;

public class TabLayoutAdapter extends FragmentStatePagerAdapter {

    int mTotalTabs;

    public TabLayoutAdapter(FragmentManager fragment, int totalTabs) {
        super(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mTotalTabs = totalTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PendingInspectionsFragment();
            case 1:
                return new CompletedInspectionsFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}

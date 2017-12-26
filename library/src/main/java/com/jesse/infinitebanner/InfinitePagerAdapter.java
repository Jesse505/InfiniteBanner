package com.jesse.infinitebanner;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RxRead on 2015/9/24.
 */
public abstract class InfinitePagerAdapter extends RecyclingPagerAdapter {


    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        return null;
    }

    @Override
    /**
     * Note: use getItemCount instead*/
    public final int getCount() {
        return getItemCount() == 1 ? 1 : getItemCount() * InfiniteViewPager.FakePositionHelper.MULTIPLIER;
    }

    @Deprecated

    protected final View getViewInternal(int position, View convertView, ViewGroup container) {
        if (getItemCount() == 0 || getRealItemCount() == 0)
            return null;
        return getView((position % getItemCount()) % getRealItemCount(), convertView, container);
    }

    public final int getItemCount() {
        if (getRealItemCount() > 1 && getRealItemCount() < 4) {
            return getRealItemCount() * 10;
        } else {
            return getRealItemCount();
        }
    }

    public abstract int getRealItemCount();

}

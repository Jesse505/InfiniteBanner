package com.jesse.infinitebanner.demo.infinitebanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jesse.infinitebanner.InfinitePagerAdapter;

import java.util.List;

/**
 * 文件描述
 * 创建人：zhaoyf
 * 创建时间：2017/12/15
 */

public class HomePageTopViewPagerAdapter extends InfinitePagerAdapter {
    List<Integer> bannerList;
    LayoutInflater layoutInflater;

    public HomePageTopViewPagerAdapter(List<Integer> bannerList, Context context) {
        this.bannerList = bannerList;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getRealItemCount() {
        return (null != bannerList && bannerList.size() > 0) ? bannerList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder;
        if (null != convertView) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(R.layout.layout_banner_item, container, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        if (bannerList != null && bannerList.size() > 0) {
            viewHolder.position = position;
            viewHolder.imageView.setImageResource(bannerList.get(position));
        }
        return convertView;
    }

    private static class ViewHolder {
        public int position;
        ImageView imageView;

        public ViewHolder(View view) {
            this.imageView = (ImageView) view.findViewById(R.id.banner_iv);
        }
    }
}

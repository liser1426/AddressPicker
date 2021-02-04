package com.example.addressproject;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/29
 * Time: 下午6:30
 */
class ViewPagerAdapter  extends PagerAdapter {

    private List<String> titleNames;
    private List<View> views;

    public ViewPagerAdapter(List<String> titleNames,  List<View> views){
        this.titleNames = titleNames;
        this.views = views;
    }

    @Override
    public int getCount() {
        return titleNames.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
        super.destroyItem(container, position, object);
    }
}

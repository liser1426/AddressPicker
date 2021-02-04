package com.example.addressproject;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/27
 * Time: 下午1:44
 */
class CommonHeadSection<T extends FirstLetterBean> extends SectionEntity<T> {

    private int position;


    public CommonHeadSection(boolean isHeader, String header, int position) {
        super(isHeader, header);
        this.position = position;
    }

    public CommonHeadSection(T t) {
        super(t);
    }


    public int getPosition() {
        return position;
    }


}

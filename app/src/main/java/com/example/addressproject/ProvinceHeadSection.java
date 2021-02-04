package com.example.addressproject;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/28
 * Time: 上午11:31
 */
class ProvinceHeadSection extends SectionEntity<AddressCountryBean.ProvinceBean> {

    private int position;


    public ProvinceHeadSection(boolean isHeader, String header ,int position) {
        super(isHeader, header);
        this.position = position;
    }

    public ProvinceHeadSection(AddressCountryBean.ProvinceBean provinceBean) {
        super(provinceBean);
    }

    public int getPosition() {
        return position;
    }
}

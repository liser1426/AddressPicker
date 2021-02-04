package com.example.addressproject;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/28
 * Time: 下午2:51
 */
class CityHeadSection extends SectionEntity<AddressCountryBean.ProvinceBean.CityBean> {

    private int position;

    public CityHeadSection(boolean isHeader, String header,int position) {
        super(isHeader, header);
        this.position = position;
    }

    public CityHeadSection(AddressCountryBean.ProvinceBean.CityBean cityBean) {
        super(cityBean);
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

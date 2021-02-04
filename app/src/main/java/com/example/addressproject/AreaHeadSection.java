package com.example.addressproject;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/28
 * Time: 下午2:51
 */
class AreaHeadSection extends SectionEntity<AddressCountryBean.ProvinceBean.CityBean.AreaBean> {

    private int position;

    public AreaHeadSection(boolean isHeader, String header, int position) {
        super(isHeader, header);
        this.position = position;
    }

    public AreaHeadSection(AddressCountryBean.ProvinceBean.CityBean.AreaBean areaBean) {
        super(areaBean);
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

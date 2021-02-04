package com.example.addressproject;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/27
 * Time: 下午1:44
 */
class CountryHeadSection extends SectionEntity<AddressCountryBean> {

    private int position;


    public CountryHeadSection(boolean isHeader, String header, int position) {
        super(isHeader, header);
        this.position = position;
    }

    public CountryHeadSection(AddressCountryBean addressCountryBean) {
        super(addressCountryBean);
    }


    public int getPosition() {
        return position;
    }


}

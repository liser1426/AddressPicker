package com.example.addressproject;

import android.graphics.Color;
import android.util.Log;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ProvinceAdapter extends BaseSectionQuickAdapter<ProvinceHeadSection, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public ProvinceAdapter(int layoutResId, int sectionHeadResId, List<ProvinceHeadSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ProvinceHeadSection item) {
        helper.setText(R.id.letter_tv,item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProvinceHeadSection item) {
        Log.e("item------>",item.toString());
        AddressCountryBean.ProvinceBean addressBean = item.t;
        helper.setText(R.id.textview, addressBean.getLabel());
        helper.setTextColor(R.id.textview, addressBean.isStatus() ? Color.parseColor("#65C15C") : Color.parseColor("#444444"));

    }
}

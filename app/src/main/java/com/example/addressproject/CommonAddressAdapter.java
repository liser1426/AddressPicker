package com.example.addressproject;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CommonAddressAdapter<T extends FirstLetterBean> extends BaseSectionQuickAdapter<CommonHeadSection<T>, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public CommonAddressAdapter(int layoutResId, int sectionHeadResId, List<CommonHeadSection<T>> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, CommonHeadSection<T> item) {
        helper.setText(R.id.letter_tv, item.header + helper.getLayoutPosition());

    }


    @Override
    protected void convert(BaseViewHolder helper, CommonHeadSection<T> item) {
        T addressCountryBean = item.t;
        helper.setText(R.id.textview, addressCountryBean.getLabel() + helper.getLayoutPosition());
        helper.setTextColor(R.id.textview, addressCountryBean.isStatus() ? Color.parseColor("#65C15C") : Color.parseColor("#444444"));

    }
}

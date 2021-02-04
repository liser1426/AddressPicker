package com.example.addressproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class AddressPickerDialog extends DialogFragment {

    private Context mContext;
    private SlidingTabLayout mSegmentTabLayout;

    private String[] tabTitles = new String[]{"中国地区", "其他国家地区"};
    private List<Fragment> fragments = new ArrayList<>();
    private CustomViewPager mViewPager;
    private ImageView mBackIv;
//    public AreaPickerView3(@NonNull Context context) {
//        super(context);
//    }
//
//    public AreaPickerView3(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//        this.mContext = context;
//    }
//
//    protected AreaPickerView3(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

    private PickerAddressFragment mChinaFragment;
    private PickerAddressFragment mOtherFragment;
    private AddressModel mAddressModel;
    private AddressSelectResultBean resultBean;
    private View rootView;
    private  List<AddressCountryBean>  countryBeans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mAddressModel = new AddressModel();
        //Todo  获取数据处理放到子线程中
        countryBeans = mAddressModel.getCountryData(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater,container,savedInstanceState);
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.picker_address_fragment, container, false);
            mSegmentTabLayout = rootView.findViewById(R.id.sg_tab);
            mViewPager = rootView.findViewById(R.id.viewpager);
            mBackIv = rootView.findViewById(R.id.iv_btn);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        mSegmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initView() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCanPageScroll(false);
        mSegmentTabLayout.setViewPager(mViewPager,tabTitles);
    }

    private void initData() {
        //Todo 根据地址情况，决定先展示哪个页面
        if(mChinaFragment  == null && mOtherFragment == null) {
            mChinaFragment = new PickerAddressFragment(getContext(), countryBeans, resultBean);
//        mChinaFragment.setSelect(resultBean);
            mOtherFragment = new PickerAddressFragment(getContext(), countryBeans, resultBean);
//        mOtherFragment.setSelect(resultBean);
            fragments.add(mChinaFragment);
            fragments.add(mOtherFragment);
        }else{
            mChinaFragment.setAddressSelectResultBean(resultBean);
            mOtherFragment.setAddressSelectResultBean(resultBean);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params =  getDialog().getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
//        window.setLayout(-1,-2);
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setWindowAnimations(R.style.PickerAnim);
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout(dm.widthPixels, dp2px(getContext(),500));
    }


    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setResultBean(AddressSelectResultBean resultBean) {
        this.resultBean = resultBean;
    }


    class ViewPagerAdapter  extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}

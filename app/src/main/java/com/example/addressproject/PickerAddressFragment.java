package com.example.addressproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.SectionEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/29
 * Time: 上午10:45
 */
@SuppressLint("ValidFragment")
public class PickerAddressFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    /**
     * View的集合
     */
    private List<View> views;
    /**
     * tab的集合
     */
    private List<String> tabNames = new ArrayList<>();
    /**
     * 整体数据集合
     */
    private List<AddressCountryBean> addressOriginData;
    /**
     * 国分组数据或者第一级数据
     */
//    private List<CountryHeadSection> countryHeadSections;
    private List<CommonHeadSection<AddressCountryBean>> countryHeadSections;

    /**
     * 省/二级
     */
//    private List<AddressBean.CityBean> cityBeans;
    private List<CommonHeadSection<AddressCountryBean.ProvinceBean>> provinceHeadSections;
    /**
     * 市/三级
     */
//    private List<AddressBean.CityBean.AreaBean> areaBeans;
    private List<CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean>> cityHeadSections;
    /**
     * 区/四级
     */
    private List<CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean>> areaHeadSections;

    private Context mContext;

    private ViewPagerAdapter viewPagerAdapter;

    private CommonAddressAdapter<AddressCountryBean> countryAdapter;
    private CommonAddressAdapter<AddressCountryBean.ProvinceBean> provinceAdapter;
    private CommonAddressAdapter<AddressCountryBean.ProvinceBean.CityBean> cityAdapter;
    private CommonAddressAdapter<AddressCountryBean.ProvinceBean.CityBean.AreaBean> areaAdapter;

    /**
     * 选中的区域下标 默认-1
     */
    private int countrySelected = -1;
    private int provinceSelected = -1;
    private int citySelected = -1;
    private int areaSelected = -1;

    /**
     * 历史选中的区域下标 默认-1
     */
    private int oldCountrySelected = -1;
    private int oldProvinceSelected = -1;
    private int oldCitySelected = -1;
    private int oldAreaSelected = -1;

    private RecyclerView countryRecyclerView;
    private RecyclerView provinceRecyclerView;
    private RecyclerView cityRecyclerView;
    private RecyclerView areaRecyclerView;

    private AZSideBarView mCountryASBV;
    private AZSideBarView mProvinceASBV;
    private AZSideBarView mCityASBV;
    private AZSideBarView mAreaASBV;

    private boolean isCreate;

    private AddressModel mAddressModel = new AddressModel();
    //地址回调接口
    private AreaPickerViewCallback areaPickerViewCallback;
    private AddressSelectResultBean mAddressSelectResultBean;
//    private  AreaPickerView3 mAddressPickDialog;

    public AddressSelectResultBean getAddressSelectResultBean() {
        return mAddressSelectResultBean;
    }

    public void setAddressSelectResultBean(AddressSelectResultBean addressSelectResultBean) {
        this.mAddressSelectResultBean = addressSelectResultBean;
    }

    @SuppressLint("StaticFieldLeak")
    private volatile static PickerAddressFragment mPickerAddressFragment;

//    public static PickerAddressFragment getInstance(Context context,List<AddressCountryBean> addressOriginData,AddressSelectResultBean resultBean){
//            if(mPickerAddressFragment==null){
//                synchronized (PickerAddressFragment.class){
//                    if(mPickerAddressFragment==null){
//                        return new PickerAddressFragment(context,addressOriginData,resultBean);
//                    }else{
//                        return mPickerAddressFragment;
//                    }
//                }
//            }else{
//                return mPickerAddressFragment;
//            }
//    }


    public PickerAddressFragment(Context context, List<AddressCountryBean> addressOriginData, AddressSelectResultBean resultBean) {
        this.mContext = context;
        this.addressOriginData = addressOriginData;
        this.mAddressSelectResultBean = resultBean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_address, container, false);
//        mAddressPickDialog = (AreaPickerView3) getFragmentManager().findFragmentByTag("pickerAddress");
        mTabLayout = view.findViewById(R.id.tablayout);
        mViewPager = view.findViewById(R.id.viewpager);
        isCreate = true;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initCountry();
        initProvince();
        initCity();
        initArea();
        setSelectNew(mAddressSelectResultBean);
        initListener();
    }

    private void initView() {
        View countryView = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_recyclerview, null, false);
        View provinceView = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_recyclerview, null, false);
        View cityView = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_recyclerview, null, false);
        View areaView = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_recyclerview, null, false);

        countryRecyclerView = countryView.findViewById(R.id.recyclerview);
        provinceRecyclerView = provinceView.findViewById(R.id.recyclerview);
        cityRecyclerView = cityView.findViewById(R.id.recyclerview);
        areaRecyclerView = areaView.findViewById(R.id.recyclerview);

        mCountryASBV = countryView.findViewById(R.id.azs_bv);
        mProvinceASBV = provinceView.findViewById(R.id.azs_bv);
        mCityASBV = cityView.findViewById(R.id.azs_bv);
        mAreaASBV = areaView.findViewById(R.id.azs_bv);

        views = new ArrayList<>();
        views.add(countryView);
        views.add(provinceView);
        views.add(cityView);
        views.add(areaView);

//        tabNames.add("请选择");

        //配置adapter
//        viewPagerAdapter = new ViewPagerAdapter();
//        mViewPager.setAdapter(viewPagerAdapter);
//        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.getTabAt(0).select();

        /**
         * 这句话设置了过后，假如又3个tab 删除第三个 刷新过后 第二个划第三个会有弹性
         * viewPager.setOffscreenPageLimit(2);
         */
    }

    private void initData() {
//        addressOriginData = new ArrayList<>();
//        addressOriginData.addAll(mAddressModel.getCountryData(getContext()));
        countryHeadSections = new ArrayList<>();
        countryHeadSections = mAddressModel.getFirstSection(addressOriginData);
        /**
         * 设置右侧字母索引
         */
        mCountryASBV.initLetters(mAddressModel.getRightLetters(addressOriginData));
    }

    /**
     * 初始化一级地址
     */
    private void initCountry() {
        countryAdapter = new CommonAddressAdapter<AddressCountryBean>(R.layout.item_address, R.layout.item_head, countryHeadSections);
        countryRecyclerView.setAdapter(countryAdapter);
        LinearLayoutManager provinceManager = new LinearLayoutManager(mContext);
        countryRecyclerView.setLayoutManager(provinceManager);
        countryRecyclerView.addItemDecoration(new ItemHeaderDecoration(mContext, countryHeadSections));
    }

    /**
     * 初始化二级地址
     */
    private void initProvince() {
        provinceHeadSections = new ArrayList<>();
        provinceAdapter = new CommonAddressAdapter<AddressCountryBean.ProvinceBean>(R.layout.item_address, R.layout.item_head, provinceHeadSections);
        provinceRecyclerView.setAdapter(provinceAdapter);
        LinearLayoutManager provinceManager = new LinearLayoutManager(mContext);
        provinceRecyclerView.setLayoutManager(provinceManager);
        provinceRecyclerView.addItemDecoration(new ItemHeaderDecoration(mContext, provinceHeadSections));
    }

    /**
     * 初始化三级地址
     */
    private void initCity() {
        cityHeadSections = new ArrayList<>();
        cityAdapter = new CommonAddressAdapter<AddressCountryBean.ProvinceBean.CityBean>(R.layout.item_address, R.layout.item_head, cityHeadSections);
        cityRecyclerView.setAdapter(cityAdapter);
        LinearLayoutManager areaListManager = new LinearLayoutManager(mContext);
        cityRecyclerView.setLayoutManager(areaListManager);
        cityRecyclerView.addItemDecoration(new ItemHeaderDecoration(mContext, cityHeadSections));
    }

    /**
     * 初始化四级地址
     */
    private void initArea() {
        areaHeadSections = new ArrayList<>();
        areaAdapter = new CommonAddressAdapter<AddressCountryBean.ProvinceBean.CityBean.AreaBean>(R.layout.item_address, R.layout.item_head, areaHeadSections);
        LinearLayoutManager areaManager = new LinearLayoutManager(mContext);
        areaRecyclerView.setLayoutManager(areaManager);
        areaRecyclerView.setAdapter(areaAdapter);
        areaRecyclerView.addItemDecoration(new ItemHeaderDecoration(mContext, areaHeadSections));
    }

    private void initListener() {
        countryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("AreaPickerView", oldCountrySelected + "~~~" + oldProvinceSelected + "~~~" + oldCitySelected);
                if (countryHeadSections.get(position).isHeader) {
                    return;
                }
                countryHeadSections.get(position).t.setStatus(true);
                countrySelected = position;

//                if (oldCountrySelected != -1 && oldCountrySelected != countrySelected) {
//                    countryHeadSections.get(oldCountrySelected).t.setStatus(false);
//                }
                if (countryHeadSections.get(position).t.getChildren() != null && countryHeadSections.get(position).t.getChildren().size() > 0) {
                    //判断是否是第一次点击
                    if (oldCountrySelected != -1) {
                        //两次点击不是同一个item
                        if (oldCountrySelected != countrySelected) {
                            //将之前已选数据清空状态
                            countryHeadSections.get(oldCountrySelected).t.setStatus(false);
                            // 清空下级数据，并重新添加新的
                            if (provinceHeadSections != null && provinceHeadSections.size() > 0) {
                                if (oldProvinceSelected != -1) {
                                    provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
                                }
                                oldProvinceSelected = -1;
                                oldCitySelected = -1;
                                oldAreaSelected = -1;
                                provinceHeadSections.clear();
                                clickCountry(position);
                            } else {
                                if (oldProvinceSelected != -1) {
                                    provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
                                }
                                clickCountry(position);
                            }
                        } else {
                            mTabLayout.getTabAt(1).select();
                        }
                    } else {
                        if (oldProvinceSelected != -1) {
                            provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
                        }
                        oldProvinceSelected = -1;
                        oldCitySelected = -1;
                        oldAreaSelected = -1;
                        provinceHeadSections.clear();
                        clickCountry(position);
                    }
                } else {
                    if (oldCountrySelected != -1 && countryHeadSections.size() > 0) {
                        countryHeadSections.get(oldCountrySelected).t.setStatus(false);
                    }
                    if (oldProvinceSelected != -1 && provinceHeadSections.size() > 0) {
                        provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
                    }
                    if (oldCitySelected != -1 && cityHeadSections.size() > 0) {
                        cityHeadSections.get(oldCitySelected).t.setStatus(false);
                    }
                    if (oldAreaSelected != -1 && areaHeadSections.size() > 0) {
                        areaHeadSections.get(oldAreaSelected).t.setStatus(false);
                    }

                    oldProvinceSelected = -1;
                    oldCitySelected = -1;
                    oldAreaSelected = -1;

                    provinceHeadSections.clear();
                    cityHeadSections.clear();
                    areaHeadSections.clear();

                    countryAdapter.notifyDataSetChanged();
                    provinceAdapter.notifyDataSetChanged();
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();

                    tabNames.set(0, countryHeadSections.get(position).t.getLabel());
                    mTabLayout.setupWithViewPager(mViewPager);
                    viewPagerAdapter.notifyDataSetChanged();
                    mAddressSelectResultBean.setAddressCountryBean(countryHeadSections.get(position).t);
                    mAddressSelectResultBean.setProvinceBean(null);
                    mAddressSelectResultBean.setCityBean(null);
                    mAddressSelectResultBean.setAreaBean(null);
                    oldCountrySelected = countrySelected;
                    EventBus.getDefault().post(mAddressSelectResultBean);
                }


//                if (position != oldCountrySelected) {
//                    if (oldProvinceSelected != -1 && provinceHeadSections.size()>0) {
//                        provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
//                    }
//                    if (oldCitySelected != -1 && cityHeadSections.size()>0) {
//                        cityHeadSections.get(oldCitySelected).t.setStatus(false);
//                    }

//                    oldProvinceSelected = -1;
//                    oldCitySelected = -1;
//                    oldAreaSelected = -1;
//                }
//
//                if (countryHeadSections.get(position).t.getChildren() != null && countryHeadSections.get(position).t.getChildren().size() > 0) {
//                    provinceHeadSections.clear();
//                    provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(position).t.getChildren()));
//                    mProvinceASBV.initLetters(mAddressModel.getRightLetters(countryHeadSections.get(position).t.getChildren()));
//                    countryAdapter.notifyDataSetChanged();
//                    provinceAdapter.notifyDataSetChanged();
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//
//                    tabNames.set(0, countryHeadSections.get(position).t.getLabel());
//
//                    if (tabNames.size() == 1) {
//                        tabNames.add("请选择");
//                    } else if (tabNames.size() > 1) {
//                        if (position != oldCountrySelected) {
//                            tabNames.set(1, "请选择");
//                            if (tabNames.size() == 4) {
//                                tabNames.remove(3);
//                                tabNames.remove(2);
//                            }
//                        }
//                    }
//                    mAddressSelectResultBean.setAddressCountryBean(countryHeadSections.get(position).t);
//                    mTabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    //默认选中tab
//                    mTabLayout.getTabAt(1).select();
//                    oldCountrySelected = countrySelected;
//                } else {
//                    oldProvinceSelected = -1;
//                    oldCitySelected = -1;
//                    oldAreaSelected = -1;
//
//                    countryAdapter.notifyDataSetChanged();
//                    provinceAdapter.notifyDataSetChanged();
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//
//                    tabNames.set(0, countryHeadSections.get(position).t.getLabel());
//                    mTabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    mAddressSelectResultBean.setAddressCountryBean(countryHeadSections.get(position).t);
//                    mAddressSelectResultBean.setProvinceBean(null);
//                    mAddressSelectResultBean.setCityBean(null);
//                    mAddressSelectResultBean.setAreaBean(null);
//                    oldCountrySelected = countrySelected;
//                    EventBus.getDefault().post(mAddressSelectResultBean);
//                }

            }
        });
        provinceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (provinceHeadSections.get(position).isHeader) {
                    return;
                }
                provinceHeadSections.get(position).t.setStatus(true);
                provinceSelected = position;
                //先判断当前item  是否有下级地址 如果没有，直接关闭对话框，返回数据，如果有，判断两次点击是否为同一个Item
                if (provinceHeadSections.get(position).t.getChildren() != null && provinceHeadSections.get(position).t.getChildren().size() > 0) {
                    //判断是否是第一次点击
                    if (oldProvinceSelected != -1) {
                        //两次点击不是同一个item
                        if (oldProvinceSelected != provinceSelected) {
                            //将之前已选数据清空状态
                            provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
                            // 清空下级数据，并重新添加新的
                            if (cityHeadSections != null && cityHeadSections.size() > 0) {
                                oldCitySelected = -1;
                                oldAreaSelected = -1;
                                cityHeadSections.clear();
                            }
                            clickProvince(position);
                        } else {
                            mTabLayout.getTabAt(2).select();
                        }
                    } else {
                        if (cityHeadSections != null && cityHeadSections.size() > 0) {
                            oldCitySelected = -1;
                            oldAreaSelected = -1;
                            cityHeadSections.clear();
                        }
                        clickProvince(position);
                    }
                } else {

                    if (oldProvinceSelected != -1 && provinceHeadSections.size() > 0) {
                        provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
                    }
                    if (oldCitySelected != -1 && cityHeadSections.size() > 0) {
                        cityHeadSections.get(oldCitySelected).t.setStatus(false);
                    }
                    if (oldAreaSelected != -1 && areaHeadSections.size() > 0) {
                        areaHeadSections.get(oldAreaSelected).t.setStatus(false);
                    }

                    oldCitySelected = -1;
                    oldAreaSelected = -1;

                    cityHeadSections.clear();
                    areaHeadSections.clear();

                    provinceAdapter.notifyDataSetChanged();
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();

                    mAddressSelectResultBean.setProvinceBean(provinceHeadSections.get(position).t);
                    mAddressSelectResultBean.setCityBean(null);
                    mAddressSelectResultBean.setAreaBean(null);
                    tabNames.set(1, provinceHeadSections.get(position).t.getLabel());

                    mTabLayout.setupWithViewPager(mViewPager);
                    viewPagerAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(mAddressSelectResultBean);
                }
            }
        });
        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (cityHeadSections.get(position).isHeader) {
                    return;
                }
//                areaHeadSections.clear();

                cityHeadSections.get(position).t.setStatus(true);
                citySelected = position;

//                if (oldCitySelected != -1 && oldCitySelected != citySelected) {
//                    cityHeadSections.get(oldCitySelected).t.setStatus(false);
//                }
                //先判断当前item  是否有下级地址 如果没有，直接关闭对话框，返回数据，如果有，判断两次点击是否为同一个Item
                if (cityHeadSections.get(position).t.getChildren() != null && cityHeadSections.get(position).t.getChildren().size() > 0) {
                    //判断是否是第一次点击
                    if (oldCitySelected != -1) {
                        //两次点击不是同一个item 不仅要判断是否是一个position,还要判断是否是同一个数据
                        if (oldCitySelected != citySelected) {
                            //将之前已选数据清空状态
                            cityHeadSections.get(oldCitySelected).t.setStatus(false);
                            // 清空下级数据，并重新添加新的
                            if (areaHeadSections != null && areaHeadSections.size() > 0) {
                                oldAreaSelected = -1;
                                areaHeadSections.clear();
                            }
                            clickCity(position);
                        } else {
                            mTabLayout.getTabAt(3).select();
                        }
                    } else {
                        if (areaHeadSections != null && areaHeadSections.size() > 0) {
                            oldAreaSelected = -1;
                            areaHeadSections.clear();
                        }
                        clickCity(position);
                    }
                } else {

                    if (oldCitySelected != -1 && cityHeadSections.size() > 0) {
                        cityHeadSections.get(oldCitySelected).t.setStatus(false);
                    }
                    if (oldAreaSelected != -1 && areaHeadSections.size() > 0) {
                        areaHeadSections.get(oldAreaSelected).t.setStatus(false);
                    }
                    oldAreaSelected = -1;
                    areaHeadSections.clear();
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    tabNames.set(2, cityHeadSections.get(position).t.getLabel());
                    mAddressSelectResultBean.setCityBean(cityHeadSections.get(position).t);
                    mAddressSelectResultBean.setAreaBean(null);

                    mTabLayout.setupWithViewPager(mViewPager);
                    viewPagerAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(mAddressSelectResultBean);
                }


//                if (position != oldCitySelected) {
//
////                    if (oldAreaSelected != -1 && cityHeadSections.get(position).t.getChildren() != null&&areaHeadSections.size()>0) {
////                        areaHeadSections.get(oldAreaSelected).t.setStatus(false);
////                    }
//
//                    oldAreaSelected = -1;
//                }
//                oldCitySelected = citySelected;
//                if (cityHeadSections.get(position).t.getChildren() != null && cityHeadSections.get(position).t.getChildren().size() > 0) {
//                    areaHeadSections.clear();
//                    areaHeadSections.addAll(mAddressModel.getFourthSection(cityHeadSections.get(position).t.getChildren()));
//                    mAreaASBV.initLetters(mAddressModel.getRightLetters(cityHeadSections.get(position).t.getChildren()));
//
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//
//                    tabNames.set(2, cityHeadSections.get(position).t.getLabel());
//
//                    if (tabNames.size() == 3) {
//                        tabNames.add("请选择");
//                    } else if (tabNames.size() == 4) {
//                        tabNames.set(3, "请选择");
//                    }
//                    mAddressSelectResultBean.setCityBean(cityHeadSections.get(position).t);
//                    mTabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    mTabLayout.getTabAt(3).select();
//                } else {
//                    oldAreaSelected = -1;
//
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//                    tabNames.set(2, cityHeadSections.get(position).t.getLabel());
//                    mAddressSelectResultBean.setCityBean(cityHeadSections.get(position).t);
//                    mAddressSelectResultBean.setAreaBean(null);
//
//                    mTabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    EventBus.getDefault().post(mAddressSelectResultBean);
//                }

            }
        });

        areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (areaHeadSections.get(position).isHeader) {
                    return;
                }

                tabNames.set(3, areaHeadSections.get(position).t.getLabel());
                mTabLayout.setupWithViewPager(mViewPager);
                viewPagerAdapter.notifyDataSetChanged();
                areaHeadSections.get(position).t.setStatus(true);
                areaSelected = position;
                if (oldAreaSelected != -1 && oldAreaSelected != position) {
                    areaHeadSections.get(oldAreaSelected).t.setStatus(false);
                }
                oldAreaSelected = areaSelected;
                areaAdapter.notifyDataSetChanged();
                mAddressSelectResultBean.setAreaBean(areaHeadSections.get(position).t);
                EventBus.getDefault().post(mAddressSelectResultBean);
            }
        });

        mCountryASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                CommonHeadSection<AddressCountryBean> mCountryHeadSection = null;
                for (CommonHeadSection<AddressCountryBean> t : countryHeadSections) {
                    if (letter.equals(t.header)) {
                        mCountryHeadSection = t;
                    }
                }
                if (null == mCountryHeadSection) {
                    return;
                }
                int position = mCountryHeadSection.getPosition();
                if (position != -1) {
                    if (countryRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) countryRecyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        countryRecyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
        mProvinceASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                CommonHeadSection<AddressCountryBean.ProvinceBean> mProvinceHeadSection = null;
                Log.e("letter", letter);
                Log.e("province", provinceHeadSections.toString());
                for (CommonHeadSection<AddressCountryBean.ProvinceBean> t : provinceHeadSections) {
                    if (letter.equals(t.header)) {
                        mProvinceHeadSection = t;
                    }
                }
                if (null == mProvinceHeadSection) {
                    return;
                }
                int position = mProvinceHeadSection.getPosition();
                if (position != -1) {
                    if (provinceRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) provinceRecyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        provinceRecyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
        mCityASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean> mCityHeadSection = null;
                for (CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean> t : cityHeadSections) {
                    if (letter.equals(t.header)) {
                        mCityHeadSection = t;
                    }
                }
                if (null == mCityHeadSection) {
                    return;
                }
                int position = mCityHeadSection.getPosition();
                if (position != -1) {
                    if (cityRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) cityRecyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        cityRecyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
        mAreaASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean> mAreaHeadSection = null;
                for (CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean> t : areaHeadSections) {
                    if (letter.equals(t.header)) {
                        mAreaHeadSection = t;
                    }
                }
                if (null == mAreaHeadSection) {
                    return;
                }
                int position = mAreaHeadSection.getPosition();
                if (position != -1) {
                    if (areaRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) areaRecyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        areaRecyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        countryRecyclerView.scrollToPosition(oldCountrySelected == -1 ? 0 : oldCountrySelected);
                        break;
                    case 1:
                        provinceRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : oldProvinceSelected);
                        break;
                    case 2:
                        cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : oldCitySelected);
                        break;
                    case 3:
                        areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 点击一级地址
     *
     * @param position
     */
    private void clickCountry(int position) {
        provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(position).t.getChildren()));
        mProvinceASBV.initLetters(mAddressModel.getRightLetters(countryHeadSections.get(position).t.getChildren()));

        countryAdapter.notifyDataSetChanged();
        provinceAdapter.notifyDataSetChanged();
        cityAdapter.notifyDataSetChanged();
        areaAdapter.notifyDataSetChanged();

        tabNames.set(0, countryHeadSections.get(position).t.getLabel());

        if (tabNames.size() == 1) {
            tabNames.add("请选择");
        } else if (tabNames.size() > 1) {
            if (position != oldCountrySelected) {
                tabNames.set(1, "请选择");
                if (tabNames.size() == 4) {
                    tabNames.remove(3);
                    tabNames.remove(2);
                }
            }
        }
        mAddressSelectResultBean.setAddressCountryBean(countryHeadSections.get(position).t);
        mTabLayout.setupWithViewPager(mViewPager);
        viewPagerAdapter.notifyDataSetChanged();
        //默认选中tab
        mTabLayout.getTabAt(1).select();
        oldCountrySelected = countrySelected;
    }

    //点击二级菜单
    private void clickProvince(int position) {
        oldProvinceSelected = provinceSelected;
        cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(position).t.getChildren()));
        mCityASBV.initLetters(mAddressModel.getRightLetters(provinceHeadSections.get(position).t.getChildren()));
        provinceAdapter.notifyDataSetChanged();
        cityAdapter.notifyDataSetChanged();
        areaAdapter.notifyDataSetChanged();
        tabNames.set(1, provinceHeadSections.get(position).t.getLabel());
        if (tabNames.size() == 2) {
            tabNames.add("请选择");
        } else if (tabNames.size() == 4) {
//                        tabNames.set(3, "请选择");
            tabNames.remove(3);
            tabNames.set(2, "请选择");
        }
        mAddressSelectResultBean.setProvinceBean(provinceHeadSections.get(position).t);
        mTabLayout.setupWithViewPager(mViewPager);
        viewPagerAdapter.notifyDataSetChanged();
        mTabLayout.getTabAt(2).select();
    }

    /**
     * 点击三级地址
     *
     * @param position
     */
    public void clickCity(int position) {
        oldCitySelected = citySelected;
        areaHeadSections.addAll(mAddressModel.getFourthSection(cityHeadSections.get(position).t.getChildren()));
        mAreaASBV.initLetters(mAddressModel.getRightLetters(cityHeadSections.get(position).t.getChildren()));

        cityAdapter.notifyDataSetChanged();
        areaAdapter.notifyDataSetChanged();

        tabNames.set(2, cityHeadSections.get(position).t.getLabel());

        if (tabNames.size() == 3) {
            tabNames.add("请选择");
        } else if (tabNames.size() == 4) {
            tabNames.set(3, "请选择");
        }
        mAddressSelectResultBean.setCityBean(cityHeadSections.get(position).t);
        mTabLayout.setupWithViewPager(mViewPager);
        viewPagerAdapter.notifyDataSetChanged();
        mTabLayout.getTabAt(3).select();
    }

    /**
     * 用户已选择的地址,先判断是海外还是国内，再根据地址匹配省市区高亮
     *
     * @param value
     */
    public void setSelect(int... value) {
        tabNames = new ArrayList<>();
        if (value == null) {
            tabNames.add("请选择");
            if (isCreate) {
                mTabLayout.setupWithViewPager(mViewPager);
                viewPagerAdapter.notifyDataSetChanged();
                mTabLayout.getTabAt(0).select();
                if (countrySelected != -1)
                    countryHeadSections.get(countrySelected).t.setStatus(false);
                if (provinceSelected != -1)
                    provinceHeadSections.get(provinceSelected).t.setStatus(false);
                provinceHeadSections.clear();
                cityHeadSections.clear();
                countryAdapter.notifyDataSetChanged();
                provinceAdapter.notifyDataSetChanged();
                cityAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (value.length == 4) {
            for (int i : value) {
                Log.e("value---->", i + "");
            }
            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
            tabNames.add(provinceHeadSections.get(value[1]).t.getLabel());
            tabNames.add(cityHeadSections.get(value[2]).t.getLabel());
            tabNames.add(areaHeadSections.get(value[3]).t.getLabel());
            mTabLayout.setupWithViewPager(mViewPager);
            viewPagerAdapter.notifyDataSetChanged();

            mTabLayout.getTabAt(value.length - 1).select();

            if (countrySelected != -1)
                countryHeadSections.get(countrySelected).t.setStatus(false);
            if (provinceSelected != -1)
                provinceHeadSections.get(provinceSelected).t.setStatus(false);

            countryHeadSections.get(value[0]).t.setStatus(true);
            provinceHeadSections.get(value[1]).t.setStatus(true);
            cityHeadSections.get(value[2]).t.setStatus(true);
            areaHeadSections.get(value[3]).t.setStatus(true);

//            provinceHeadSections.clear();
//            provinceHeadSections.addAll(countryHeadSections.get(value[0]).t.getChildren());
//            provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(value[0]).t.getChildren()));

//            cityHeadSections.clear();
//            cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(value[1]).t.getChildren()));
//            areaHeadSections.clear();
//            areaHeadSections.addAll(mAddressModel.getFourthSection(cityHeadSections.get(value[2]).t.getChildren()));


            countryAdapter.notifyDataSetChanged();
            provinceAdapter.notifyDataSetChanged();
            cityAdapter.notifyDataSetChanged();
            areaAdapter.notifyDataSetChanged();
            oldCountrySelected = value[0];
            oldProvinceSelected = value[1];
            oldCitySelected = value[2];
            oldAreaSelected = value[3];
            areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
        }

        if (value.length == 3) {
            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
            tabNames.add(provinceHeadSections.get(value[1]).t.getLabel());
            tabNames.add(cityHeadSections.get(value[2]).t.getLabel());
            mTabLayout.setupWithViewPager(mViewPager);
            viewPagerAdapter.notifyDataSetChanged();
            mTabLayout.getTabAt(value.length - 1).select();
            if (countrySelected != -1)
                countryHeadSections.get(countrySelected).t.setStatus(false);
            if (provinceSelected != -1)
                provinceHeadSections.get(provinceSelected).t.setStatus(false);

            countryHeadSections.get(value[0]).t.setStatus(true);
            provinceHeadSections.get(value[1]).t.setStatus(true);
            cityHeadSections.get(value[2]).t.setStatus(true);
            provinceHeadSections.clear();
            provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(value[0]).t.getChildren()));
            cityHeadSections.clear();
            cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(value[1]).t.getChildren()));
            countryAdapter.notifyDataSetChanged();
            provinceAdapter.notifyDataSetChanged();
            cityAdapter.notifyDataSetChanged();
            oldCountrySelected = value[0];
            oldProvinceSelected = value[1];
            oldCitySelected = value[2];
            areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
        }

        if (value.length == 2) {
            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
//            tabNames.add(countryHeadSections.get(value[0]).t.getChildren().get(value[1]).getLabel());
            tabNames.add(provinceHeadSections.get(value[1]).t.getLabel());
            mTabLayout.setupWithViewPager(mViewPager);
            viewPagerAdapter.notifyDataSetChanged();
            mTabLayout.getTabAt(value.length - 1).select();

            countryHeadSections.get(countrySelected).t.setStatus(false);
            provinceHeadSections.get(provinceSelected).t.setStatus(false);

            countryHeadSections.get(value[0]).t.setStatus(true);
            provinceHeadSections.get(value[1]).t.setStatus(true);

//            provinceHeadSections.clear();
//            cityBeans.addAll(countryHeadSections.get(value[0]).t.getChildren());
//            provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(value[0]).t.getChildren()));

            countryAdapter.notifyDataSetChanged();
            provinceAdapter.notifyDataSetChanged();
            oldCountrySelected = value[0];
            oldProvinceSelected = value[1];
            oldCitySelected = -1;
            cityRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : oldProvinceSelected);
        }

        if (value.length == 1) {
            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
            mTabLayout.setupWithViewPager(mViewPager);
            viewPagerAdapter.notifyDataSetChanged();
            mTabLayout.getTabAt(value.length - 1).select();
            countryHeadSections.get(countrySelected).t.setStatus(false);
            countryHeadSections.get(value[0]).t.setStatus(true);
            provinceHeadSections.clear();
            countryAdapter.notifyDataSetChanged();
            provinceAdapter.notifyDataSetChanged();
            oldCountrySelected = value[0];
            countryRecyclerView.scrollToPosition(oldCountrySelected == -1 ? 0 : oldCountrySelected);
        }
    }

    public void setSelectNew(AddressSelectResultBean resultBean) {

        mAddressSelectResultBean = new AddressSelectResultBean();

        if (null == resultBean) {
            tabNames.clear();
            tabNames.add("请选择");
            viewPagerAdapter = new ViewPagerAdapter();
            mViewPager.setAdapter(viewPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.getTabAt(tabNames.size() - 1).select();
            return;
        } else {
            tabNames.clear();
            mAddressSelectResultBean.setAddressCountryBean(resultBean.getAddressCountryBean());
            mAddressSelectResultBean.setProvinceBean(resultBean.getProvinceBean());
            mAddressSelectResultBean.setCityBean(resultBean.getCityBean());
            mAddressSelectResultBean.setAreaBean(resultBean.getAreaBean());
        }

        if (resultBean.getAddressCountryBean() != null) {

            tabNames.add(resultBean.getAddressCountryBean().getLabel());

            if (oldCountrySelected != -1) {
                countryHeadSections.get(oldCountrySelected).t.setStatus(false);
            }

            countrySelected = getPositionInListForId(countryHeadSections, resultBean.getAddressCountryBean());

            if (countrySelected != -1) {
                countryHeadSections.get(countrySelected).t.setStatus(true);
            }
            provinceHeadSections.clear();
            countryAdapter.notifyDataSetChanged();
            provinceAdapter.notifyDataSetChanged();
            oldCountrySelected = countrySelected;
            int scrollPosition = oldCountrySelected;
//            countryRecyclerView.scrollToPosition(Math.max(scrollPosition - 1, 0));
            countryRecyclerView.scrollToPosition(oldCountrySelected == -1 ? 0 : scrollPosition - 1);

        }

        if (resultBean.getProvinceBean() != null) {
            tabNames.add(resultBean.getProvinceBean().getLabel());
            //是否是修改地址的情况
            if (provinceHeadSections.size() <= 0) {
                provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(countrySelected).t.getChildren()));
            }
            mProvinceASBV.initLetters(mAddressModel.getRightLetters(countryHeadSections.get(countrySelected).t.getChildren()));
//            if(oldProvinceSelected != -1){
//                provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
//            }

            provinceSelected = getPositionInListForId(provinceHeadSections, resultBean.getProvinceBean());
            if (provinceSelected != -1) {
                provinceHeadSections.get(provinceSelected).t.setStatus(true);
            }
            provinceAdapter.notifyDataSetChanged();
            oldProvinceSelected = provinceSelected;
            int scrollPosition = oldProvinceSelected;
//            provinceRecyclerView.scrollToPosition(Math.max(scrollPosition - 1, 0));
            provinceRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : scrollPosition - 1);


        }

        if (resultBean.getCityBean() != null) {
            tabNames.add(resultBean.getCityBean().getLabel());
            //是否是修改地址的情况
            if (cityHeadSections.size() <= 0) {
                cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(provinceSelected).t.getChildren()));
            }
            mCityASBV.initLetters(mAddressModel.getRightLetters(provinceHeadSections.get(provinceSelected).t.getChildren()));
//            if(oldCitySelected != -1){
//                cityHeadSections.get(oldCitySelected).t.setStatus(false);
//            }
            citySelected = getPositionInListForId(cityHeadSections, resultBean.getCityBean());
            if (citySelected != -1) {
                cityHeadSections.get(citySelected).t.setStatus(true);
            }

            cityAdapter.notifyDataSetChanged();
            oldCitySelected = citySelected;
            int scrollPosition = oldCitySelected;
//            cityRecyclerView.scrollToPosition(Math.max(scrollPosition - 1, 0));
            cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : scrollPosition - 1);

        }

        if (resultBean.getAreaBean() != null) {
            tabNames.add(resultBean.getAreaBean().getLabel());
            //是否是修改地址的情况
            if (areaHeadSections.size() <= 0) {
                areaHeadSections.addAll(mAddressModel.getFourthSection(cityHeadSections.get(citySelected).t.getChildren()));
            }
            mAreaASBV.initLetters(mAddressModel.getRightLetters(cityHeadSections.get(citySelected).t.getChildren()));

//            if(areaSelected != -1){
//                areaHeadSections.get(areaSelected).t.setStatus(false);
//            }

            areaSelected = getPositionInListForId(areaHeadSections, resultBean.getAreaBean());
            if (areaSelected != -1) {
                areaHeadSections.get(areaSelected).t.setStatus(true);
            }
            areaAdapter.notifyDataSetChanged();
            oldAreaSelected = areaSelected;
            int scrollPosition = oldCitySelected;
//            areaRecyclerView.scrollToPosition(Math.max(scrollPosition - 1, 0));
            areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : scrollPosition - 1);
        }
        viewPagerAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        viewPagerAdapter.notifyDataSetChanged();
        mTabLayout.getTabAt(tabNames.size() - 1).select();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (oldCountrySelected != -1 && countryHeadSections.size() > 0) {
            countryHeadSections.get(oldCountrySelected).t.setStatus(false);
        }
        if (oldProvinceSelected != -1 && provinceHeadSections.size() > 0) {
            provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
        }
        if (oldCitySelected != -1 && cityHeadSections.size() > 0) {
            cityHeadSections.get(oldCitySelected).t.setStatus(false);
        }
        if (oldAreaSelected != -1 && areaHeadSections.size() > 0) {
            areaHeadSections.get(oldAreaSelected).t.setStatus(false);
        }
        oldCountrySelected = -1;
        oldProvinceSelected = -1;
        oldCitySelected = -1;
        oldAreaSelected = -1;
    }

    public <T extends FirstLetterBean> int getPositionInListForId(List<CommonHeadSection<T>> mData, FirstLetterBean item) {
        int position = -1;
        for (CommonHeadSection<T> chs : mData) {
            if (!chs.isHeader) {
                if (chs.t.getValue().equals(item.getValue())) {
                    position = mData.indexOf(chs);
                    break;
                }
            }
        }
        return position;
    }


    public interface AreaPickerViewCallback {
        void callback(String address, int... value);
    }

    private String getSelectAddress() {
        StringBuilder selectAddress = new StringBuilder();
        for (String address : tabNames) {
            if (!address.equals("请选择")) {
                selectAddress.append(address);
            }
        }
        return selectAddress.toString();
    }

    class ViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return tabNames.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            Log.e("AreaPickView", "------------instantiateItem");
            return views.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
            Log.e("AreaPickView", "------------destroyItem");
        }

    }


}
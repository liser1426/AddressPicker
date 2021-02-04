//package com.example.addressproject;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AreaPickerView2 extends Dialog {
//
//
//    private TabLayout tabChinaOtherTb;
//    private String[] tabTitles = new String[]{"中国地区", "其他国家地区"};
//
//
//    private TabLayout tabLayout;
//    private ViewPager mViewPager;
//    private ImageView ivBtn;
//
//    private AreaPickerViewCallback areaPickerViewCallback;
//    /**
//     * View的集合
//     */
//    private List<View> views;
//    /**
//     * tab的集合
//     */
//    private List<String> tabNames = new ArrayList<>();
//    /**
//     * 整体数据集合
//     */
//    private List<AddressCountryBean> addressOriginData;
//    /**
//     * 国分组数据或者第一级数据
//     */
//    private List<CountryHeadSection> countryHeadSections;
//    /**
//     * 省/二级
//     */
////    private List<AddressBean.CityBean> cityBeans;
//    private List<ProvinceHeadSection> provinceHeadSections;
//    /**
//     * 市/三级
//     */
////    private List<AddressBean.CityBean.AreaBean> areaBeans;
//    private List<CityHeadSection> cityHeadSections;
//    /**
//     * 区/四级
//     */
//    private List<AreaHeadSection> areaHeadSections;
//
//    private Context context;
//
//    private ViewPagerAdapter viewPagerAdapter;
//
//    private CountryAdapter countryAdapter;
//    private ProvinceAdapter provinceAdapter;
//    private CityAdapter cityAdapter;
//    private AreaAdapter areaAdapter;
//
//    /**
//     * 选中的区域下标 默认-1
//     */
//    private int countrySelected = -1;
//    private int provinceSelected = -1;
//    private int citySelected = -1;
//    private int areaSelected = -1;
//
//    /**
//     * 历史选中的区域下标 默认-1
//     */
//    private int oldCountrySelected = -1;
//    private int oldProvinceSelected = -1;
//    private int oldCitySelected = -1;
//    private int oldAreaSelected = -1;
//
//    private RecyclerView countryRecyclerView;
//    private RecyclerView provinceRecyclerView;
//    private RecyclerView cityRecyclerView;
//    private RecyclerView areaRecyclerView;
//
//    private AZSideBarView mCountryASBV;
//    private AZSideBarView mProvinceASBV;
//    private AZSideBarView mCityASBV;
//    private AZSideBarView mAreaASBV;
//
//    private boolean isCreate;
//    private AddressModel mAddressModel = new AddressModel();
//
//    public AreaPickerView2(@NonNull Context context, int themeResId, List<AddressCountryBean> addressOriginData) {
//        super(context, themeResId);
//        this.addressOriginData = addressOriginData;
//        this.context = context;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_area_pickerview);
//        initView();
//        initData();
//        initCountry();
//        initProvince();
//        initCity();
//        initArea();
//        initListener();
//       /* areaBeans = new ArrayList<>();
//        areaAdapter = new AreaAdapter(R.layout.item_address, areaBeans);
//        LinearLayoutManager areaListManager = new LinearLayoutManager(context);
//        provinceRecyclerView.setLayoutManager(areaListManager);
//        provinceRecyclerView.setAdapter(areaAdapter);
//        areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                strings.set(2, areaBeans.get(position).getLabel());
//                tabLayout.setupWithViewPager(viewPager);
//                viewPagerAdapter.notifyDataSetChanged();
//                areaBeans.get(position).setStatus(true);
//                areaSelected = position;
//                if (oldCitySelected != -1 && oldCitySelected != position) {
//                    areaBeans.get(oldCitySelected).setStatus(false);
//                }
//                oldCitySelected = areaSelected;
//                areaAdapter.notifyDataSetChanged();
//                dismiss();
//                areaPickerViewCallback.callback(CountrySelected, citySelected, areaSelected);
//            }
//        });
//*/
//
//
//    }
//
//    private void initArea() {
//        areaHeadSections = new ArrayList<>();
//        areaAdapter = new AreaAdapter(R.layout.item_address,R.layout.item_head,areaHeadSections);
//        LinearLayoutManager areaManager = new LinearLayoutManager(context);
//        areaRecyclerView.setLayoutManager(areaManager);
//        areaRecyclerView.setAdapter(areaAdapter);
//        areaRecyclerView.addItemDecoration(new ItemHeaderDecoration(context,areaHeadSections));
//    }
//
//
//    /**
//     * 初始化一级地址适配器
//     */
//    private void initCountry() {
//        countryAdapter = new CountryAdapter(R.layout.item_address,R.layout.item_head,countryHeadSections);
//        countryRecyclerView.setAdapter(countryAdapter);
//        LinearLayoutManager provinceManager = new LinearLayoutManager(context);
//        countryRecyclerView.setLayoutManager(provinceManager);
//        countryRecyclerView.addItemDecoration(new ItemHeaderDecoration(context,countryHeadSections));
//    }
//    /**
//     * 初始化二级地址适配器
//     */
//    private void initProvince(){
//        provinceHeadSections = new ArrayList<>();
//        provinceAdapter = new ProvinceAdapter(R.layout.item_address,R.layout.item_head,provinceHeadSections);
//        provinceRecyclerView.setAdapter(provinceAdapter);
//        LinearLayoutManager provinceManager = new LinearLayoutManager(context);
//        provinceRecyclerView.setLayoutManager(provinceManager);
//        provinceRecyclerView.addItemDecoration(new ItemHeaderDecoration(context,provinceHeadSections));
//    }
//
//    /**
//     * 初始化三级菜单
//     */
//    private void initCity() {
//        cityHeadSections = new ArrayList<>();
//        cityAdapter = new CityAdapter(R.layout.item_address,R.layout.item_head, cityHeadSections);
//        cityRecyclerView.setAdapter(cityAdapter);
//        LinearLayoutManager areaListManager = new LinearLayoutManager(context);
//        cityRecyclerView.setLayoutManager(areaListManager);
//        cityRecyclerView.addItemDecoration(new ItemHeaderDecoration(context,cityHeadSections));
//    }
//    /**
//     * 弹窗View 初始化
//     */
//    private void initView() {
//        Window window = this.getWindow();
//        isCreate = true;
//        /**
//         * 位于底部
//         */
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(params);
//        /**
//         * 设置弹出动画
//         */
//        window.setWindowAnimations(R.style.PickerAnim);
//
//        tabChinaOtherTb = findViewById(R.id.tab_china_other_tl);
//        tabLayout = findViewById(R.id.tablayout);
//        mViewPager = findViewById(R.id.viewpager);
//        ivBtn = findViewById(R.id.iv_btn);
//
//        setChinaOtherAreaTap();
//
//        View countryView = LayoutInflater.from(context)
//                .inflate(R.layout.layout_recyclerview, null, false);
//        View provinceView = LayoutInflater.from(context)
//                .inflate(R.layout.layout_recyclerview, null, false);
//        View cityView = LayoutInflater.from(context)
//                .inflate(R.layout.layout_recyclerview, null, false);
//        final View areaView = LayoutInflater.from(context)
//                .inflate(R.layout.layout_recyclerview, null, false);
//
//        countryRecyclerView = countryView.findViewById(R.id.recyclerview);
//        provinceRecyclerView = provinceView.findViewById(R.id.recyclerview);
//        cityRecyclerView = cityView.findViewById(R.id.recyclerview);
//        areaRecyclerView = areaView.findViewById(R.id.recyclerview);
//
//        mCountryASBV = countryView.findViewById(R.id.azs_bv);
//        mProvinceASBV= provinceView.findViewById(R.id.azs_bv);
//        mCityASBV = cityView.findViewById(R.id.azs_bv);
//        mAreaASBV = areaView.findViewById(R.id.azs_bv);
//
//        views = new ArrayList<>();
//        views.add(countryView);
//        views.add(provinceView);
//        views.add(cityView);
//        views.add(areaView);
//
//        /**
//         * 配置adapter
//         */
//        viewPagerAdapter = new ViewPagerAdapter();
//        mViewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(mViewPager);
//        /**
//         * 这句话设置了过后，假如又3个tab 删除第三个 刷新过后 第二个划第三个会有弹性
//         * viewPager.setOffscreenPageLimit(2);
//         */
//    }
//
//
//    private void initData() {
//        countryHeadSections = new ArrayList<>();
//        countryHeadSections = mAddressModel.getFirstSection(addressOriginData);
//        /**
//         * 设置右侧字母索引
//         */
//        mCountryASBV.initLetters(mAddressModel.getRightLetters(addressOriginData));
//    }
//
//    /**
//     * 设置监听
//     */
//    private void initListener() {
//        ivBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//        countryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.e("AreaPickerView", oldCountrySelected + "~~~" + oldProvinceSelected + "~~~" + oldCitySelected);
//                if(countryHeadSections.get(position).isHeader){
//                    return;
//                }
////              cityBeans.clear();
//                //清空已选数据
////                provinceHeadSections.clear();
////                cityHeadSections.clear();
////                areaHeadSections.clear();
//
//                countryHeadSections.get(position).t.setStatus(true);
//                countrySelected = position;
//
//                if (oldCountrySelected != -1 && oldCountrySelected != countrySelected) {
//                    countryHeadSections.get(oldCountrySelected).t.setStatus(false);
//                    Log.e("AreaPickerView", "清空");
//                }
//                if (position != oldCountrySelected) {
//                    if (oldProvinceSelected != -1) {
//                        provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
//                    }
//                    if (oldCitySelected != -1) {
//                        cityHeadSections.get(oldCitySelected).t.setStatus(false);
//                    }
//
//                    oldProvinceSelected = -1;
//                    oldCitySelected = -1;
//                    oldAreaSelected = -1;
//                }
//                if (countryHeadSections.get(position).t.getChildren() != null && countryHeadSections.get(position).t.getChildren().size() > 0) {
////                    cityBeans.addAll(countryHeadSections.get(position).t.getChildren());
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
//                    tabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    //默认选中tab
//                    tabLayout.getTabAt(1).select();
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
//
//                    tabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    dismiss();
//                    String address = getSelectAddress();
//                    areaPickerViewCallback.callback(address,countrySelected);
//                }
//
//            }
//        });
//        provinceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                if(provinceHeadSections.get(position).isHeader){
//                    return;
//                }
//
////                cityHeadSections.clear();
////                areaHeadSections.clear();
//
//                provinceHeadSections.get(position).t.setStatus(true);
//                provinceSelected = position;
//
//                if (oldProvinceSelected != -1 && oldProvinceSelected != provinceSelected) {
//                    provinceHeadSections.get(oldProvinceSelected).t.setStatus(false);
//                }
//                if (position != oldProvinceSelected) {
//                    if (oldCitySelected != -1 && provinceHeadSections.get(position).t.getChildren() != null) {
//                        cityHeadSections.get(oldCitySelected).t.setStatus(false);
//                    }
//                    oldCitySelected = -1;
//                    oldAreaSelected = -1;
//
//                }
//                oldProvinceSelected = provinceSelected;
//                if (provinceHeadSections.get(position).t.getChildren() != null && provinceHeadSections.get(position).t.getChildren().size() > 0) {
//                    cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(position).t.getChildren()));
//                    mCityASBV.initLetters(mAddressModel.getRightLetters(provinceHeadSections.get(position).t.getChildren()));
//
//                    provinceAdapter.notifyDataSetChanged();
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//
//                    tabNames.set(1, provinceHeadSections.get(position).t.getLabel());
//
//                    if (tabNames.size() == 2) {
//                        tabNames.add("请选择");
//                    } else if (tabNames.size() == 4) {
////                        tabNames.set(3, "请选择");
//                        tabNames.remove(3);
//                        tabNames.set(2,"请选择");
//                    }
//                    tabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    tabLayout.getTabAt(2).select();
//                } else {
//                    oldCitySelected = -1;
//                    oldAreaSelected = -1;
//
//                    provinceAdapter.notifyDataSetChanged();
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//
//                    tabNames.set(1, provinceHeadSections.get(position).t.getLabel());
//
//                    tabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    dismiss();
//                    String address = getSelectAddress();
//                    areaPickerViewCallback.callback(address,countrySelected, provinceSelected);
//                }
//            }
//        });
//        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if(cityHeadSections.get(position).isHeader){
//                    return;
//                }
////                areaHeadSections.clear();
//
//                cityHeadSections.get(position).t.setStatus(true);
//                citySelected = position;
//
//                if (oldCitySelected != -1 && oldCitySelected != citySelected) {
//                    cityHeadSections.get(oldCitySelected).t.setStatus(false);
//                }
//                if (position != oldCitySelected) {
//
//                    if (oldAreaSelected != -1 && cityHeadSections.get(position).t.getChildren() != null) {
//                        areaHeadSections.get(oldAreaSelected).t.setStatus(false);
//                    }
//
//                    oldAreaSelected = -1;
//                }
//                oldCitySelected = citySelected;
//                if (cityHeadSections.get(position).t.getChildren() != null && cityHeadSections.get(position).t.getChildren().size() > 0) {
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
//                    tabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    tabLayout.getTabAt(3).select();
//                } else {
//                    areaSelected = -1;
//
//                    cityAdapter.notifyDataSetChanged();
//                    areaAdapter.notifyDataSetChanged();
//
//                    tabNames.set(2, cityHeadSections.get(position).t.getLabel());
//
//                    tabLayout.setupWithViewPager(mViewPager);
//                    viewPagerAdapter.notifyDataSetChanged();
//                    dismiss();
//                    String address = getSelectAddress();
//                    areaPickerViewCallback.callback(address,countrySelected, provinceSelected, citySelected);
//                }
//
//            }
//        });
//
//        areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if(areaHeadSections.get(position).isHeader){
//                    return;
//                }
//                tabNames.set(3, areaHeadSections.get(position).t.getLabel());
//                tabLayout.setupWithViewPager(mViewPager);
//                viewPagerAdapter.notifyDataSetChanged();
//                areaHeadSections.get(position).t.setStatus(true);
//                areaSelected = position;
//                if (oldAreaSelected != -1 && oldAreaSelected != position) {
//                    areaHeadSections.get(oldAreaSelected).t.setStatus(false);
//                }
//                oldAreaSelected = areaSelected;
//                areaAdapter.notifyDataSetChanged();
//                dismiss();
//                String address = getSelectAddress();
//                areaPickerViewCallback.callback(address,countrySelected, provinceSelected, citySelected, areaSelected);
//            }
//        });
//        mCountryASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
//            @Override
//            public void onLetterChange(String letter) {
//                CountryHeadSection mCountryHeadSection = null;
//                for(CountryHeadSection t:countryHeadSections){
//                    if(letter.equals(t.header)){
//                        mCountryHeadSection = t;
//                    }
//                }
//                if(null == mCountryHeadSection){
//                    return;
//                }
//                int position =  mCountryHeadSection.getPosition();
//                if (position != -1) {
//                    if (countryRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//                        LinearLayoutManager manager = (LinearLayoutManager) countryRecyclerView.getLayoutManager();
//                        manager.scrollToPositionWithOffset(position, 0);
//                    } else {
//                        countryRecyclerView.getLayoutManager().scrollToPosition(position);
//                    }
//                }
//            }
//        });
//        mProvinceASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
//            @Override
//            public void onLetterChange(String letter) {
//                ProvinceHeadSection mProvinceHeadSection = null;
//                Log.e("letter", letter);
//                Log.e("province",provinceHeadSections.toString());
//                for(ProvinceHeadSection t:provinceHeadSections){
//                    if(letter.equals(t.header)){
//                        mProvinceHeadSection = t;
//                    }
//                }
//                if(null == mProvinceHeadSection){
//                    return;
//                }
//                int position =  mProvinceHeadSection.getPosition();
//                if (position != -1) {
//                    if (provinceRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//                        LinearLayoutManager manager = (LinearLayoutManager) provinceRecyclerView.getLayoutManager();
//                        manager.scrollToPositionWithOffset(position, 0);
//                    } else {
//                        provinceRecyclerView.getLayoutManager().scrollToPosition(position);
//                    }
//                }
//            }
//        });
//        mCityASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
//            @Override
//            public void onLetterChange(String letter) {
//                CityHeadSection mCityHeadSection = null;
//                for(CityHeadSection t:cityHeadSections){
//                    if(letter.equals(t.header)){
//                        mCityHeadSection = t;
//                    }
//                }
//                if(null == mCityHeadSection){
//                    return;
//                }
//                int position =  mCityHeadSection.getPosition();
//                if (position != -1) {
//                    if (cityRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//                        LinearLayoutManager manager = (LinearLayoutManager) cityRecyclerView.getLayoutManager();
//                        manager.scrollToPositionWithOffset(position, 0);
//                    } else {
//                        cityRecyclerView.getLayoutManager().scrollToPosition(position);
//                    }
//                }
//            }
//        });
//        mAreaASBV.setOnLetterChangeListener(new AZSideBarView.OnLetterChangeListener() {
//            @Override
//            public void onLetterChange(String letter) {
//                AreaHeadSection mCityHeadSection = null;
//                for(AreaHeadSection t:areaHeadSections){
//                    if(letter.equals(t.header)){
//                        mCityHeadSection = t;
//                    }
//                }
//                if(null == mCityHeadSection){
//                    return;
//                }
//                int position =  mCityHeadSection.getPosition();
//                if (position != -1) {
//                    if (areaRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//                        LinearLayoutManager manager = (LinearLayoutManager) areaRecyclerView.getLayoutManager();
//                        manager.scrollToPositionWithOffset(position, 0);
//                    } else {
//                        areaRecyclerView.getLayoutManager().scrollToPosition(position);
//                    }
//                }
//            }
//        });
//
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                switch (i) {
//                    case 0:
//                        countryRecyclerView.scrollToPosition(oldCountrySelected == -1 ? 0 : oldCountrySelected);
//                        break;
//                    case 1:
//                        provinceRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : oldProvinceSelected);
//                        break;
//                    case 2:
//                        cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : oldCitySelected);
//                        break;
//                    case 3:
//                        areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//    }
//
//
//    private void setChinaOtherAreaTap() {
//        for (int i = 0; i < tabTitles.length; i++) {
//            tabChinaOtherTb.addTab(tabChinaOtherTb.newTab());
//            tabChinaOtherTb.getTabAt(i).setText(tabTitles[i]);
//        }
//    }
//
//    private String getSelectAddress() {
//        StringBuilder selectAddress = new StringBuilder();
//        for(String address : tabNames){
//            if(!address.equals("请选择")) {
//                selectAddress.append(address);
//            }
//        }
//        return selectAddress.toString();
//    }
//
//    class ViewPagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return tabNames.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
//            return view == o;
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return tabNames.get(position);
//        }
//
//        @NonNull
//        @Override
//        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            container.addView(views.get(position));
//            Log.e("AreaPickView", "------------instantiateItem");
//            return views.get(position);
//        }
//
//        @Override
//        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            container.removeView(views.get(position));
//            Log.e("AreaPickView", "------------destroyItem");
//        }
//
//    }
//
//    public interface AreaPickerViewCallback {
//        void callback(String address,int... value);
//    }
//
//    public void setAreaPickerViewCallback(AreaPickerViewCallback areaPickerViewCallback) {
//        this.areaPickerViewCallback = areaPickerViewCallback;
//    }
//
//    /**
//     * 用户已选择的地址,先判断是海外还是国内，再根据地址匹配省市区高亮
//     * @param value
//     */
//    public void setSelect(int... value) {
//        tabNames = new ArrayList<>();
//        if (value == null) {
//            tabNames.add("请选择");
//            if (isCreate) {
//                tabLayout.setupWithViewPager(mViewPager);
//                viewPagerAdapter.notifyDataSetChanged();
//                tabLayout.getTabAt(0).select();
//                if (countrySelected != -1)
//                    countryHeadSections.get(countrySelected).t.setStatus(false);
//                if (provinceSelected != -1)
//                    provinceHeadSections.get(provinceSelected).t.setStatus(false);
//                provinceHeadSections.clear();
//                cityHeadSections.clear();
//                countryAdapter.notifyDataSetChanged();
//                provinceAdapter.notifyDataSetChanged();
//                cityAdapter.notifyDataSetChanged();
//            }
//            return;
//        }
//
//        if (value.length == 4) {
//            for(int i:value){
//            Log.e("value---->",i+"");
//            }
//            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
//            tabNames.add(provinceHeadSections.get(value[1]).t.getLabel());
//            tabNames.add(cityHeadSections.get(value[2]).t.getLabel());
//            tabNames.add(areaHeadSections.get(value[3]).t.getLabel());
//            tabLayout.setupWithViewPager(mViewPager);
//            viewPagerAdapter.notifyDataSetChanged();
//
//            tabLayout.getTabAt(value.length - 1).select();
//
//            if (countrySelected != -1)
//                countryHeadSections.get(countrySelected).t.setStatus(false);
//            if (provinceSelected != -1)
//                provinceHeadSections.get(provinceSelected).t.setStatus(false);
//
//            countryHeadSections.get(value[0]).t.setStatus(true);
//            provinceHeadSections.get(value[1]).t.setStatus(true);
//            cityHeadSections.get(value[2]).t.setStatus(true);
//            areaHeadSections.get(value[3]).t.setStatus(true);
//
////            provinceHeadSections.clear();
////            provinceHeadSections.addAll(countryHeadSections.get(value[0]).t.getChildren());
////            provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(value[0]).t.getChildren()));
//
////            cityHeadSections.clear();
////            cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(value[1]).t.getChildren()));
////            areaHeadSections.clear();
////            areaHeadSections.addAll(mAddressModel.getFourthSection(cityHeadSections.get(value[2]).t.getChildren()));
//
//
//            countryAdapter.notifyDataSetChanged();
//            provinceAdapter.notifyDataSetChanged();
//            cityAdapter.notifyDataSetChanged();
//            areaAdapter.notifyDataSetChanged();
//            oldCountrySelected = value[0];
//            oldProvinceSelected = value[1];
//            oldCitySelected = value[2];
//            oldAreaSelected = value[3];
//            areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
//        }
//
//        if (value.length == 3) {
//            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
//            tabNames.add(provinceHeadSections.get(value[1]).t.getLabel());
//            tabNames.add(cityHeadSections.get(value[2]).t.getLabel());
//            tabLayout.setupWithViewPager(mViewPager);
//            viewPagerAdapter.notifyDataSetChanged();
//            tabLayout.getTabAt(value.length - 1).select();
//            if (countrySelected != -1)
//                countryHeadSections.get(countrySelected).t.setStatus(false);
//            if (provinceSelected != -1)
//                provinceHeadSections.get(provinceSelected).t.setStatus(false);
//
//            countryHeadSections.get(value[0]).t.setStatus(true);
//            provinceHeadSections.get(value[1]).t.setStatus(true);
//            cityHeadSections.get(value[2]).t.setStatus(true);
//            provinceHeadSections.clear();
//            provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(value[0]).t.getChildren()));
//            cityHeadSections.clear();
//            cityHeadSections.addAll(mAddressModel.getThirdSection(provinceHeadSections.get(value[1]).t.getChildren()));
//            countryAdapter.notifyDataSetChanged();
//            provinceAdapter.notifyDataSetChanged();
//            cityAdapter.notifyDataSetChanged();
//            oldCountrySelected = value[0];
//            oldProvinceSelected = value[1];
//            oldCitySelected = value[2];
//            areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
//        }
//
//        if (value.length == 2) {
//            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
////            tabNames.add(countryHeadSections.get(value[0]).t.getChildren().get(value[1]).getLabel());
//            tabNames.add(provinceHeadSections.get(value[1]).t.getLabel());
//            tabLayout.setupWithViewPager(mViewPager);
//            viewPagerAdapter.notifyDataSetChanged();
//            tabLayout.getTabAt(value.length - 1).select();
//
//            countryHeadSections.get(countrySelected).t.setStatus(false);
//            provinceHeadSections.get(provinceSelected).t.setStatus(false);
//
//            countryHeadSections.get(value[0]).t.setStatus(true);
//            provinceHeadSections.get(value[1]).t.setStatus(true);
//
//            provinceHeadSections.clear();
////            cityBeans.addAll(countryHeadSections.get(value[0]).t.getChildren());
//            provinceHeadSections.addAll(mAddressModel.getSecondSection(countryHeadSections.get(value[0]).t.getChildren()));
//
//            countryAdapter.notifyDataSetChanged();
//            provinceAdapter.notifyDataSetChanged();
//            oldCountrySelected = value[0];
//            oldProvinceSelected = value[1];
//            oldCitySelected = -1;
//            cityRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : oldProvinceSelected);
//        }
//
//        if (value.length == 1) {
//            tabNames.add(countryHeadSections.get(value[0]).t.getLabel());
//            tabLayout.setupWithViewPager(mViewPager);
//            viewPagerAdapter.notifyDataSetChanged();
//            tabLayout.getTabAt(value.length - 1).select();
//            countryHeadSections.get(countrySelected).t.setStatus(false);
//            countryHeadSections.get(value[0]).t.setStatus(true);
//            provinceHeadSections.clear();
//            countryAdapter.notifyDataSetChanged();
//            provinceAdapter.notifyDataSetChanged();
//            oldCountrySelected = value[0];
//            countryRecyclerView.scrollToPosition(oldCountrySelected == -1 ? 0 : oldCountrySelected);
//        }
//    }
//
//}

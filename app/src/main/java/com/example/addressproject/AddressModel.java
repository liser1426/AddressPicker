package com.example.addressproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/28
 * Time: 上午9:58
 */
class AddressModel {

    /**
     * 获取数据并排序
     * @param context
     * @return
     */
   public  List<AddressCountryBean> getCountryData(Context context){
       Gson gson = new Gson();
       List<AddressCountryBean> addressCountryBeans = gson.fromJson(getJsonFromAssets(context), new TypeToken<List<AddressCountryBean>>() {
       }.getType());
       Log.e("addressBeans", addressCountryBeans.toString());
       addressCountryBeans = sort(addressCountryBeans);
       return addressCountryBeans;
   }


    /**
     * 按字母处理排序问题
     */
    public  List<AddressCountryBean> sort(List<AddressCountryBean> addressCountryBeans){

        if(null== addressCountryBeans || addressCountryBeans.size()< 0){
            return null;
        }

        for (AddressCountryBean mData : addressCountryBeans) {
            //汉字转换成拼音
            String countryFirst = getFirstLetterSpell(mData.getLabel());
            mData.setFirstLetter(countryFirst);

            if(mData.getChildren() == null || mData.getChildren().size() == 0){
                continue;
            }
            for(AddressCountryBean.ProvinceBean cityBean : mData.getChildren()){

                String cityFirst = getFirstLetterSpell(cityBean.getLabel());
                cityBean.setFirstLetter(cityFirst);

                if(cityBean.getChildren() == null || cityBean.getChildren().size() == 0){
                    continue;
                }
                for(AddressCountryBean.ProvinceBean.CityBean areaBean :cityBean.getChildren()){

                    String areaFirst = getFirstLetterSpell(areaBean.getLabel());
                    areaBean.setFirstLetter(areaFirst);

                    if(areaBean.getChildren() == null || areaBean.getChildren().size() == 0){
                        continue;
                    }

                    for(AddressCountryBean.ProvinceBean.CityBean.AreaBean villageBean : areaBean.getChildren()){
                        String villageFirst = getFirstLetterSpell(villageBean.getLabel());
                        villageBean.setFirstLetter(villageFirst);
                    }
                    Log.e("areaBean",areaBean.getChildren().toString());
                    Collections.sort(areaBean.getChildren(), new LettersComparator<AddressCountryBean.ProvinceBean.CityBean.AreaBean>());
                }

                Collections.sort(cityBean.getChildren(), new LettersComparator<AddressCountryBean.ProvinceBean.CityBean>());

            }
            Collections.sort(mData.getChildren(), new LettersComparator<AddressCountryBean.ProvinceBean>());

            //取第一个首字母
//            if(pinyin.length()>0) {
//                String letters = pinyin.substring(0, 1).toUpperCase();
//                // 正则表达式，判断首字母是否是英文字母
//                if (letters.matches("[A-Z]")) {
//                    if(!letterRights.contains(letters)){
//                        letterRights.add(letters.toUpperCase());
//                    }
//                    mData.setFirstLetter(letters.toUpperCase());
//                } else {
//                    if(!letterRights.contains("#")){
//                        letterRights.add("#");
//                    }
//                    mData.setFirstLetter("#");
//                }
//            }
        }
        Collections.sort(addressCountryBeans, new LettersComparator<AddressCountryBean>());
        return addressCountryBeans;
    }

    /**
     * 按字母将一级数据进行分组
     */
    public List<CommonHeadSection<AddressCountryBean>> getFirstSection(List<AddressCountryBean> addressCountryBeans){
        List<String> letterRights = getRightLetters(addressCountryBeans);
        int headPosition=0;
        List<CommonHeadSection<AddressCountryBean>> list = new ArrayList<>();
        for (int i = 0; i < letterRights.size(); i++) {
            headPosition++;
            CommonHeadSection<AddressCountryBean> sectionHeader = new CommonHeadSection<AddressCountryBean>(true, letterRights.get(i), headPosition);
            list.add(sectionHeader);
            if (addressCountryBeans.size() != 0) {
                for (int j = 0; j < addressCountryBeans.size(); j++) {
                    if(addressCountryBeans.get(j).getFirstLetter().equals(letterRights.get(i))) {
                        headPosition++;
                        CommonHeadSection<AddressCountryBean> addressBean = new  CommonHeadSection<AddressCountryBean>(addressCountryBeans.get(j));
                        list.add(addressBean);
                    }
                }
            }
        }
        return list;
    }
    /**
     * 按字母将二级数据进行分组
     */
    public List<CommonHeadSection<AddressCountryBean.ProvinceBean>> getSecondSection(List<AddressCountryBean.ProvinceBean> provinceBeans){
        List<String> letterRights = getRightLetters(provinceBeans);
        int headPosition=0;
        List<CommonHeadSection<AddressCountryBean.ProvinceBean>> list = new ArrayList<>();
        for (int i = 0; i < letterRights.size(); i++) {
            headPosition++;
            CommonHeadSection<AddressCountryBean.ProvinceBean> sectionHeader = new CommonHeadSection<AddressCountryBean.ProvinceBean>(true, letterRights.get(i), headPosition);
            list.add(sectionHeader);
            if (provinceBeans.size() != 0) {
                for (int j = 0; j < provinceBeans.size(); j++) {
                    if(provinceBeans.get(j).getFirstLetter().equals(letterRights.get(i))) {
                        headPosition++;
                        CommonHeadSection<AddressCountryBean.ProvinceBean> addressBean = new CommonHeadSection<AddressCountryBean.ProvinceBean>(provinceBeans.get(j));
                        list.add(addressBean);
                    }
                }
            }
        }
        return list;
    }

    /**
     *
     * 按字母将四级数据进行分组
     * @param areaBeans
     * @return
     */
    public List<CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean>> getFourthSection(List<AddressCountryBean.ProvinceBean.CityBean.AreaBean> areaBeans){
        List<String> letterRights = getRightLetters(areaBeans);
        int headPosition=0;
        List<CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean>> list = new ArrayList<>();
        for (int i = 0; i < letterRights.size(); i++) {
            headPosition++;
            CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean> sectionHeader = new CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean>(true, letterRights.get(i), headPosition);
            list.add(sectionHeader);
            if (areaBeans.size() != 0) {
                for (int j = 0; j < areaBeans.size(); j++) {
                    if(areaBeans.get(j).getFirstLetter().equals(letterRights.get(i))) {
                        headPosition++;
                        CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean> addressBean = new CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean.AreaBean>(areaBeans.get(j));
                        list.add(addressBean);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取三级数据
     * @param cityBeans
     * @return
     */
    public List<CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean>> getThirdSection(List<AddressCountryBean.ProvinceBean.CityBean> cityBeans){
        List<String> letterRights = getRightLetters(cityBeans);
        int headPosition=0;
        List<CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean>> cityList = new ArrayList<>();
        for (int i = 0; i < letterRights.size(); i++) {
            headPosition++;
            CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean> sectionHeader = new CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean>(true, letterRights.get(i), headPosition);
            cityList.add(sectionHeader);
            if (cityBeans.size() != 0) {
                for (int j = 0; j < cityBeans.size(); j++) {
                    if(cityBeans.get(j).getFirstLetter().equals(letterRights.get(i))) {
                        headPosition++;
                        CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean> addressBean = new CommonHeadSection<AddressCountryBean.ProvinceBean.CityBean>(cityBeans.get(j));
                        cityList.add(addressBean);
                    }
                }
            }
        }
        return cityList;
    }
    /**
     * 读取本地文件
     * @param context
     * @return
     */
    public  String getJsonFromAssets(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("region.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String getFirstLetterSpell(String firstLetter){
        String pinyin =  PinyinUtils.converterToFirstSpell(firstLetter);
        Log.e("pinyin--------->", pinyin);
        if(pinyin.length() > 0){
            String letters = pinyin.substring(0,1).toUpperCase();
            Log.e("letters--------->", letters);
            if(letters.matches("[A-Z]")){
                return letters.toUpperCase();
            }else{
                return "#";
            }
        }
        return  "#";
    }

    public  <T extends FirstLetterBean> List<String>  getRightLetters(List<T> datas){

        List<String> rightLetters = new ArrayList<>();

        for(FirstLetterBean data :datas){
            if(!rightLetters.contains(data.getFirstLetter())){
                rightLetters.add(data.getFirstLetter());
            }
        }
        return rightLetters;
    }

    public <T extends SectionEntity<FirstLetterBean>> int getPositionInListForId(List<T> mData, AddressCountryBean item){
        int position = -1 ;
        for(T chs:mData){
            if(!chs.isHeader) {
                if (chs.t.getValue().equals(item.getValue())) {
                    position = mData.indexOf(chs);
                    break;
                }
            }
        }
        return position;
    }

}

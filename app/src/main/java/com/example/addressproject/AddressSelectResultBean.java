package com.example.addressproject;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/30
 * Time: 下午3:37
 */
class AddressSelectResultBean {

    private AddressCountryBean addressCountryBean;
    private AddressCountryBean.ProvinceBean provinceBean;
    private AddressCountryBean.ProvinceBean.CityBean cityBean;
    private AddressCountryBean.ProvinceBean.CityBean.AreaBean areaBean;

    public AddressCountryBean getAddressCountryBean() {
        return addressCountryBean;
    }

    public void setAddressCountryBean(AddressCountryBean addressCountryBean) {
        this.addressCountryBean = addressCountryBean;
    }

    public AddressCountryBean.ProvinceBean getProvinceBean() {
        return provinceBean;
    }

    public void setProvinceBean(AddressCountryBean.ProvinceBean provinceBean) {
        this.provinceBean = provinceBean;
    }

    public AddressCountryBean.ProvinceBean.CityBean getCityBean() {
        return cityBean;
    }

    public void setCityBean(AddressCountryBean.ProvinceBean.CityBean cityBean) {
        this.cityBean = cityBean;
    }

    public AddressCountryBean.ProvinceBean.CityBean.AreaBean getAreaBean() {
        return areaBean;
    }

    public void setAreaBean(AddressCountryBean.ProvinceBean.CityBean.AreaBean areaBean) {
        this.areaBean = areaBean;
    }
}

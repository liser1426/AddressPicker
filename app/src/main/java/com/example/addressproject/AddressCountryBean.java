package com.example.addressproject;

import java.util.List;

public class AddressCountryBean extends FirstLetterBean {


    private List<ProvinceBean> children;


    public List<ProvinceBean> getChildren() {
        return children;
    }

    public void setChildren(List<ProvinceBean> children) {
        this.children = children;
    }


    public class ProvinceBean extends FirstLetterBean {

        private List<CityBean> children;

        public List<CityBean> getChildren() {
            return children;
        }

        public void setChildren(List<CityBean> children) {
            this.children = children;
        }


        public class CityBean extends FirstLetterBean {

            private List<AreaBean> children;

            public void setChildren(List<AreaBean> children) {
                this.children = children;
            }
            public List<AreaBean> getChildren() {
                return children;
            }

            public class AreaBean extends FirstLetterBean {


            }

        }

    }

}

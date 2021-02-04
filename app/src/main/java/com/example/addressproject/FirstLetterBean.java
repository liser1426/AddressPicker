package com.example.addressproject;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/28
 * Time: 上午10:43
 */
class FirstLetterBean {
    private String firstLetter;
    private String label;
    private String value;

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}

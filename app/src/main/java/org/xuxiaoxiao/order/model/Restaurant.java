package org.xuxiaoxiao.order.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class Restaurant extends BmobObject {
    private String name;
    private Integer rate;
    private BmobFile photo;
    private String address;
    private String pinYinName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getPinYinName() {
        return pinYinName;
    }

    public void setPinYinName(String pinYinName) {
        this.pinYinName = pinYinName;
    }
}

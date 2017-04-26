package org.xuxiaoxiao.order.model;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class Dish {
    private String name;  // 菜名
    private Integer price;  // 价格
    private String discription;
    private BmobFile photoUrl;

    public Dish(String name, Integer price, String discription, BmobFile photoUrl) {
        this.name = name;
        this.price = price;
        this.discription = discription;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public BmobFile getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(BmobFile photoUrl) {
        this.photoUrl = photoUrl;
    }
}

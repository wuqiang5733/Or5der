package org.xuxiaoxiao.order.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class Dish extends BmobObject {
    private String name;  // 菜名
    private Integer price;  // 价格
    private String discription;
    private BmobFile photoUrl;
    private String restaurantName;

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

    public BmobFile getPhoto() {
        return photoUrl;
    }

    public void setPhoto(BmobFile photo) {
        this.photoUrl = photo;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}

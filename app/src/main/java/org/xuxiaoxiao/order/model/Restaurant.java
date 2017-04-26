package org.xuxiaoxiao.order.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class Restaurant extends BmobObject {
    private String name;
    private Integer rate;

    public Restaurant(String name,int rate){
        this.name = name;
        this.rate = rate;
    }

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


}

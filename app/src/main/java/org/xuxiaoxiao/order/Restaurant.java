package org.xuxiaoxiao.order;

import cn.bmob.v3.BmobObject;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class Restaurant extends BmobObject {
    private String name;
    private int rate;

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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


}

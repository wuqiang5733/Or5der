package org.xuxiaoxiao.order;

import cn.bmob.v3.BmobObject;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class Restaurant extends BmobObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

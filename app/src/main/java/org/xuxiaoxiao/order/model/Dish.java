package org.xuxiaoxiao.order.model;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class Dish {
    private String name;
    private Integer price;
    private String discription;

    public Dish(String name, Integer price, String discription) {
        this.name = name;
        this.price = price;
        this.discription = discription;
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
}

package org.xuxiaoxiao.order.infrastructure;


import org.xuxiaoxiao.order.model.Restaurant;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class RestaurantReadyEvent {
    private Restaurant restaurant;

    public RestaurantReadyEvent(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

package org.xuxiaoxiao.order.infrastructure;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class SendRstaurantNameEvent {
    String restaurantName;

    public SendRstaurantNameEvent(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}

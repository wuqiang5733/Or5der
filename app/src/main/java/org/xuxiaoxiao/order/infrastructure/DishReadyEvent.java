package org.xuxiaoxiao.order.infrastructure;


import org.xuxiaoxiao.order.model.Dish;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class DishReadyEvent {
    private Dish dish;

    public DishReadyEvent(Dish dish) {
        this.dish = dish;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}

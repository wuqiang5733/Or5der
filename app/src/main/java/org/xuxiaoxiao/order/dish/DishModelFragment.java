/***
 Copyright (c) 2008-2014 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain	a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 Covered in detail in the book _The Busy Coder's Guide to Android Development_
 https://commonsware.com/Android
 */

package org.xuxiaoxiao.order.dish;

import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.xuxiaoxiao.order.infrastructure.DishReadyEvent;
import org.xuxiaoxiao.order.model.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//import de.greenrobot.event.EventBus;

public class DishModelFragment extends android.support.v4.app.Fragment {
    private List<Dish> model =
            Collections.synchronizedList(new ArrayList<Dish>());
    private boolean isStarted = false;
//    String restaurantName ;
    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.restaurant_name";


    public static DishModelFragment newInstance(String restaurantName) {
        // 这个方法接收来自 Activity 的数据
        Bundle args = new Bundle();
        args.putString(RESTAURANT_NAME, restaurantName);

        DishModelFragment fragment = new DishModelFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (!isStarted) {
            isStarted = true;
            new LoadWordsThread().start();
        }
    }

    public ArrayList<Dish> getModel() {
        return (new ArrayList<Dish>(model));
    }

    class LoadWordsThread extends Thread {
        @Override
        public void run() {
            // Fetch data from website
            BmobQuery<Dish> query = new BmobQuery<Dish>();
            query.setLimit(10);
            // 获得来自 Activity 的数据
            final String restaurantName = getArguments().getString(RESTAURANT_NAME);
//            EventBus.getDefault().post(new SendRstaurantNameEvent(restaurantName));
            query.addWhereEqualTo("restaurantName", restaurantName);
//            Log.d("WQWQ",restaurantName);

            query.findObjects(new FindListener<Dish>() {
                @Override
                public void done(List<Dish> object, BmobException e) {
                    if (e == null) {
                        // Success
                        for (Dish dish : object) {
                            if (!isInterrupted()) {
//                                Log.d("WQWQ",restaurant_item.getName());
                                model.add(new Dish(dish.getName(), dish.getPrice(), dish.getDiscription(), dish.getPhotoUrl(),dish.getRestaurantName()));
                                EventBus.getDefault().post(new DishReadyEvent(dish));
                            }
                        }
                    } else {
                        // Fail
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onDetach() {
//        EventBus.getDefault().unregister(this);
//
//        super.onDetach();
//    }
//    @Subscribe
//    public void onEventMainThread(SendRstaurantNameEvent event) {
//        restaurantName = event.getRestaurantName();
//        Log.d("WQWQ","饭店的名字");
//    }
}


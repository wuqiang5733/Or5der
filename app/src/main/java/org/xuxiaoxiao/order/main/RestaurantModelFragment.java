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

package org.xuxiaoxiao.order.main;

import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.xuxiaoxiao.order.infrastructure.RestaurantReadyEvent;
import org.xuxiaoxiao.order.model.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//import de.greenrobot.event.EventBus;

public class RestaurantModelFragment extends android.support.v4.app.Fragment {
    private static final String[] items = {"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
            "vel", "erat", "placerat", "ante", "porttitor", "sodales",
            "pellentesque", "augue", "purus"};
    private List<Restaurant> model =
            Collections.synchronizedList(new ArrayList<Restaurant>());
    private boolean isStarted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (!isStarted) {
            isStarted = true;
            new LoadWordsThread().start();
        }
    }

    public ArrayList<Restaurant> getModel() {
        return (new ArrayList<Restaurant>(model));
    }

    class LoadWordsThread extends Thread {
        @Override
        public void run() {
            // Fetch data from website
            BmobQuery<Restaurant> query = new BmobQuery<Restaurant>();
            query.setLimit(10);
            query.findObjects(new FindListener<Restaurant>() {
                @Override
                public void done(List<Restaurant> object, BmobException e) {
                    if (e == null) {
                        // Success
                        for (Restaurant restaurant : object) {
                            if (!isInterrupted()) {
                                Log.d("WQWQ",restaurant.getName());
                                model.add(new Restaurant(restaurant.getName(), restaurant.getRate()));
                                EventBus.getDefault().post(new RestaurantReadyEvent(restaurant));
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

    /*

    class LoadWordsThread extends Thread {
        @Override
        public void run() {
            for (String item : items) {
                if (!isInterrupted()) {
                    model.add(item);
                    EventBus.getDefault().post(new WordReadyEvent(item));
                    SystemClock.sleep(400);
                }
            }
        }
    }
    */
}

package org.xuxiaoxiao.order.dish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.model.Dish;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by WuQiang on 2017/4/27.
 */

public class NewDishActivity extends AppCompatActivity {
    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.order.newdishactivity.restaurantname";
    private String restaurantName;

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//
//        super.onStop();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(SendRstaurantNameEvent event) {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("WQWQ", "onCreate");
        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);
        setContentView(R.layout.activity_new_dish);
        Log.i("WQWQ", "onCreate");
        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);
        EditText dishName = (EditText) findViewById(R.id.dish_name_edit_text);
        EditText distPrice = (EditText) findViewById(R.id.dish_price_edit_text);
        EditText distDiscription = (EditText) findViewById(R.id.dish_discription_edit_text);
        Button saveButton = (Button) findViewById(R.id.save_dish);
        saveButton.setText(restaurantName);

        String picPath = "sdcard/temp.jpg";
        final BmobFile bmobFile = new BmobFile(new File(picPath));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Dish newDish = new Dish("扣肉", 28, "采用传统工艺制作", null, restaurantName);
                        newDish.save(new SaveListener<String>() {

                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Log.i("WQWQ", "创建数据成功：" + objectId);

                                } else {
                                    Log.i("WQWQ", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });

                    }
                }).start();
            }
        });
    }

    public static Intent newIntent(Context packageContext, String restaurantName) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, NewDishActivity.class);
        intent.putExtra(RESTAURANT_NAME, restaurantName);
        return intent;
    }

}

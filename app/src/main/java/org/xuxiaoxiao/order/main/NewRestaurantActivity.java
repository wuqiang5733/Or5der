package org.xuxiaoxiao.order.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.login.LoginActivity;
import org.xuxiaoxiao.order.model.Restaurant;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by WuQiang on 2017/4/27.
 */

public class NewRestaurantActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("WQWQ", "onCreate");

        setContentView(R.layout.activity_new_restaurant);
        Log.i("WQWQ", "onCreate");

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        EditText restaurantName = (EditText) findViewById(R.id.restaurant_name_edit_text);
        EditText restaurantRate = (EditText) findViewById(R.id.restaurant_rate_edit_text);
        Button saveButton = (Button) findViewById(R.id.save_restaurant);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Restaurant newRestaurant = new Restaurant();
                        newRestaurant.setName("新饭店");
                        newRestaurant.setRate(5);
//注意：不能调用gameScore.setObjectId("")方法
//                        gameScore.setName("比目");
//                        gameScore.setRate(89);
                        newRestaurant.save(new SaveListener<String>() {

                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
//                                    toast("创建数据成功：" + objectId);
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
}

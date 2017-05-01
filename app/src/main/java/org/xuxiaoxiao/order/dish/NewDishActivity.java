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
import org.xuxiaoxiao.order.addimage.MediaFolderActivity;
import org.xuxiaoxiao.order.model.Dish;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i("WQWQ", "onCreate");
        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);
        setContentView(R.layout.activity_new_dish);
//        Log.i("WQWQ", "onCreate");
        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);
        EditText dishName = (EditText) findViewById(R.id.dish_name_edit_text);
        EditText distPrice = (EditText) findViewById(R.id.dish_price_edit_text);
        EditText distDiscription = (EditText) findViewById(R.id.dish_discription_edit_text);
        Button addDishButton = (Button) findViewById(R.id.add_dish);
        Button selectImage = (Button) findViewById(R.id.select_image);
        addDishButton.setText(restaurantName);

        String picPath = "/storage/extSdCard/DCIM/Camera/20170430_003125.jpg";
//        String picPath = "sdcard/temp.jpg";
        final BmobFile bmobFile = new BmobFile("test.png",null,"http://bmob-cdn-10939.b0.upaiyun.com/2017/04/30/6d8efe6bb7484b568333bf955d8a2f33.jpg");
//        final BmobFile bmobFile = new BmobFile(new File(picPath));

// http://blog.csdn.net/u014454120/article/details/51302508
        /**
         * 应该是在上传图片成功的回调函数当中，往数据库当中添加一行数据
         */

        addDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

//                        bmobFile.uploadblock(new UploadFileListener() {
//
//                            @Override
//                            public void done(BmobException e) {
//                                if(e==null){
//                                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                                    Log.d("WQWQ","上传文件成功:" + bmobFile.getFileUrl());
//                                }else{
//                                    Log.d("WQWQ","上传文件失败：" + e.getMessage());
//                                }
//
//                            }
//
//                            @Override
//                            public void onProgress(Integer value) {
//                                // 返回的上传进度（百分比）
//                            }
//                        });

                        Dish newDish = new Dish("扣肉", 28, "采用传统工艺制作", bmobFile, restaurantName);
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
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MediaFolderActivity.class);
                startActivity(intent);
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

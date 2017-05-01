package org.xuxiaoxiao.order.dish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.addimage.MediaFolderActivity;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by WuQiang on 2017/4/27.
 */

public class NewDishActivity extends AppCompatActivity {
    private static final String RESTAURANT_NAME = "org.xuxiaoxiao.order.newdishactivity.restaurantname";
    private static final String IMAGE_PATH = "org.xuxiaoxiao.newdishactivity.image_path";
    private String restaurantName ,imagePath;
    private ImageView dishImageView;


// 为了选择图片之后，把图片的 路径 传过来
public static Intent newImagePathIntent(Context packageContent,String imagePath){
    Intent intent = new Intent(packageContent,NewDishActivity.class);
    intent.putExtra(IMAGE_PATH,imagePath);
    return intent;
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i("WQWQ", "onCreate");
        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);
        imagePath = getIntent().getStringExtra(IMAGE_PATH);

        setContentView(R.layout.activity_new_dish);
//        Log.i("WQWQ", "onCreate");
//        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);


        EditText dishName = (EditText) findViewById(R.id.dish_name_edit_text);
        EditText distPrice = (EditText) findViewById(R.id.dish_price_edit_text);
        EditText distDiscription = (EditText) findViewById(R.id.dish_discription_edit_text);
        Button addDishButton = (Button) findViewById(R.id.add_dish);
        Button selectImage = (Button) findViewById(R.id.select_image);
        dishImageView = (ImageView) findViewById(R.id.dish_image_view);
        if (imagePath != null){  // 如果能找到 imagePath 说明是从选择图片的Fragment 来的，把图片送去显示
            Log.d("WQWQ",imagePath);
            Toast.makeText(this,imagePath,Toast.LENGTH_SHORT).show();
            Glide
                    .with(this)
                    .load(imagePath)
                    .centerCrop()
//                    .placeholder(R.drawable.error)
                    .crossFade()
                    .into(dishImageView);
        }
//        addDishButton.setText(restaurantName);

        String picPath = "/storage/extSdCard/DCIM/Camera/20170430_003125.jpg";
//        String picPath = "sdcard/temp.jpg";
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
                        final BmobFile bmobFile = new BmobFile(new File(imagePath));

                        bmobFile.uploadblock(new UploadFileListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                    Log.d("WQWQ","上传文件成功:" + bmobFile.getFileUrl());
                                }else{
                                    Log.d("WQWQ","上传文件失败：" + e.getMessage());
                                }

                            }

                            @Override
                            public void onProgress(Integer value) {
                                // 返回的上传进度（百分比）
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
// 为了传递饭店的名称
    public static Intent newIntent(Context packageContext, String restaurantName) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, NewDishActivity.class);
        intent.putExtra(RESTAURANT_NAME, restaurantName);
        return intent;
    }
/**
 *               Dish newDish = new Dish("扣肉", 28, "采用传统工艺制作", bmobFile, restaurantName);
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
 *
 */
}



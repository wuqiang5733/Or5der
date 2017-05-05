package org.xuxiaoxiao.order.dish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;
import org.xuxiaoxiao.order.login.LoginActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class DishActivity extends UniversalFragmentActivity {

    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.android.dishintent.restaurantname";

    private static final String MODEL_TAG = "model";
    private static final String RESTAURANT_PHOTO_URL = "org.xuxiaoxiao.order.dish.DishActivity.restaurant_phpoto_rul";
    private DishModelFragment mFrag = null;
    private String restaurantName;
    private String restaurantPhotoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public static Intent newIntent(Context packageContext, String restaurantName,String restaurantPhotoUrl) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, DishActivity.class);
        intent.putExtra(RESTAURANT_NAME, restaurantName);
        intent.putExtra(RESTAURANT_PHOTO_URL, restaurantPhotoUrl);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // 获得 Intent 的数据
        restaurantName = getIntent()
                .getStringExtra(RESTAURANT_NAME);
        restaurantPhotoUrl = getIntent()
                .getStringExtra(RESTAURANT_NAME);
        // 把数据传给自己 Hold 的 Fragment
        return DishesFragment.newInstance(restaurantName,restaurantPhotoUrl);
    }

    @Override
    protected String[] getDesiredPermissions() {
        return (new String[]{});
//        return (new String[]{WRITE_EXTERNAL_STORAGE});
    }

    @Override
    protected void onPermissionDenied() {
        Toast.makeText(this, R.string.msg_sorry, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onReady(Bundle state) {

    }
}

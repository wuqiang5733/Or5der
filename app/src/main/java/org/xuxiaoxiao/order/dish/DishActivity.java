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

    private static final String RESTAURANT_NAME_AND_PHPTO_URL = "org.xuxiaoxiao.order.dish.DishActivity.restaurant_name_and_phpto_url";
    private DishModelFragment mFrag = null;
    private String[] nameAndUrl;

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

    public static Intent newIntent(Context packageContext, String[] strArray) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, DishActivity.class);
        intent.putExtra(RESTAURANT_NAME_AND_PHPTO_URL,strArray);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // 获得 Intent 的数据
        nameAndUrl = getIntent().getStringArrayExtra(RESTAURANT_NAME_AND_PHPTO_URL);
        // 把数据传给自己 Hold 的 Fragment
        return DishesFragment.newInstance(nameAndUrl);
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

package org.xuxiaoxiao.order.dish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;

/**
 * Created by WuQiang on 2017/4/26.
 */

public class DishActivity extends UniversalFragmentActivity {

    private static final String RESTAURANT_NAME =
            "org.xuxiaoxiao.android.dishintent.restaurantname";

    public static Intent newIntent(Context packageContext, String restaurantName) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, DishActivity.class);
        intent.putExtra(RESTAURANT_NAME, restaurantName);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // 获得 Intent 的数据
        String restaurantName = getIntent()
                .getStringExtra(RESTAURANT_NAME);
        // 把数据传给自己 Hold 的 Fragment
        return DishesFragment.newInstance(restaurantName);
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

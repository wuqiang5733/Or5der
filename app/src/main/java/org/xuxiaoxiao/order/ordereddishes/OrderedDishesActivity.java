package org.xuxiaoxiao.order.ordereddishes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;

/**
 * Created by WuQiang on 2017/5/2.
 */

public class OrderedDishesActivity extends UniversalFragmentActivity {
    private static final String ORDERED_DISHES = "org.xuxiaoxiao.ordereddishesactivity.oredred_dishes";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent newOredredDishesIntent(Context packageContext, String[] orderedDishes) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, OrderedDishesActivity.class);
        intent.putExtra(ORDERED_DISHES, orderedDishes);
//        intent.putExtra(ORDERED_DISHES, orderedDishes);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // 获得 Intent 的数据
//        orderedDishesArrayList = getIntent().getStringArrayExtra(ORDERED_DISHES);
        // 把数据传给自己 Hold 的 Fragment
        return OrderedDishesFragment.newInstance(getIntent().getStringArrayExtra(ORDERED_DISHES));
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
// 传递数据： https://github.com/kimkevin/CachePot

package org.xuxiaoxiao.order.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;
import org.xuxiaoxiao.order.login.LoginActivity;

import cn.bmob.v3.BmobUser;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends UniversalFragmentActivity {



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

//        FragmentManager mgr = getFragmentManager();
//        FragmentTransaction trans = mgr.beginTransaction();
/////////////////////////////////////////////////
        /*
           protected Fragment fragment;
           protected FragmentManager supportFragmentManager;
           protected FragmentTransaction fragmentTransaction;
         */
//        mFrag = (RestaurantModelFragment) supportFragmentManager.findFragmentByTag(MODEL_TAG);
//
//        if (mFrag == null) {
//            mFrag = new RestaurantModelFragment();
//            fragmentTransaction.add(mFrag,MODEL_TAG);
////            fragmentTransaction.add(mFrag, MODEL_TAG);
//        }

//        MainFragment demo =
//                (MainFragment) mgr.findFragmentById(android.R.id.content);
//
//        if (demo == null) {
//            demo = new AsyncDemoFragment();
//            trans.add(android.R.id.content, demo);
//        }

//        (MainFragment)fragment.setModel(mFrag.getModel());

//        if (!fragmentTransaction.isEmpty()) {
//            fragmentTransaction.commit();
//        }
    }
    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
    // 下面是跟权限有关的
    @Override
    protected String[] getDesiredPermissions() {
//        return (new String[]{});
        return (new String[]{WRITE_EXTERNAL_STORAGE,INTERNET,ACCESS_NETWORK_STATE,READ_EXTERNAL_STORAGE,ACCESS_NETWORK_STATE});
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

package org.xuxiaoxiao.order.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;

public class MainActivity extends UniversalFragmentActivity {

    private static final String MODEL_TAG = "model";
    private RestaurantModelFragment mFrag = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FragmentManager mgr = getFragmentManager();
//        FragmentTransaction trans = mgr.beginTransaction();
/////////////////////////////////////////////////
        /*
           protected Fragment fragment;
           protected FragmentManager supportFragmentManager;
           protected FragmentTransaction fragmentTransaction;
         */
        mFrag = (RestaurantModelFragment) supportFragmentManager.findFragmentByTag(MODEL_TAG);

        if (mFrag == null) {
            mFrag = new RestaurantModelFragment();
            fragmentTransaction.add(mFrag,MODEL_TAG);
//            fragmentTransaction.add(mFrag, MODEL_TAG);
        }

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

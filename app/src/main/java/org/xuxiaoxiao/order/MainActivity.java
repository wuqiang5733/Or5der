package org.xuxiaoxiao.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends UniversalFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
    // 下面是跟权限有关的
    @Override
    protected String[] getDesiredPermissions() {
        return (new String[]{WRITE_EXTERNAL_STORAGE});
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

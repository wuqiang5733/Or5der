package org.xuxiaoxiao.order.addimage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MediaFolderActivity extends UniversalFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MediaFolderFragment();
    }

    // 下面是跟权限有关的
    @Override
    protected String[] getDesiredPermissions() {
        return (new String[]{WRITE_EXTERNAL_STORAGE,INTERNET,ACCESS_NETWORK_STATE,READ_EXTERNAL_STORAGE});
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

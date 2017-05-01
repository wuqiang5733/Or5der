package org.xuxiaoxiao.order.addimage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;

public class MediaFolderActivity extends UniversalFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MediaFolderFragment();
    }

    // 下面是跟权限有关的
    @Override
    protected String[] getDesiredPermissions() {
//        return (new String[]{WRITE_EXTERNAL_STORAGE,INTERNET,ACCESS_NETWORK_STATE,READ_EXTERNAL_STORAGE});
        return (new String[]{});
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

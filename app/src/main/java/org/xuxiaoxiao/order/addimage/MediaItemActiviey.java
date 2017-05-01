package org.xuxiaoxiao.order.addimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.UniversalFragmentActivity;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by WuQiang on 2017/4/30.
 */

public class MediaItemActiviey extends UniversalFragmentActivity {

//    public static void finishMediaItemActivity(){
//        finish();
//    }
    private static final String MEDIA_FOLDER_NAME =
            "org.xuxiaoxiao.android.mediaFolderName";

    public static Intent newIntent(Context packageContext, String mediaFolderName) {
        // 可以在其它地方调用的，能够传递数据的 Intent
        Intent intent = new Intent(packageContext, MediaItemActiviey.class);
        intent.putExtra(MEDIA_FOLDER_NAME, mediaFolderName);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // 获得 Intent 的数据
        String mediaFolderName = getIntent()
                .getStringExtra(MEDIA_FOLDER_NAME);
//                    Log.d("WQWQ",mediaFolderName);

        // 把数据传给自己 Hold 的 Fragment
        return MediaItemFragment.newInstance(mediaFolderName);
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

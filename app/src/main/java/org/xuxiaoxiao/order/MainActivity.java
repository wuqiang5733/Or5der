package org.xuxiaoxiao.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Restaurant qianqian = new Restaurant();
        qianqian.setName("qianqian");
        qianqian.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.d("WQWQ","添加数据成功，返回objectId为："+objectId);
//                    toast("添加数据成功，返回objectId为："+objectId);
                }else{
                    Log.d("WQWQ","创建数据失败：" + e.getMessage());

//                    toast("创建数据失败：" + e.getMessage());
                }
            }
        });
    }
}

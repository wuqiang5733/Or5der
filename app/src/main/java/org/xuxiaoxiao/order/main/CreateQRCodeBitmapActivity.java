package org.xuxiaoxiao.order.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.xuxiaoxiao.order.R;
import org.xuxiaoxiao.order.login.LoginActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by WuQiang on 2017/5/3.
 */

public class CreateQRCodeBitmapActivity extends AppCompatActivity {
    ImageView qRCodeBitmapImageView;
    Button createQRCodeBitmap;
    EditText codeStringEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("WQWQ", "onCreate");

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_qrcode_bitmap);
        Log.i("WQWQ", "onCreate");
        qRCodeBitmapImageView = (ImageView) findViewById(R.id.qrcode_bitmap_image_view);
        createQRCodeBitmap = (Button) findViewById(R.id.create_qrcode_bitmap_button);
        codeStringEditText = (EditText)findViewById(R.id.code_string_edit_text);

        createQRCodeBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = codeStringEditText.getText().toString().trim();
                qRCodeBitmapImageView.setImageBitmap(encodeAsBitmap(temp));
            }
        });
    }

    Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 300, 300);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }

        // 如果不使用 ZXing Android Embedded 的话，要写的代码

//        int w = result.getWidth();
//        int h = result.getHeight();
//        int[] pixels = new int[w * h];
//        for (int y = 0; y < h; y++) {
//            int offset = y * w;
//            for (int x = 0; x < w; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels,0,100,0,0,w,h);

        return bitmap;
    }
}

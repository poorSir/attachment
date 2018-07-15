package com.example.szh.upload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.szh.upload.views.UploadFireLayout;

public class MainActivity extends AppCompatActivity {
    private UploadFireLayout uploadFireLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadFireLayout = findViewById(R.id.uploadFire);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BaseParam.takePhotoResult://相册选择
                uploadFireLayout.clickForResult( BaseParam.takePhotoResult,data);
                break;
            case BaseParam.takeCameraResult://手机拍摄
                uploadFireLayout.clickForResult(BaseParam.takeCameraResult,data);
                break;
                case BaseParam.vedioResult:
                    uploadFireLayout.clickForResult(BaseParam.vedioResult,data);
                    break;
        }
    }
}

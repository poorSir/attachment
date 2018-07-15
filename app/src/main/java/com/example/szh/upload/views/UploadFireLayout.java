package com.example.szh.upload.views;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.szh.upload.AttachmentModel;
import com.example.szh.upload.BaseParam;
import com.example.szh.upload.MainActivity;
import com.example.szh.upload.MyApplicaion;
import com.example.szh.upload.R;
import com.example.szh.upload.utils.AudioRecoderUtils;
import com.example.szh.upload.utils.PopupWindowFactory;
import com.example.szh.upload.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadFireLayout extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;

    private Activity mActivity = MyApplicaion.getActivity();
    private View mView;
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;
    private AudioRecoderUtils mAudioRecoderUtils;
    //图片保存路径
    private String mCurrentPath;
    //当前点击的图片控件
    private int selectIndex = 1;
    //当前选择模式 0-图片  1-语音 2-视频 3-通讯录
    private int selectMode = 0;
    //附件存储model
    private static List<AttachmentModel> attachmentList = new ArrayList<>();

    public UploadFireLayout(Context context) {
        super(context);
    }

    public UploadFireLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.layout_upload, null, false);
        mView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        initView();
        addView(mView);
    }

    //初始化控件
    private void initView() {
        for (int i = 0; i < 4; i++) {
            attachmentList.add(new AttachmentModel());
        }
        img1 = mView.findViewById(R.id.img1);
        img2 = mView.findViewById(R.id.img2);
        img3 = mView.findViewById(R.id.img3);
        img4 = mView.findViewById(R.id.img4);
        img5 = mView.findViewById(R.id.img5);
        rb1 = mView.findViewById(R.id.rb1);
        rb2 = mView.findViewById(R.id.rb2);
        rb3 = mView.findViewById(R.id.rb3);
        rb4 = mView.findViewById(R.id.rb4);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        img1.setOnTouchListener(this);
        img2.setOnTouchListener(this);
        img3.setOnTouchListener(this);
        img4.setOnTouchListener(this);
        img5.setOnTouchListener(this);
        //PopupWindow的布局文件
        final View view = View.inflate(mActivity, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(mActivity, view);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Log.e("11", filePath + "");
                mTextView.setText(TimeUtils.long2String(0));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                selectIndex = 1;
                selectFunction();
                break;
            case R.id.img2:
                selectIndex = 2;
                selectFunction();
                break;
            case R.id.img3:
                selectIndex = 3;
                selectFunction();
                break;
            case R.id.img4:
                selectIndex = 4;
                selectFunction();
                break;
            case R.id.img5:
                selectIndex = 5;
                selectFunction();
                break;
            case R.id.rb1:
                selectMode = 0;
                setImageFromModel();
                break;
            case R.id.rb2:
                selectMode = 1;
                setImageFromModel();
                break;
            case R.id.rb3:
                selectMode = 2;
                setImageFromModel();
                break;
            case R.id.rb4:
                selectMode = 3;
                setImageFromModel();
                break;
            default:
                break;
        }
    }

    /***
     * 功能选择
     */
    private void selectFunction() {
        switch (selectMode) {
            case 0:
                albumSelect();
                break;
            case 1://onTouch事件截断
                break;
            case 2:
                vedioButton();
                break;
            case 3:
                break;
        }
    }

    /***
     * 从model中设置图片
     */
    public void setImageFromModel() {
        for (int i = 1; i <= 5; i++) {
            Glide.with(mActivity)
                    .load(getAttachmentModel().getFile(i))
                    .asBitmap()
                    .placeholder(getResources().getDrawable(R.mipmap.ic_launcher))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into((ImageView) getImageView(i));
        }
    }

    /**
     * 相册选择
     */
    private void albumSelect() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mActivity.startActivityForResult(i, BaseParam.takePhotoResult);
        } else {
            Toast.makeText(mActivity, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 相机拍摄
     */
    public void takeCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            //创建文件
            File photoFile = createImageFile();
            if (photoFile != null) {
                //存入照片
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }
        }
        mActivity.startActivityForResult(takePictureIntent, BaseParam.takeCameraResult);
    }

    //创建图片文件
    private File createImageFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPath = image.getAbsolutePath();
        return image;
    }

    //生成文件名
    public static String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }

    /***
     * 视频录制
     * @return
     */
    public void vedioButton(){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        // 将文件存到指定的路径
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = storageDir + timeStamp+".mp4";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");

        // 自定义输出位置，这样可以将视频存在我们指定的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mActivity.startActivityForResult(intent, BaseParam.vedioResult);
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }

    //获取点击的图片控件
    public View getImageView() {
        switch (selectIndex) {
            case 1:
                return img1;
            case 2:
                return img2;
            case 3:
                return img3;
            case 4:
                return img4;
            case 5:
                return img5;
        }
        return null;
    }

    //获取点击的图片控件
    public View getImageView(int selectIndex) {
        switch (selectIndex) {
            case 1:
                return img1;
            case 2:
                return img2;
            case 3:
                return img3;
            case 4:
                return img4;
            case 5:
                return img5;
        }
        return null;
    }

    /***
     * 点击回调
     * @param requestCode
     * @param data
     */
    public void clickForResult(int requestCode, Intent data) {
        switch (requestCode) {
            case BaseParam.takePhotoResult://相册选择
                if (data == null) return;
                getAttachmentModel().setFile(getSelectIndex(), data.getData().toString());
                Glide.with(mActivity)
                        .load(data.getData())
                        .asBitmap()
                        .placeholder(getResources().getDrawable(R.mipmap.ic_launcher))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into((ImageView) getImageView());
                break;
            case BaseParam.takeCameraResult://手机拍摄
                getAttachmentModel().setFile(getSelectIndex(), getCurrentPath());
                Glide.with(mActivity)
                        .load(getCurrentPath())
                        .asBitmap()
                        .placeholder(getResources().getDrawable(R.mipmap.ic_launcher))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into((ImageView) getImageView());
                break;
                case BaseParam.vedioResult:
                    Log.e("22",data.getData()+"");
                    break;
        }
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public AttachmentModel getAttachmentModel() {
        AttachmentModel model = attachmentList.get(selectMode);
        return model;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (selectMode != 1) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                mAudioRecoderUtils.startRecord();
                break;
            case MotionEvent.ACTION_UP:
                mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                mPop.dismiss();
                break;
        }
        return true;
    }
}

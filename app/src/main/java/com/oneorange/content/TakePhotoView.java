package com.oneorange.content;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.oneorange.base.BaseActivity;
import com.oneorange.manager.FilePath;
import com.oneorange.utils.FileUtils;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/6/14.
 */
public class TakePhotoView extends BaseActivity {


    //data
    public int bundleCode;
    public String filepath;
    public String fileName;

    private final int RESULT_CODE = 3;
    private final int REQUEST_CODE_PICK_IMAGE = 4;
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filepath = FilePath.ASSET_PATH;
        getData();

    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleCode = bundle.getInt("photo", 0);
            switch (bundleCode) {
                case 1://相机选择
                    getPhotoFromPhoto();
                    break;
                case 2://本地图片
                    getPhotoFromLocal();
                    break;
            }
        } else {
            setResult(RESULT_CODE, null);
            finish();
        }
    }
    private String fileName(String end) {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        fileName = timeStampFormat.format(new Date()) + end;
        return fileName;
    }
    private void getPhotoFromPhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            ToastUtil.show(TakePhotoView.this, "请确认插入内存卡", 200);
        }
    }

    private void getPhotoFromLocal() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } catch (android.content.ActivityNotFoundException e) {
            setResult(RESULT_CODE, null);
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("result", resultCode + "");
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_CAMEIA:
                    Bundle extras = data.getExtras();
                    Bitmap bt = (Bitmap) extras.get("data");
                    fileName = filepath + fileName(".jpg");
                    FileUtils.saveFile(filepath, fileName, bt);
                    Intent photoIntent = new Intent();
                    fileName=new File(fileName).getAbsolutePath();
                    photoIntent.putExtra("path", fileName);
                    setResult(RESULT_CODE, photoIntent);
                    finish();
                    break;
                case REQUEST_CODE_PICK_IMAGE:
                    Uri localUri = data.getData();
                    String path = GetPath(localUri);
                    Intent localIntent = new Intent();
                    localIntent.putExtra("path", path);
                    setResult(RESULT_CODE, localIntent);
                    finish();
                    break;
            }
        } else if (resultCode == 0) {
            Intent intent = new Intent();
            intent.putExtra("path", "null");
            setResult(RESULT_CODE, intent);
            finish();
        } else {
            setResult(RESULT_CODE, null);
            finish();
        }
    }





    /**
     * 从uri得到真实的file路径
     *
     * @param uri
     * @return
     */
    private String GetPath(Uri uri) {
        //
        String path = uri.getPath();
        File mfile = new File(path);
        if (mfile.exists())
            return mfile.getPath();

        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
//			cursor.close();
            return (s);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return (null);
    }
}

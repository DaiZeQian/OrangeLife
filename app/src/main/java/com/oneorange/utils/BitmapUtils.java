package com.oneorange.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 2016/4/26.
 */
public class BitmapUtils {

    /**
     * 加载超大图片文件的方法
     * @param dst
     * @return
     */
    public static boolean ZoomBitmapFromFile(File dst,File outfile, float scale) {
        boolean ret = false;

        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (scale > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);

                opts.inSampleSize = (int)(scale);
                if(opts.inSampleSize==0)
                    opts.inSampleSize = 1;

                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }

            try {
                Bitmap bm = BitmapFactory.decodeFile(dst.getPath(), opts);
                ret = Global.SaveFile(outfile,Bitmap2Bytes(bm));
                bm.recycle();
                bm = null;
                System.gc();
            } catch (OutOfMemoryError e)
            {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     * 加载超大图片文件的方法
     * @param dst
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                float out_pix = opts.outWidth*opts.outHeight;
                float max_pix = width*height;
                float scale = out_pix / max_pix;
//
//	            float scaleWidth = ((float) opts.outWidth /width );
//	    		float scaleHeight = ((float)opts.outHeight /height );
//	    		float scale = scaleHeight;
//	    		if(scaleWidth>scaleHeight)
//	    			scale = scaleWidth;

                scale += 0.7;
                opts.inSampleSize = (int)(scale);
                if(opts.inSampleSize==0)
                    opts.inSampleSize = 1;

                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

	/*
	 * 坐标变换矩阵
	 * a b c
	 * d e f
	 * g h i
	 * x' = ax + by + c
	 * y' = dx + ey + f
	 */

    /**
     * 图片处理
     * @param degree
     */
    public static Bitmap toMatrix(Bitmap bmp, float degree, float scale,int reverseflag)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        if(reverseflag==0)
            matrix.postScale(scale, scale);
        else if(reverseflag==1)
            matrix.postScale(-scale, scale);	//水平反转
        else if(reverseflag==2)
            matrix.postScale(scale, -scale);	//垂直反转
        else if(reverseflag==3)
            matrix.postScale(-scale, -scale);	//水平、垂直反转

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        return bm;
    }




    /**
     * @param //将图片内容解析成字节数组
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    /**
     * @param //将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * @param //图片缩放
     * @param bitmap
     *            对象
     * @param w
     * @param h
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if(width*height < w*h)
            return(bitmap);

        // 计算图片缩放比例
        float out_pix = width*height;
        float max_pix = w*h;
        float scale = out_pix / max_pix;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    /**
     * 把Bitmap转Byte
     *
     * @Author HEH
     * @EditTime 2010-07-19 上午11:45:56
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        return baos.toByteArray();
    }


    /**
     * 把字节数组保存为一个文件
     *
     * @Author HEH
     * @EditTime 2010-07-19 上午11:45:56
     */
    public static File getFileFromBytes(byte[] b,String outDir, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            File myWorkDir = new File(outDir);
            if(!myWorkDir.exists())
                myWorkDir.mkdirs();

            file = new File(outDir, outputFile);
            if(file.exists())
                return file;

            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

}

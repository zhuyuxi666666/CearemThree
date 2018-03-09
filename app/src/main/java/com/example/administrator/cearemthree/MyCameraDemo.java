package com.example.administrator.cearemthree;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 自定义拍照，将特定图片添加到预览图片中保存起来
 *
 * @author xiaoxiao
 * @ClassName: MyCameraDemo
 * @Description:
 */
public class MyCameraDemo extends Activity implements OnClickListener {
    private SurfaceView surface = null;
    private Button but = null;
    private SurfaceHolder holder = null;
    private Camera cam = null;
    private boolean previewRunning = true;
    private ImageView iv_img_01, iv_img_02, iv_img_03;
    private FrameLayout flay_view;
    private TextView luyna;
    List<Bitmap> mDatas = new ArrayList<>();
    String filePath;
    List<String> filePathList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        this.but = (Button) super.findViewById(R.id.but);
        this.surface = (SurfaceView) super.findViewById(R.id.surface);
        iv_img_01 = (ImageView) findViewById(R.id.iv_img_01);
        iv_img_01.setOnClickListener(this);
        iv_img_02 = (ImageView) findViewById(R.id.iv_img_02);
        iv_img_02.setOnClickListener(this);
        iv_img_03 = (ImageView) findViewById(R.id.iv_img_03);
        iv_img_03.setOnClickListener(this);
        luyna = (TextView) findViewById(R.id.luyna);
        luyna.setOnClickListener(this);
        flay_view = (FrameLayout) findViewById(R.id.flay_view);
        this.holder = this.surface.getHolder();
        this.holder.addCallback(new MySurfaceViewCallback());
        this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.holder.setFixedSize(500, 350);
        this.but.setOnClickListener(new OnClickListenerImpl());


    }


    private class OnClickListenerImpl implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (cam != null && filePathList.size() < 3) {
                cam.autoFocus(new AutoFocusCallbackImpl());
            } else {
                Toast.makeText(getApplicationContext(), "最多张照片！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class MySurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (cam != null) {
                cam.stopPreview();// 停掉原来摄像头的预览
                cam.release();// 释放资源
                cam = null;// 取消原来摄像头
            }
            try {
                cam = Camera.open(0); // 取得第一个摄像头
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(MyCameraDemo.this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
                return;
            }
            cam = deal2(cam);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (cam != null) {
                if (MyCameraDemo.this.previewRunning) {
                    cam.stopPreview(); // 停止预览
                    MyCameraDemo.this.previewRunning = false;
                }
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        }

    }

    private class AutoFocusCallbackImpl implements AutoFocusCallback {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            if (success) { // 成功
                cam.takePicture(null, null, jpgcall);
            }

        }

    }

    Bitmap bmp;
    private PictureCallback jpgcall = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) { // 保存图片的操作
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            String fileName = "test_" + System.currentTimeMillis() + ".jpg";
            filePath = Environment.getExternalStorageDirectory()
                    .toString()
                    + File.separator
                    + "aaaaaaaaaa"
                    + File.separator
                    + fileName;
            bmp = rotateBitmapByDegree(bmp, 90);

            save(bmp, filePath, fileName);
            bmp = loadBitmap(filePath, true);
            setPictureDegreeZero(filePath);

            save(bmp, filePath, fileName);
            filePathList.add(filePath);

            mDatas.add(bmp);

            for (int i = 0; i < filePathList.size(); i++) {
                iv_img_01.setImageBitmap(mDatas.get(0));
                if (filePathList.size() > 1) {
                    iv_img_02.setImageBitmap(mDatas.get(1));
                }
                if (filePathList.size() > 2) {
                    iv_img_03.setImageBitmap(mDatas.get(2));
                }
            }
            cam.stopPreview();
            cam.startPreview();
        }

    };

    @Override
    public void onClick(View view) {
        for (int i = 0; i < filePathList.size(); i++) {
            switch (view.getId()) {
                case R.id.luyna:
                    if (filePathList.size() < 0) {
                    } else {
                        Intent intent = new Intent(MyCameraDemo.this, ViewPagerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("filePath", (Serializable) filePathList);
                        Log.e("filePathList-----", "" + filePathList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    break;
                case R.id.iv_img_01:
                    DeleteImage(filePathList.get(i));
                    iv_img_01.setImageBitmap(null);
                    break;
                case R.id.iv_img_02:
                    DeleteImage(filePathList.get(i));
                    iv_img_02.setImageBitmap(null);
                    break;
                case R.id.iv_img_03:
                    DeleteImage(filePathList.get(i));
                    iv_img_03.setImageBitmap(null);
                    break;
            }
        }
    }
    //删除路径下的图片
    private void DeleteImage(String imgPath) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=?",
                new String[] { imgPath }, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }

        if (result) {
            filePathList.remove(imgPath);
            Toast.makeText(MyCameraDemo.this, "删除成功", Toast.LENGTH_LONG).show();
        }
    }
//设置拍照时--是否有声音
//    private ShutterCallback sc = new ShutterCallback() {
//        @Override
//        public void onShutter() {
//            // 按下快门之后进行的操作
//        }
//    };
//    private PictureCallback pc = new PictureCallback() {
//
//        @Override
//        public void onPictureTaken(byte[] data, Camera camera) {
//
//        }
//
//    };


    private void save(Bitmap bitmap, String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos); // 向缓冲区之中压缩图片
            bos.flush();
            bos.close();
            Toast.makeText(MyCameraDemo.this,
                    "拍照成功，照片已保存在" + fileName + "文件之中！", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            Toast.makeText(MyCameraDemo.this, "拍照失败！", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    // 控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    // 实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod(
                    "setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    private Camera deal2(Camera mCamera) {
        // 设置camera预览的角度，因为默认图片是倾斜90度的
        // mCamera.setDisplayOrientation(90);

        int PreviewWidth = 0;
        int PreviewHeight = 0;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);// 获取窗口的管理器
        Display display = wm.getDefaultDisplay();// 获得窗口里面的屏幕
        Camera.Parameters parameters = mCamera.getParameters();
        // parameters.setFlashMode(Parameters.FLASH_MODE_TORCH); //开启闪光灯,支持
        setDispaly(parameters, mCamera);
        // parameters.setRotation(90);
        // parameters.setPreviewFrameRate(3);// 每秒3帧 每秒从摄像头里面获得3个画面,
        // 某些机型（红米note2）不支持
        parameters.setPictureFormat(PixelFormat.JPEG);// 设置照片输出的格式
        parameters.set("jpeg-quality", 100);// 设置照片质量
        try {
            // 选择合适的预览尺寸
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if (sizeList.size() > 1) {
                Iterator<Camera.Size> itor = sizeList.iterator();
                while (itor.hasNext()) {
                    Camera.Size cur = itor.next();
                    if (cur.width >= PreviewWidth
                            && cur.height >= PreviewHeight) {
                        PreviewWidth = cur.width;
                        PreviewHeight = cur.height;
                        break;
                    }
                }
            }
            parameters.setPreviewSize(PreviewWidth, PreviewHeight); // 获得摄像区域的大小
            parameters.setPictureSize(PreviewWidth, PreviewHeight); // 获得保存图片的大小
            // parameters.setPreviewSize(display.getWidth(),
            // display.getWidth()); // 获得摄像区域的大小
            // parameters.setPictureSize(display.getWidth(),
            // display.getWidth());// 设置拍出来的屏幕大小

        } catch (Exception e) {
            Log.e("MyCameraDemo", e.toString());
        }
        try {
            cam.setPreviewDisplay(MyCameraDemo.this.holder);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mCamera.setParameters(parameters);// 把上面的设置 赋给摄像头
        mCamera.startPreview();// 开始预览
        mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
        previewRunning = true;
        return mCamera;
    }

    /**
     * 从给定路径加载图片
     */
    public Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
    public Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath);
        } else {
            Bitmap bm = loadBitmap(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                // int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                // ExifInterface.ORIENTATION_NORMAL);
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_FLIP_VERTICAL);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    /**
     * 将图片的旋转角度置为0  ，此方法可以解决某些机型拍照后图像，出现了旋转情况
     *
     * @param path
     * @return void
     * @Title: setPictureDegreeZero
     * @date 2012-12-10 上午10:54:46
     */
    private void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

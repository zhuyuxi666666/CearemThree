package com.example.administrator.cearemthree;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Cream extends AppCompatActivity implements View.OnClickListener {
    Button btn2, cancels, next_cream;
    RelativeLayout cccccccccl = null;
    RelativeLayout re_01, re_02, re_03;

    // camera 类
    private Camera camera = null;
    // 继承surfaceView的自定义view 用于存放照相的图片
    private Cream.CameraView cv = null;
    List<Drawable> dList = new ArrayList<>();
    // 回调用的picture，实现里边的onPictureTaken方法，其中byte[]数组即为照相后获取到的图片信息
    private Camera.PictureCallback picture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            cccccccccl.removeAllViews();
            cv = new CameraView(Cream.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
            cccccccccl.addView(cv, params);

            // 主要就是将图片转化成drawable，设置为固定区域的背景（展示图片），当然也可以直接在布局文件里放一个surfaceView供使用。
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            Drawable d = BitmapDrawable.createFromStream(bais, Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/img.jpeg");

            dList.add(d);
            Log.e("dList-----------", "" + dList.size());
            re_01.setBackgroundDrawable(dList.get(0));
            if (dList.size() > 1) {
                re_02.setBackgroundDrawable(dList.get(1));
            }
            if (dList.size() > 2) {
                re_03.setBackgroundDrawable(dList.get(2));
            }

            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.cream);
        cccccccccl = (RelativeLayout) findViewById(R.id.cameraView);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        cancels = (Button) findViewById(R.id.cancels);
        cancels.setOnClickListener(this);
        next_cream = (Button) findViewById(R.id.next_cream);
        next_cream.setOnClickListener(this);
        re_01 = (RelativeLayout) findViewById(R.id.re_01);
        re_02 = (RelativeLayout) findViewById(R.id.re_02);
        re_03 = (RelativeLayout) findViewById(R.id.re_03);
        //---------------------
        cccccccccl.removeAllViews();
        cv = new CameraView(Cream.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        cccccccccl.addView(cv, params);
        //---------------------------
    }


    //主要的surfaceView，负责展示预览图片，camera的开关
    class CameraView extends SurfaceView {

        //
        private SurfaceHolder holder = null;


        public CameraView(Context context) {
            super(context);
            holder = this.getHolder();

            holder.addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                                           int width, int height) {
//                    Camera.Parameters parameters = camera.getParameters();
//                    //以下注释掉的是设置预览时的图像以及拍照的一些参数
//                     parameters.setPictureFormat(PixelFormat.JPEG);
//                     parameters.setPreviewSize(parameters.getPictureSize().width,
//                     parameters.getPictureSize().height);
//                     parameters.setFocusMode("auto");
//                     parameters.setPictureSize(width, height);
//                    camera.setParameters(parameters);
                    camera.startPreview();
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    camera = Camera.open();//获取相机

                    try {
                        //设置camera预览的角度，因为默认图片是倾斜90度的
                        camera.setDisplayOrientation(90);
                        //设置holder主要是用于surfaceView的图片的实时预览，以及获取图片等功能，可以理解为控制camera的操作..
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        camera.release();
                        camera = null;
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    //顾名思义可以看懂
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn2:
                camera.takePicture(null, null, picture);
                if (dList.size() >= 3) {
                    Toast.makeText(getApplicationContext(), "最多拍3张照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancels:
                finish();
                break;
            case R.id.next_cream:
                if (dList.size() < 3) {
                    Toast.makeText(getApplicationContext(), "需要拍摄3张照片！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "举报成功！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

package com.example.administrator.cearemthree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private List<Bitmap> mDatas = new ArrayList<>();
    private RecycleView_Adapter recycleView_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        Intent intent = getIntent();
        List<String> filePath = (List<String>) intent.getSerializableExtra("filePath");
        Log.e("filePath===========", "" + filePath);
        List<String> mFileList=new ArrayList<>();
        for (int i=0;i<filePath.size();i++){

            mFileList.add(filePath.get(i));
                Bitmap bitmap_01 = BitmapFactory.decodeFile(filePath.get(i));
                mDatas.add(bitmap_01);
                Log.e("mDatas-----数量", "" + mDatas.size());
                mRecycleView = (RecyclerView) findViewById(R.id.mRecycleview);
                recycleView_adapter = new RecycleView_Adapter(mDatas);
                mRecycleView.setAdapter(recycleView_adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecycleView.setLayoutManager(linearLayoutManager);
            }
        }
//        File mFile_01 = new File(filePath.get(0));
//        File mFile_02 = new File(filePath.get(1));
//        File mFile_03 = new File(filePath.get(2));
        //若该文件存在

            //      调用按钮返回事件回调的方法
//            recycleView_adapter.buttonSetOnclick(new RecycleView_Adapter.ButtonInterface() {
//                @Override
//                public void onclick(View view, int position) {
//                    Toast.makeText(ViewPagerActivity.this, "删除了条目："+position, Toast.LENGTH_SHORT).show();
////                    recycleView_adapter.removeData(position);
//
//                }
//            });




}

package com.example.administrator.cearemthree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    // 按钮 布局等定义，不作赘述
    Button btn1,btn2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                Intent intent = new Intent(MainActivity.this, Cream.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                Intent intent2 = new Intent(MainActivity.this, MyCameraDemo.class);
                startActivity(intent2);
                break;
        }
    }
}

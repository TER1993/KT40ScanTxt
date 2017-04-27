package com.speedata.kt40scantxt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //两个按钮分别是单个与计数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_one).setOnClickListener(this);
        findViewById(R.id.iv_count).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_one:
                Intent intent1 = new Intent(MainActivity.this, OneActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_count:
                Intent intent2 = new Intent(MainActivity.this, CountActivity.class);
                startActivity(intent2);
                break;
        }
    }
}

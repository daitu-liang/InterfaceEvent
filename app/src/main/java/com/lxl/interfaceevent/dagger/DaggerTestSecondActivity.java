package com.lxl.interfaceevent.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lxl.interfaceevent.R;

public class DaggerTestSecondActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_test_second);
        tv = (TextView) findViewById(R.id.tv);
//        tv.setText("注解值："+menu.toString());
    }
}

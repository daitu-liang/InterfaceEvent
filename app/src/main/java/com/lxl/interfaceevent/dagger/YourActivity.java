package com.lxl.interfaceevent.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lxl.interfaceevent.R;
import com.lxl.interfaceevent.dagger.di.DaggerAppComponent;
import com.lxl.interfaceevent.dagger.module.PersonBean;

import javax.inject.Inject;

public class YourActivity extends AppCompatActivity {

    private static final String TAG = "YourActivity";
    private TextView tv;


    @Inject
    PersonBean personBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DaggerAppComponent.create().injectFirstActivity(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        tv = (TextView) findViewById(R.id.tv);

        Log.i(TAG, "chef.cook=" + personBean.getPerName());
//        Log.i(TAG, "chef.cook=" + cookModules.providerMenus().toString());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("-->=" + personBean.getPerName() + personBean.getCarInstance().getCarName());
                Log.i(TAG, "chef.cook=" + personBean.getPerName() + personBean.getCarInstance().getCarName());

//                startActivity(new Intent(YourActivity.this, DaggerTestSecondActivity.class));
            }
        });

        findPeople();
        addNunm(1);
    }

    private String addNunm(int index) {
        return "你大爷的" + index;
    }

    private String findPeople() {
        return "心情不美丽";
    }


}

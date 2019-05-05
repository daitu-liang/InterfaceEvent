package com.lxl.interfaceevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SecondActivity extends AppCompatActivity {

    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        EventBus.getDefault().register(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               EventManager.getInstance().postMsg("kakaxi");
//               EventManager.getInstance().postMsg("kakaxi",new UserInfo("pppp","333333"));

//               UserInfo us = EventManager.getInstance().postMsg("kakaxi", new UserInfo("uupppp", "333333444"), UserInfo.class);
//               Log.i("==MainActivity","返回对象参数输出"+us.toString());

//               EventManager.getInstance().post(new ToastEvent("SecondActivity是2界面"));
//                EventBus.getDefault().post(new ToastEvent("我是个1"));

                EventBusUtils.sendEvent(new ToastEvent("kakkxi"));

                int  num=66;
                String numStr="66";

                Log.i("==MainActivity","numStr.equals(num)="+numStr.equals(num));
                Log.i("==MainActivity","numStr.equals(String.valueOf(num))="+numStr.equals(String.valueOf(num)));
                Log.i("==MainActivity","num==Integer.parseInt(numStr)="+(num==Integer.parseInt(numStr)));

            }
        });
        button2=(Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ToastEvent("我是个2"));
//               EventManager.getInstance().postMsg("kakaxi");
//               EventManager.getInstance().postMsg("kakaxi",new UserInfo("pppp","333333"));

//               UserInfo us = EventManager.getInstance().postMsg("kakaxi", new UserInfo("uupppp", "333333444"), UserInfo.class);
//               Log.i("==MainActivity","返回对象参数输出"+us.toString());

//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        EventManager.getInstance().post(new ToastEvent("--SecondActivity是2界面=Thread"));
//
//                    }
//                }.start();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, ThreeActivity.class));

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public UserInfo getSeconde(UserInfo userInfo){
        button2.setText("参数值="+userInfo.toString());
        Log.d("==MainActivity", "SecondActivity-getSeconde: " +userInfo.toString() );

        return new UserInfo("重新设置usinfo",userInfo.getPwd());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.fuicuiedu.xc.easyshop_20170413.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fuicuiedu.xc.easyshop_20170413.R;
import com.fuicuiedu.xc.easyshop_20170413.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170413.model.CachePreferences;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activityUtils = new ActivityUtils(this);

        //判断用户是否登录，自动登录
        if (CachePreferences.getUser().getName() != null) {
            activityUtils.startActivity(MainActivity.class);
            finish();
            return;
        }


        // TODO: 2017/4/14 0014 环信登录相关（账号冲突踢出）

        //1.5s跳转到主页
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //跳转到主页
                activityUtils.startActivity(MainActivity.class);
                finish();
            }
        }, 1500);
    }
}

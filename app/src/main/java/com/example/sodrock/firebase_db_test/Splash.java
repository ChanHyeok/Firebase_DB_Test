package com.example.sodrock.firebase_db_test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * @Project : Firebase_DB_Test
 * @Author : ChanHyeok Jeong
 * @Date :  2017-01-26.
 * @Usage : splash 로딩화면
 */

public class Splash extends Activity{
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                finish();
            }
        }, 3000); // 3초 뒤 액티비티 종료
    }
}

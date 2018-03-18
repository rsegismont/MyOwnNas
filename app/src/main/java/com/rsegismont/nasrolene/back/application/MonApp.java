package com.rsegismont.nasrolene.back.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Rolène on 16/03/2018.
 */

public class MonApp extends Application {

        private static MonApp instance;

        public static MonApp getInstance() {
            return instance;
        }

        public static  Context getContext(){
            return instance.getApplicationContext();
            // or return instance.getApplicationContext();
        }

        @Override
        public void onCreate() {
            instance = this;
            super.onCreate();
        }



}

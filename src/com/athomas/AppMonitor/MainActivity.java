package com.athomas.AppMonitor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


/**
 * android.permission.DUMP is a system permission.
 * You must set the permission manually by typing: adb shell pm grant com.athomas.AppMonitor android.permission.DUMP
 */
public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AppMonitorService.class);
        intent.putExtra(AppMonitorService.EXTRA_PACKAGE_NAME, "com.jb.gosms");
        startService(intent);
    }
}
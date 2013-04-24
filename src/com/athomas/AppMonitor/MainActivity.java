package com.athomas.AppMonitor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * android.permission.DUMP is a system permission.
 * You must set the permission manually by typing: adb shell pm grant com.athomas.AppMonitor android.permission.DUMP
 */
public class MainActivity extends Activity {

    private EditText packageNameField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        packageNameField = (EditText) findViewById(R.id.package_name);

        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = packageNameField.getText().toString();
                if (TextUtils.isEmpty(packageName)) {
                    Toast.makeText(MainActivity.this, "The package name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, AppMonitorService.class);
                    intent.putExtra(AppMonitorService.EXTRA_PACKAGE_NAME, packageName);
                    startService(intent);
                }
            }
        });
    }
}
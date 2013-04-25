package com.athomas.appmonitor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.athomas.appmonitor.event.StopServiceEvent;


/**
 * android.permission.DUMP is a system permission.
 * You must set the permission manually by typing: adb shell pm grant com.athomas.appmonitor android.permission.DUMP
 */
public class MainActivity extends Activity {

    private EditText packageNameField;
    private EditText intervalField;
    private EditText topProcessesField;

    private boolean permissionGranted;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageNameField = (EditText) findViewById(R.id.package_name);
        intervalField = (EditText) findViewById(R.id.interval);
        topProcessesField = (EditText) findViewById(R.id.top_processes);

        // check permission
        int result = getPackageManager().checkPermission(Manifest.permission.DUMP, getPackageName());
        if (result == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
        } else {
            displayErrorMessage();
        }

        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!permissionGranted) {
                    displayErrorMessage();
                    return;
                }

                String packageName = packageNameField.getText().toString();
                if (TextUtils.isEmpty(packageName)) {
                    Toast.makeText(MainActivity.this, "The package name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, AppMonitorService.class);
                    intent.putExtra(AppMonitorService.EXTRA_PACKAGE_NAME, packageName);

                    String intervalString = intervalField.getText().toString();
                    if (!TextUtils.isEmpty(intervalString)) {
                        int interval = Integer.parseInt(intervalString);
                        intent.putExtra(AppMonitorService.EXTRA_INTERVAL, interval);
                    }

                    String topProcessesString = topProcessesField.getText().toString();
                    if (!TextUtils.isEmpty(topProcessesString)) {
                        int topProcesses = Integer.parseInt(topProcessesString);
                        intent.putExtra(AppMonitorService.EXTRA_TOP_PROCESSES, topProcesses);
                    }

                    startService(intent);
                }
            }
        });

        Button stopButton = (Button) findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionGranted) {
                    displayErrorMessage();
                    return;
                }
                BusProvider.get().post(new StopServiceEvent());
            }
        });
    }

    public void displayErrorMessage() {
        Toast.makeText(this, "The android.permission.DUMP must be granted before.", Toast.LENGTH_SHORT).show();
    }
}
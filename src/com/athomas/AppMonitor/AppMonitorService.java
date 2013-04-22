package com.athomas.AppMonitor;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.athomas.AppMonitor.event.RemoveFileEvent;
import com.athomas.AppMonitor.event.StopServiceEvent;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AppMonitorService extends Service {

    private final static String TAG = AppMonitorService.class.getSimpleName();
    private final static int DELAY = 1000;

    private Handler handler = new Handler();
    private FileManager fileManager = new FileManager();

    private WindowManager windowManager;
    private TextView resultView;

    private Runnable repetitiveTask = new Runnable() {
        @Override
        public void run() {
            // display cpu info + battery level
            String result = new StringBuilder()
                    .append(Dumpsys.$().cpuInfo().lines(4).exec())
                    .append(Dumpsys.$().battery().grep("level").exec())
                    .toString();
            resultView.setText(result);

            // save values
            String cpu = Dumpsys.$().cpuInfo().grep("lifeisbetteron.com").regexp("([\\d.]*)%").exec();
            String battery = Dumpsys.$().battery().grep("level").regexp("level: ([\\d.]*)").exec();
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            fileManager.append(currentTime + "/" + cpu + "/" + battery);

            // get the actual time
            handler.postDelayed(this, DELAY);
        }
    };

    @Subscribe
    public void handleStopService(StopServiceEvent event) {
        stopSelf();
    }

    @Subscribe
    public void handleRemoveFile(RemoveFileEvent event) {
        fileManager.removeFile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // add view on top of other apps

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        resultView = new TextView(this);

        resultView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.LEFT;

        resultView.setTextSize(6);
        windowManager.addView(resultView, params);

        // start repetitive task
        handler.post(repetitiveTask);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(resultView != null) {
            windowManager.removeView(resultView);
        }

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}

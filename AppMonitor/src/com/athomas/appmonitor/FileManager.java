package com.athomas.appmonitor;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private static final String TAG = FileManager.class.getSimpleName();

    private String fileName = "monitor.trace";

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void removeFile() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDirectory, fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void append(String value) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String eol = System.getProperty("line.separator");

        File file = new File(externalStorageDirectory, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.append(value);
            fileWriter.append(eol);
            fileWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "error during file writing", e);
        }
    }

}

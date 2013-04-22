package com.athomas.AppMonitor;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dumpsys {

    private final static String TAG = Dumpsys.class.getSimpleName();
    private final static String DUMPSYS = "dumpsys";

    private String command;
    private String grepString;
    private int maxLines;
    private String regexpPattern;

    private Dumpsys() {
        // cannot be instantiated
    }

    public static Dumpsys $() {
        return new Dumpsys();
    }

    public Dumpsys cpuInfo() {
        command = DUMPSYS + " cpuinfo";
        return this;
    }

    public Dumpsys battery() {
        command = DUMPSYS + " battery";
        return this;
    }

    public Dumpsys grep(String grep) {
        if (grep == null || grep.isEmpty()) {
            throw new IllegalArgumentException("The grep argument cannot be null or empty");
        }
        grepString = grep;
        return this;
    }

    public Dumpsys lines(int lines) {
        if (lines <= 0) {
            throw new IllegalArgumentException("The number of lines must be superior than 0");
        }
        this.maxLines = lines;
        return this;
    }

    public Dumpsys regexp(String regexpPattern) {
        this.regexpPattern = regexpPattern;
        return this;
    }

    public String exec() {
        String eol = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process pr = runtime.exec(command);
            InputStream stream = pr.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count <= maxLines) {
                if (grepString != null && !line.contains(grepString)) {
                    // the line doesn't contains the grep string
                    continue;
                }
                count++;
                builder.append(line);
                builder.append(eol);
            }
        } catch (IOException e) {
            Log.e(TAG, "error during command execution", e);
        }

        String result = builder.toString();

        // apply regexp before
        if (regexpPattern != null) {
            Pattern pattern = Pattern.compile(regexpPattern);
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
                Log.d(TAG, "cool");
                return matcher.group(1);
            }
        }

        return result;
    }

}

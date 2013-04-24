package com.athomas.AppMonitor;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cmd {

    private final static String TAG = Cmd.class.getSimpleName();

    private String command;
    private String grepString;
    private int maxLines;
    private String regexpPattern;
    private int occurence = -1;

    private Cmd() {
        // cannot be instantiated
    }

    public static Cmd $() {
        return new Cmd();
    }

    public Cmd meminfo(String packageName) {
        command = "dumpsys meminfo " + packageName;
        return this;
    }

    public Cmd cpuinfo() {
        command = "dumpsys cpuinfo";
        return this;
    }

    public Cmd battery() {
        command = "dumpsys battery";
        return this;
    }

    public Cmd ps() {
        command = "ps";
        return this;
    }

    public Cmd grep(String grep) {
        if (grep == null || grep.isEmpty()) {
            throw new IllegalArgumentException("The grep argument cannot be null or empty");
        }
        grepString = grep;
        return this;
    }

    public Cmd lines(int lines) {
        if (lines <= 0) {
            throw new IllegalArgumentException("The number of lines must be superior than 0");
        }
        this.maxLines = lines;
        return this;
    }

    public Cmd regexp(String regexpPattern) {
        this.regexpPattern = regexpPattern;
        return this;
    }

    public Cmd regexp(String regexpPattern, int occurence) {
        this.regexpPattern = regexpPattern;
        this.occurence = occurence;
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

        // apply regexp before return
        if (regexpPattern != null) {
            Pattern pattern = Pattern.compile(regexpPattern);
            Matcher matcher = pattern.matcher(result);

            // return a specific occurence of the regexp
            if (occurence > -1) {
                for (int i = 0; i < occurence; i++) {
                    if (!matcher.find()) {
                        // occurence is too far
                        return null;
                    }
                }
                return matcher.group();
            }

            // return
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return result;
    }

}

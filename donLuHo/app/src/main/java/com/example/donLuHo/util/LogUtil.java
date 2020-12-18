package com.example.donLuHo.util;

import android.annotation.SuppressLint;

import com.example.donLuHo.Const;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 写log
 */

public class LogUtil {
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyyMMdd"); // 日志文件格式
    @SuppressLint({"SdCardPath", "SimpleDateFormat"})
    private static SimpleDateFormat LogSdf = new SimpleDateFormat(
            "HH:mm:ss"); // 日志的输出格式
    private static String LOGFILENAME = ".log"; // 本类输出的日志文件名称

    public static void print(final String tag, final String text) {
        L.e(tag,text);
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = LogSdf.format(nowtime)
                + " " + tag + " " + text;

        File logdir = new File(Const.ROOT_PATH);
        if (!logdir.exists()) {
            logdir.mkdirs();
        }
        File file = new File(Const.ROOT_PATH, needWriteFiel + LOGFILENAME);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

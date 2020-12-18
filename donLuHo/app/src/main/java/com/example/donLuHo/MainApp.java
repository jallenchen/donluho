package com.example.donLuHo;

import android.app.Application;

import com.example.donLuHo.http.HttpServiceGenerator;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class MainApp extends Application {
    private static MainApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LoggerSettings();
        HttpServiceGenerator.init(this, Const.API_HOST);

    }

    public static MainApp getInstance() {
        return mInstance;
    }

    private void LoggerSettings() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .methodOffset(0)
                .tag(getString(R.string.app_name))
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
}

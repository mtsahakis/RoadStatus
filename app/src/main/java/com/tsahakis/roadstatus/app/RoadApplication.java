package com.tsahakis.roadstatus.app;

import android.app.Application;
import android.util.Log;

import com.tsahakis.roadstatus.BuildConfig;
import com.tsahakis.roadstatus.injection.AppComponent;
import com.tsahakis.roadstatus.injection.AppModule;
import com.tsahakis.roadstatus.injection.DaggerAppComponent;

import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;

public class RoadApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) { // Timber logging
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        RxJavaPlugins.setErrorHandler(Timber::e);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }


    private static class ReleaseTree extends Timber.DebugTree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            switch (priority) {
                case Log.WARN:
                    Log.w(tag, message);
                    break;
                case Log.ERROR:
                    if (t == null) {
                        Log.e(tag, message);
                    } else {
                        Log.e(tag, message, t);
                    }
                    break;
                default:
                    Log.wtf(tag, message);
            }
        }
    }
}

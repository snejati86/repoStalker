package com.bypassmobile.octo.application;

import android.app.Application;

import com.bypassmobile.octo.di.AppComponent;
import com.bypassmobile.octo.di.DaggerAppComponent;
import com.bypassmobile.octo.di.SystemModule;

/**
 * Created by nejasix on 12/1/15.
 */
public class ByPassApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        appComponent = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if ( appComponent == null ){
            appComponent = DaggerAppComponent.builder().systemModule(new SystemModule(this)).build();
        }

    }

    public AppComponent getAppComponent()
    {
        return appComponent;
    }

    private void setAppComponent(AppComponent app)
    {
        if ( appComponent == null ){
            appComponent = app;
        }
    }
}

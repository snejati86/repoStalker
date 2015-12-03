package com.bypassmobile.octo.activities;

import android.app.Activity;
import android.os.Bundle;

import com.bypassmobile.octo.application.ByPassApplication;
import com.bypassmobile.octo.di.AppComponent;

/**
 * Created by nejasix on 12/2/15.
 */
public abstract class BaseActivity extends Activity {

    protected AppComponent appComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appComponent = ((ByPassApplication)getApplication()).getAppComponent();
        inject(appComponent);
    }

    /**
     * Marker to implement get around the lack of base class injection.
     * @param appComponent
     */
    abstract void inject(AppComponent appComponent);
}

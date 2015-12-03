package com.bypassmobile.octo.di;

import android.content.Context;
import android.net.ConnectivityManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nejasix on 12/1/15.
 */
@Module
public class SystemModule {

    private final Context mContext;

    public SystemModule(Context context)
    {
        this.mContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext()
    {
        return mContext;
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager(Context context)
    {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }
}

package com.bypassmobile.octo.di;

import com.bypassmobile.octo.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Application component to construct dependecies.
 */
@Component(modules = {RestModule.class,ImageModule.class,SystemModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity mainActivity);
}

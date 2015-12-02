package com.bypassmobile.octo.di;

import android.content.Context;

import com.bypassmobile.octo.image.ImageCache;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nejasix on 12/1/15.
 */
@Module
public class ImageModule {

    @Provides
    @Singleton
    public Picasso providePicasso(Context context)
    {
        Picasso pi = new Picasso.Builder(context).memoryCache(new ImageCache(1024 * 8000, context)).build();
        pi.setDebugging(true);
        return pi;
    }
}

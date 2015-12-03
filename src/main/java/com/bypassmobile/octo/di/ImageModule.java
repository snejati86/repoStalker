package com.bypassmobile.octo.di;

import android.content.Context;

import com.bypassmobile.octo.image.ImageCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nejasix on 12/1/15.
 */
@Module
public class ImageModule {

    private static final int CACHE_SIZE = 1024 * 4000 ;

    @Provides
    @Singleton
    public Picasso providePicasso(Context context,ImageCache cache,OkHttpDownloader downloader)
    {
        Picasso pi = new Picasso.Builder(context).downloader(downloader).memoryCache(cache).indicatorsEnabled(true).build();
        return pi;
    }

    @Provides
    public ImageCache providesCache(Context context)
    {
        return new ImageCache(CACHE_SIZE,context);
    }

    @Provides
    public OkHttpClient provideOkHttpClient()
    {
        return new OkHttpClient();
    }

    @Provides OkHttpDownloader provideDownloader(OkHttpClient httpClient)
    {
        return new OkHttpDownloader(httpClient);
    }
}

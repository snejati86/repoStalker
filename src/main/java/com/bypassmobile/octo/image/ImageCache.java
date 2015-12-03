package com.bypassmobile.octo.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.squareup.picasso.Cache;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageCache implements Cache{

    private LruCache<String,Bitmap> mCache;

    private DiskLruImageCache diskCache;

    public ImageCache(int cachingSize,Context context)
    {
        mCache = new LruCache<String, Bitmap>(cachingSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getByteCount() / 1024);
            }
        };
        diskCache = new DiskLruImageCache(context,"Mine",Utils.IO_BUFFER_SIZE, Bitmap.CompressFormat.JPEG,70);

    }

    @Override
    public Bitmap get(String stringResource) {
        Bitmap l1 =  mCache.get(stringResource);
        if ( l1 == null)
        {
            return diskCache.getBitmap(stringResource);
        }
        else
        {
            return l1;
        }
    }

    @Override
    public void set(String stringResource, Bitmap bitmap) {
        mCache.put(stringResource,bitmap);
        diskCache.put(stringResource,bitmap);
    }

    @Override
    public int size() {
        return mCache.size();
    }

    @Override
    public int maxSize() {
        return mCache.maxSize();
    }

    @Override
    public void clear() {
        mCache.evictAll();
    }

    @Override
    public void clearKeyUri(String keyPrefix) {

    }
}

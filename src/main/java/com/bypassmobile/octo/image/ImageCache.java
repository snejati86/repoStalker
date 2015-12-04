package com.bypassmobile.octo.image;


import android.graphics.Bitmap;
import android.util.LruCache;

import com.squareup.picasso.Cache;


public class ImageCache implements Cache{

    private LruCache<String,Bitmap> mCache;

    public ImageCache(int cachingSize)
    {
        mCache = new LruCache<String, Bitmap>(cachingSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getByteCount() / 1024);
            }
        };

    }

    @Override
    public Bitmap get(String stringResource) {
        return mCache.get(stringResource);
    }

    @Override
    public void set(String stringResource, Bitmap bitmap) {
         mCache.put(stringResource,bitmap);
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

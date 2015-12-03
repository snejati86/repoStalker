package com.bypassmobile.octo.di;

import com.bypassmobile.octo.rest.GithubEndpoint;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by nejasix on 12/1/15.
 */
@Module
public class RestModule {

    @Provides
    @Singleton
    public GithubEndpoint provideGithubEndpoint(RestAdapter restAdapter)
    {
        return restAdapter.create(GithubEndpoint.class);
    }

    @Provides
    @Singleton
    public RestAdapter provideRestAdapter()
    {
        return new RestAdapter.Builder()
                .setEndpoint(GithubEndpoint.SERVER)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }
}

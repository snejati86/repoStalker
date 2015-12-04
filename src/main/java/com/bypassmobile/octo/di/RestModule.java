package com.bypassmobile.octo.di;

import com.bypassmobile.octo.rest.GithubEndpoint;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
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
                .setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", "token b0ba550794a1019fb4dc01045bb9a1dbd8f03471");
                    }
                })
                .build();
    }
}

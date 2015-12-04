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
                        request.addHeader("Authorization", "token 9b36c263c101a02674929f096c0db31fc5729536");
                    }
                })
                .build();
    }
}

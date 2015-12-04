package com.bypassmobile.octo.di;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bypassmobile.octo.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nejasix on 12/3/15.
 */
@Module
public class AnimationModule {

    @Provides
    @Named("Fade_in")
    public Animation provideFadeInAnimation(Context context)
    {
        return AnimationUtils.loadAnimation(context, R.anim.fade_in_animation);
    }

    @Provides
    @Named("Fade_out")
    public Animation provideFadeOutAnimation(Context context)
    {
        return AnimationUtils.loadAnimation(context,R.anim.fade_out_animation);
    }
}

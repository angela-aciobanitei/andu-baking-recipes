package com.ang.acb.bakeit.di;

import android.app.Application;

import com.ang.acb.bakeit.BakingApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
            AndroidInjectionModule.class,
            AppModule.class,
            BuildersModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(BakingApplication bakingApplication);
}

package com.ang.acb.bakeit.di;

import android.app.Application;

import com.ang.acb.bakeit.BakingApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
            AndroidSupportInjectionModule.class,
            AppModule.class,
            MainActivityModule.class,
            DetailsActivityModule.class
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

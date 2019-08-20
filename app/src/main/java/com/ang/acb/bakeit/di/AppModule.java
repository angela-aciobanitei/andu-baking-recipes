package com.ang.acb.bakeit.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.ang.acb.bakeit.data.local.AppDatabase;
import com.ang.acb.bakeit.data.local.LocalRecipeDataSource;
import com.ang.acb.bakeit.data.local.RecipeDao;
import com.ang.acb.bakeit.data.remote.ApiClient;
import com.ang.acb.bakeit.data.remote.ApiService;
import com.ang.acb.bakeit.data.remote.LiveDataCallAdapterFactory;
import com.ang.acb.bakeit.data.remote.RemoteRecipeDataSource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.utils.AppExecutors;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "cake.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    RecipeDao provideRecipeDao(AppDatabase appDatabase) {
        return appDatabase.recipeDao();
    }

    @Singleton
    @Provides
    ApiService provideApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ApiService.class);
    }

    @Provides
    @Singleton
    LocalRecipeDataSource provideLocalRecipeDataSource(AppDatabase appDatabase){
        return LocalRecipeDataSource.getInstance(appDatabase);
    }

    @Provides
    @Singleton
    RemoteRecipeDataSource provideRemoteRecipeDataSource(){
        ApiService apiService = ApiClient.getInstance();
        AppExecutors executors = AppExecutors.getInstance();
        return RemoteRecipeDataSource.getInstance(apiService, executors);
    }

    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(AppDatabase database) {
        // FIXME
        return RecipeRepository.getInstance(
                provideLocalRecipeDataSource(database),
                provideRemoteRecipeDataSource(),
                AppExecutors.getInstance());
    }
}

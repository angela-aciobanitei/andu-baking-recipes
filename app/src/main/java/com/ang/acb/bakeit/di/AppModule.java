package com.ang.acb.bakeit.di;

import android.app.Application;

import androidx.room.Room;

import com.ang.acb.bakeit.data.local.AppDatabase;
import com.ang.acb.bakeit.data.local.RecipeDao;
import com.ang.acb.bakeit.data.remote.ApiService;
import com.ang.acb.bakeit.data.remote.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "recipes.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    RecipeDao provideRecipeDao(AppDatabase database) {
        return database.recipeDao();
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                // Add a logging interceptor to our OkHttp client.
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor()).build())
                // Configure which converter is used for the data serialization.
                // Gson is a Java serialization/deserialization library to convert
                // Java Objects into JSON and back.
                .addConverterFactory(GsonConverterFactory.create())
                // Add a call adapter factory for supporting service method
                // return types other than Retrofit2.Call. We will use a custom
                // Retrofit adapter that converts the Retrofit2.Call into a
                // LiveData of ApiResponse.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ApiService.class);
    }
}

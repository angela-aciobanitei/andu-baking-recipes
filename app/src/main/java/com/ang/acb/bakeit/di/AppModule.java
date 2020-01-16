package com.ang.acb.bakeit.di;

import android.app.Application;

import androidx.room.Room;

import com.ang.acb.bakeit.data.local.AppDatabase;
import com.ang.acb.bakeit.data.local.RecipeDao;
import com.ang.acb.bakeit.data.remote.ApiService;
import com.ang.acb.bakeit.utils.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * We annotate this class with @Module to signal to Dagger to search within the available
 * methods for possible instance providers. The methods that will actually expose available
 * return types should also be annotated with the @Provides annotation. The @Singleton
 * annotation also signals to the Dagger compiler that the instance should be created
 * only once in the application.
 *
 * See: https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 */

@Module(includes = ViewModelModule.class)
class AppModule {

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
    HttpLoggingInterceptor provideLoggingInterceptor() {
        // Retrofit 2 completely relies on OkHttp for any network operation.
        // Since logging isn’t integrated by default anymore in Retrofit 2,
        // we need to add a logging interceptor for OkHttp.
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        // Set the desired log level. Warning: using the HEADERS or BODY levels
        // have the potential to leak sensitive information such as "Authorization"
        // or "Cookie" headers and the contents of request and response bodies.
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);

        return loggingInterceptor;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        // Add the logging interceptor to our OkHttp client.
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    ApiService provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                // Configure which converter is used for the data serialization.
                // Gson is a Java serialization/deserialization library to convert
                // Java Objects into JSON and back.
                .addConverterFactory(GsonConverterFactory.create())
                // Add a call adapter factory for supporting service method
                // return types other than Retrofit2.Call. We will use a custom
                // Retrofit adapter that converts the Retrofit2.Call into a
                // LiveData of ApiResponse.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(client)
                .build()
                .create(ApiService.class);
    }
}

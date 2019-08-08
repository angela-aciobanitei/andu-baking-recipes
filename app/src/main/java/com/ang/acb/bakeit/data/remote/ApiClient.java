package com.ang.acb.bakeit.data.remote;

import com.ang.acb.bakeit.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiService sInstance;
    private static final OkHttpClient client;
    private static final Object sLock = new Object();

    static {
        // Retrofit 2 completely relies on OkHttp for any network operation.
        // Since logging isn’t integrated by default anymore in Retrofit 2,
        // we need to add a logging interceptor for OkHttp.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Set the desired log level. Warning: using the HEADERS or BODY levels
        // have the potential to leak sensitive information such as "Authorization"
        // or "Cookie" headers and the contents of request and response bodies.
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        // Add the logging and the request interceptors to our OkHttp client.
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    // Creates the Retrofit instance.
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
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
                .build();
    }

    // Returns the single instance of this class, creating it if necessary.
    public static ApiService getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = getRetrofitInstance().create(ApiService.class);
            }
            return sInstance;
        }
    }
}

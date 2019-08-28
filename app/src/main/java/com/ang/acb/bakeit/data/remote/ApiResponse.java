package com.ang.acb.bakeit.data.remote;

import java.io.IOException;

import androidx.annotation.Nullable;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
 *
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
public class ApiResponse<T> {

    // HTTP status code
    public final int code;

    // Data
    @Nullable
    public final T body;

    // Error message
    @Nullable
    public final String errorMessage;


    public ApiResponse(Throwable throwable) {
        code = 500;
        body = null;
        errorMessage = throwable.getMessage();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Timber.e(ignored, "Error while parsing response");
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    /**
     * Note: All HTTP response status codes are separated into five classes (or categories).
     * The first digit of the status code defines the class of response. The last two digits
     * do not have any class or categorization role. There are five values for the first digit:
     *
     * 1xx (Informational): The request was received, continuing process.
     * 2xx (Successful): The request was successfully received, understood and accepted.
     * 3xx (Redirection): Further action needs to be taken in order to complete the request.
     * 4xx (Client Error): The request contains bad syntax or cannot be fulfilled.
     * 5xx (Server Error): The server failed to fulfill an apparently valid request.
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }
}


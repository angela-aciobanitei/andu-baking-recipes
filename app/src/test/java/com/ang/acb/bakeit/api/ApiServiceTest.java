package com.ang.acb.bakeit.api;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.remote.ApiService;
import com.ang.acb.bakeit.data.remote.LiveDataCallAdapterFactory;
import com.ang.acb.bakeit.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ApiServiceTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ApiService service;
    private MockWebServer mockWebServer;

    @Before
    public void createService() throws IOException {
        mockWebServer = new MockWebServer();
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ApiService.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    // Helper methods
    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader())
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)));
    }

    @Test
    public void getAllRecipes() throws IOException, InterruptedException {
        enqueueResponse("all-recipes.json", Collections.emptyMap());
        List<Recipe> recipes = LiveDataTestUtil.getValue(service.getAllRecipes()).body;

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/baking.json"));

        assertThat(recipes.size(), is(4));

        Recipe recipe1 = recipes.get(0);
        assertThat(recipe1.getName(), is("Nutella Pie"));
        assertThat(recipe1.getServings(), is(8));
        assertThat(recipe1.getImage(), is(""));

        Recipe recipe2 = recipes.get(1);
        assertThat(recipe2.getName(), is("Brownies"));
        assertThat(recipe2.getServings(), is(8));
        assertThat(recipe2.getImage(), is(""));

        Recipe recipe3 = recipes.get(2);
        assertThat(recipe3.getName(), is("Yellow Cake"));
        assertThat(recipe3.getServings(), is(8));
        assertThat(recipe3.getImage(), is(""));

        Recipe recipe4 = recipes.get(3);
        assertThat(recipe4.getName(), is("Cheesecake"));
        assertThat(recipe4.getServings(), is(8));
        assertThat(recipe4.getImage(), is(""));
    }


}

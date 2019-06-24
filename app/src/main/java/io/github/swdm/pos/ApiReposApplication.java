package io.github.swdm.pos;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiReposApplication extends Application {
    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        // 어느 Activity에서나 API를 이용할 수 있도록, 이 클래스로 API를 이용한다
        setupAPIClient();
    }

    private void setupAPIClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://54.180.81.90:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
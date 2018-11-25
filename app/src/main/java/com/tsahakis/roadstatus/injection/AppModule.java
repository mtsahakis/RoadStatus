package com.tsahakis.roadstatus.injection;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsahakis.roadstatus.data.RoadApi;
import com.tsahakis.roadstatus.data.RoadService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private static final String API_URL = "https://api.tfl.gov.uk";

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    Context providesContext() {
        return mApplication;
    }

    @Provides
    Gson providesJsonParser() {
        return new GsonBuilder().create();
    }

    @Provides
    GsonConverterFactory providesGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    RxJava2CallAdapterFactory providesRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    HttpLoggingInterceptor providesLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    OkHttpClient providesOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    Retrofit providesRetrofit(OkHttpClient okHttpClient,
                              GsonConverterFactory gsonConverterFactory,
                              RxJava2CallAdapterFactory rxJava2CallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build();
    }

    @Provides
    RoadApi providesRoadApi(Retrofit retrofit) {
        return retrofit.create(RoadApi.class);
    }

    @Provides
    RoadService providesRoadService(RoadApi roadApi) {
        return new RoadService(roadApi);
    }
}

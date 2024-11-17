package com.example.banqueapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8082";
    private static Retrofit retrofitJson;
    private static Retrofit retrofitXml;
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance(String format ) {
        // Create logging interceptor
        String finalAcceptHeader = format.equals("application/xml") ? "application/xml" : "application/json";

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", finalAcceptHeader)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static CompteApi getApi(String format) {
        return getRetrofitInstance(format).create(CompteApi.class);
}}
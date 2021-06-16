package com.example.zemogatest.common.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.zemogatest.common.settings.Settings;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manager for Network operations.
 *
 * @author n.diazgranados
 */
public class NetworkManager {

    private final Settings settings;
    private final Context contextApp;
    private Retrofit retrofit = null;

    @Inject
    public NetworkManager(@ApplicationContext Context appContext, Settings settings) {
        this.settings = settings;
        this.contextApp = appContext;
        createDefaultRetrofit();
    }

    protected void createDefaultRetrofit() {
        if (retrofit == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(settings.getServiceTimeOut(), TimeUnit.SECONDS)
                    .connectTimeout(settings.getConnectionTimeOut(), TimeUnit.SECONDS)
                    .build();

            // Create a very simple REST adapter which points to the settings base URL.
            retrofit = new Retrofit.Builder()
                    .baseUrl(settings.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) contextApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public Retrofit getDefaultRetrofit() {
        return retrofit;
    }
}

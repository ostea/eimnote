package com.comtop.eimnote.http;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String TAG = RetrofitManager.class.getSimpleName();
    private static final String CLIENT_PROTOCOL = "TLS";
    private static final String CERTIFICATE_NAME = "derSmdpCert.crt";
    private final Gson gson;
    private final OkHttpClient client;

    public RetrofitManager(Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // gsonBuilder.registerTypeAdapter(Response.Status.class, new StatusSerializer());
        gson = gsonBuilder.create();
        client = new OkHttpClient.Builder()
                .addInterceptor(new RequestHeaderInterceptor())
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                // .sslSocketFactory(getSSLSocketFactory(context))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public <T> T createService(@NonNull String baseUrl, @NonNull Class<T> serviceClass) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(serviceClass);
    }

    private SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(
                    context.getAssets().open(CERTIFICATE_NAME));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance(CLIENT_PROTOCOL);
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e(TAG, "getSSLSocketFactory()", e);
        }
        return null;
    }


}

package com.example.retrofitlambda.util;

import android.util.Log;

import com.example.retrofitlambda.ResponseResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://chuumong.github.io/android/2017/01/13/Get-Started-With-Retrofit-2-HTTP-Client 싱글톤

public class ConnectRetrofit {

    static OkHttpClient okHttpClient;
    public Retrofit mRetrofit;
    public RetrofitAPI mRetrofitAPI;
    public ResponseResult Item;

    public String result;

    private static final ConnectRetrofit connectRetrofit = new ConnectRetrofit();

    public static ConnectRetrofit getInstance(){
        return connectRetrofit;
    }

    private ConnectRetrofit() {
        Log.d("ConnectRetrofit", " : 성공");
        init();
    }

    public void init(){
        okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://58.180.28.220:8000")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mRetrofitAPI =mRetrofit.create(RetrofitAPI.class);
    }

    public <T> void call(Call<T> call, final OnResultListener<T> listener){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                try {
                    if(response.isSuccessful()) {
                        listener.onResult( call, response);
                    }
                    else {
                        listener.onResult(call, null);
                    }
                } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                    Log.d("button2ERROR", e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                try {
                    listener.onResult(call, null);
                } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                    Log.d("button2ERROR", e.getMessage());
                }
            }
        });
    }






    public interface OnResultListener<T> {
        void onResult(Call<T> call, Response<T> response) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException;
    }

}

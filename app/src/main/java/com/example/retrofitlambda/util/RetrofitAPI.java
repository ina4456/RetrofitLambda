package com.example.retrofitlambda.util;


import com.example.retrofitlambda.ResponseResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    //@GET("comments")
    //    Call<List<Movie>> getComment(@Query("postId") int postId); //=http://jsonplaceholder.typicode.com/comments?postId=1

    // @FormUrlEncoded
   //@GET("auth/set")
   //     Call<ResponseBody> getComment();




    @POST("/auth/set")
    Call<ResponseResult> login(@Body HashMap<String, String> body);

    @POST("/alloc/request")
    Call<ResponseResult> requestBoarding(@Body HashMap<String, String> body);

    @POST("/alloc/cancel")
    Call<ResponseResult> cancelBoarding(@Body HashMap<String, String> body);

    @POST("/getinfo/carpos")
    Call<ResponseResult> carInfo(@Body HashMap<String, String> body);

    @POST("/getinfo/version")
    Call<ResponseResult> versionInfo(@Body HashMap<String, String> body);

    @POST("/getinfo/driver")
    Call<ResponseResult> driverInfo(@Body HashMap<String, String> body);

    @POST("/getinfo/callcheck")
    Call<ResponseResult> checkBoarding(@Body HashMap<String, String> body);

    @POST("/driver/review")
    Call<ResponseResult> setReview(@Body HashMap<String, String> body);




}

//https://academy.realm.io/kr/posts/droidcon-jake-wharton-simple-http-retrofit-2/
//https://lktprogrammer.tistory.com/177
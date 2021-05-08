package com.example.facmanager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    public static final String URL = "http://49.247.19.209/";


    @FormUrlEncoded
    @POST("/")
    Call<ResponseBody> login(@Field("username") String username, @Field("password") String password);

    @GET("attendance")
    Call<ResponseBody> attendance();

    @GET("attendance_on")
    Call<ResponseBody> attendance_on();

    @GET("attendance_off")
    Call<ResponseBody> attendance_off();

}

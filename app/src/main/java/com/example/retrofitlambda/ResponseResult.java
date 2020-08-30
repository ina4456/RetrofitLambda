package com.example.retrofitlambda;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseResult {
    //String isSuccessful, msg, callID, callDT, version, carNum, driverName, driverPhotoURL, carColor, callStatus;


    //Common
    @SerializedName("isSuccessful")
    @Expose
    public String isSuccessful;

    @SerializedName("msg")
    @Expose
    public String msg;

    @SerializedName("callID")
    @Expose
    public String callID;

    @SerializedName("callDT")
    @Expose
    public String callDT;

    @SerializedName("version")
    @Expose
    public String version;

    @SerializedName("carNum")
    @Expose
    public String carNum;

    @SerializedName("driverName")
    @Expose
    public String driverName;

    @SerializedName("carModel")
    @Expose
    public String carModel;

    @SerializedName("carColor")
    @Expose
    public String carColor;

    @SerializedName("callStatus")
    @Expose
    public String callStatus;

    @SerializedName("carLon")
    @Expose
    public String carLon;

    @SerializedName("carLat")
    @Expose
    public String carLat;



/*    //Login
    @SerializedName("allocRequest")
    @Expose
    public com.example.retrofitsingle.model.allocRequest allocRequest;*/

/*    //List
    @SerializedName("placeList")
    @Expose
    public ArrayList<Store> listData;*/

}

package com.example.retrofitlambda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofitlambda.util.AES256Util;
import com.example.retrofitlambda.util.ConnectRetrofit;
import com.example.retrofitlambda.util.RetrofitAPI;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv;
    Button btn, btnCancel, btnCarInfo, btnDriverInfo;
    ImageView img;
    public String callID, callDT, version, carNum, driverName, carModel, carColor, callStatus,carLon, carLat;
    String currentDate;
    String code="";

    HashMap<String, String> input;
    public ResponseResult Item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.InitializeView();
        this.SetListener();

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.KOREA);
        currentDate = dateFormat.format(mDate);

        try {
            requestBoardingMethod();
        } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e ) {
            Log.d("ResponseERROR", e.getMessage());
        }

    }


    public void InitializeView()
    {
        tv=findViewById(R.id.tv);
        btn=findViewById(R.id.btn);
        btnCancel=findViewById(R.id.btnCancel);
        btnCarInfo=findViewById(R.id.btnCarInfo);
        btnDriverInfo=findViewById(R.id.btnDriverInfo);
        img=findViewById(R.id.img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn:
                tv.setText("Test성공 : CallID는"+callID + "입니다");
                Log.d("3.버튼클릭 성공 :"," CallID는"+ callID+ "입니다");
                break;

            case R.id.btnCancel:
                try {
                    cancelBoardingMethod();
                    tv.setText("배차취소 완료");
                } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                    Log.d("button2ERROR", e.getMessage());
                }
                break;

            case R.id.btnCarInfo:
                try {
                    carInfoMethod();
                    tv.setText("현재 차의 위치는 위도:"+carLon+", 경도:"+carLat+" 입니다.");
                } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                    Log.d("button2ERROR", e.getMessage());
                }
                break;

            case R.id.btnDriverInfo:
                try {
                    driverInfoMethod();
                } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                    Log.d("button2ERROR", e.getMessage());
                }
                break;
        }
    }

    public void SetListener()
    {
        btn.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCarInfo.setOnClickListener(this);
        btnDriverInfo.setOnClickListener(this);
    }

    private void requestBoardingMethod() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        code = AES256Util.encode(currentDate);

        input = new HashMap<>();
        input.put("currentDT", currentDate);
        input.put("authCode", code);
        input.put("mobile", AES256Util.encode("01044561472"));
        input.put("posName", AES256Util.encode("인솔라인"));           //현재 고객 위치명
        input.put("posLon", AES256Util.encode("127.110580"));            //현재 고객 위도
        input.put("posLat", AES256Util.encode("37.402278"));            //현재 고객 경도
        input.put("posNameDetail", AES256Util.encode("판교예요"));     //현재 고객 위치 상세설명
        input.put("destLon", AES256Util.encode("127.112459"));           //목적지 위도
        input.put("destLat", AES256Util.encode("37.307630"));           //목적지 경도
        input.put("destination", AES256Util.encode("우리집"));       //목적지 위치명


        Call<ResponseResult> api = ConnectRetrofit.getInstance().mRetrofitAPI.requestBoarding(input);
        ConnectRetrofit.getInstance().call(api, ((call, response) -> {

            Item = response.body();
            if (Item.msg.equals("")) {
                if(Item.callID !=null) callID=AES256Util.decode(Item.callID);
                if(Item.callDT !=null) callDT=AES256Util.decode(Item.callDT);
                return;
            }else {
                Log.d("MsgERROR", AES256Util.decode(Item.msg)+ "입니다 : "+Item.isSuccessful);
                return;
            }
        }));
    }


    private void cancelBoardingMethod() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        code = AES256Util.encode(currentDate);

        input = new HashMap<>();
        input.put("currentDT", currentDate);
        input.put("authCode", code);
        input.put("mobile", AES256Util.encode("01044561472"));
        input.put("callID", callID);
        input.put("callDT", callDT);

        Call<ResponseResult> api = ConnectRetrofit.getInstance().mRetrofitAPI.requestBoarding(input);
        ConnectRetrofit.getInstance().call(api, ((call, response) -> {
            Item = response.body();
            if (Item.msg.equals("")) {
                Log.d("배차취소", "요청성공");
                return;
            }else {
                Log.d("MsgERROR", AES256Util.decode(Item.msg)+ "입니다 : "+Item.isSuccessful);
                return;
            }
        }));
    }

    private void carInfoMethod() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        code = AES256Util.encode(currentDate);

        input = new HashMap<>();
        input.put("currentDT", currentDate);
        input.put("authCode", code);
        input.put("mobile", AES256Util.encode("01044561472"));
        input.put("callID", callID);
        input.put("callDT", callDT);

        Call<ResponseResult> api = ConnectRetrofit.getInstance().mRetrofitAPI.requestBoarding(input);
        ConnectRetrofit.getInstance().call(api, ((call, response) -> {
            Item = response.body();
            if (Item.msg.equals("")) {
                Log.d("배차취소", "요청성공");
                if(Item.carLon !=null) carLon=AES256Util.decode(Item.carLon);
                if(Item.carLat !=null) carLat=AES256Util.decode(Item.carLat);
                return;
            }else {
                Log.d("MsgERROR", AES256Util.decode(Item.msg)+ "입니다 : "+Item.isSuccessful);
                return;
            }
        }));

    }

    private void driverInfoMethod() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        code = AES256Util.encode(currentDate);

        input = new HashMap<>();
        input.put("currentDT", currentDate);
        input.put("authCode", code);
        input.put("mobile", AES256Util.encode("01044561472"));
        input.put("callID", callID);
        input.put("callDT", callDT);

        Call<ResponseResult> api = ConnectRetrofit.getInstance().mRetrofitAPI.requestBoarding(input);
        ConnectRetrofit.getInstance().call(api, ((call, response) -> {
            Item = response.body();
            if (Item.msg.equals("")) {
                Log.d("배차취소", "요청성공");
                if(Item.carNum !=null) carNum=AES256Util.decode(Item.carNum);
                if(Item.driverName !=null) driverName=AES256Util.decode(Item.driverName);
                if(Item.carColor !=null) carColor=AES256Util.decode(Item.carColor);
                if(Item.carModel !=null) carModel=AES256Util.decode(Item.carModel);
                return;
            }else {
                Log.d("MsgERROR", AES256Util.decode(Item.msg)+ "입니다 : "+Item.isSuccessful);
                return;
            }
        }));

    }

}
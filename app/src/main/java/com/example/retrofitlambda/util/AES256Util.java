package com.example.retrofitlambda.util;

import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Util {

    //String text = "rlHVFyZZiUF9LYD46O0eNaS9PLrPpmLUoTU6RQlj-alIOIJPY1Vm--bVcU4bDNtOoaYZ8ESTEy48egIfJfA4cEaasWM1o3ZjKTg6Jg==";
    //Content-Type is not application/json
    //String text = "ELT2q58aa-izJ3kFEMuupIcj1tHFTkN5gTk7t5mulZGciVlDIzWU7BPHQQ4BgeZNLDe71A==";
    String text = "p3m35hPTnDtoGY4REGE5XKHNVTeQ5UQ57CyxQhCdWhBEEyrm70WnVti0sOsUttww3fbR1g==";
    public static String skey = "com.insoline.hanam";

    String strEncode = null;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCree() {
            //tv01.setText(decode(text, skey));
            //strEncode=encode("자바로 암호화 복호화 성공!!!", skey);
            //tv03.setText(decode(strEncode, skey));
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String decode(String base64Text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] key = skey.getBytes();
        //byte[] inputArr = Base64.getUrlDecoder().decode(base64Text);
        byte[] inputArr = Base64.decode(base64Text, Base64.URL_SAFE);


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key);
        byte[] tmp = md.digest();
        byte[] newKey = new byte[16];
        System.arraycopy(tmp, 0, newKey, 0, 16);

        // SecretKeySpec skSpec = new SecretKeySpec(key, "AES");
        SecretKeySpec skSpec = new SecretKeySpec(newKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        int blockSize = cipher.getBlockSize();

        //Log.d("AES256///", "inputArr.length:"+inputArr.length+", blockSize:"+blockSize);
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(inputArr, blockSize));

        byte[] dataToDecrypt = Arrays.copyOfRange(inputArr, blockSize, inputArr.length);
        cipher.init(Cipher.DECRYPT_MODE, skSpec, iv);
        byte[] result = cipher.doFinal(dataToDecrypt);
        return new String(result, StandardCharsets.UTF_8);
    }


    public static String encode(String oriText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {        // key sha256해시값중 앞 16바이트를 key값으로 사용
        byte[] key = skey.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key);
        byte[] tmp = md.digest();
        byte[] newKey = new byte[16];
        System.arraycopy(tmp, 0, newKey, 0, 16);

        SecureRandom rand = new SecureRandom();
        SecretKeySpec key_spec = new SecretKeySpec(newKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        byte[] encoded_payload = oriText.getBytes();
        int block_size = cipher.getBlockSize();
        byte[] buffer = new byte[block_size];
        rand.nextBytes(buffer);
        IvParameterSpec iv = new IvParameterSpec(buffer);
        buffer = Arrays.copyOf(buffer, block_size + encoded_payload.length);
        cipher.init(Cipher.ENCRYPT_MODE, key_spec, iv);

        try {
            cipher.doFinal(encoded_payload,0,encoded_payload.length, buffer,block_size);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(buffer, Base64.URL_SAFE);

        return encoded;
    }

}

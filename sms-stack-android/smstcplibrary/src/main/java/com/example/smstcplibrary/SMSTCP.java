package com.example.smstcplibrary;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Random;

public class SMSTCP {

    public static final int ID = 0;
    public static final int SMS_PERMISSION_CODE = 0;
    public static final int SMSLENGTH = 140;
    public static final int HEADERLENGTH = 14;
    public static final int DATALENGHT = SMSLENGTH - HEADERLENGTH;
    public AppCompatActivity activity;
    public SmsBroadcastReceiver smsBroadcastReceiver;
    public String privateKey;
    public int cipherMode;

    /**
     * Parent class, handle all methods of the SMSBroadcastReceiver as well as sending SMS
     * @param activity AppCompatActivity that implements the object
     */
    public SMSTCP(AppCompatActivity activity, int cipherMode, String privateKey) {
        this.activity = activity;
        this.cipherMode = cipherMode;
        this.privateKey = privateKey;
        this.smsBroadcastReceiver = new SmsBroadcastReceiver();
        this.registerReciver();

        if (!isPermissionGranted(Manifest.permission.READ_SMS) || !isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
            requestReadAndSendSmsPermission(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE});
        }
    }


    // SMSBroadcastReceiver handler ------------------
    /**
     * Send sms to a single or multiple addresses
     * @param smstcpLayer sms to send
     * @param phoneNo phone numbers to send the sms
     */
    public void sendSms(SMSTCPLayer smstcpLayer, String[] phoneNo) {
        String smsResponse = smstcpLayer.encondeSMS();
        for(int i = 0; i < phoneNo.length; i++){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo[i], null, smsResponse, null, null);
        }
    }

    /**
     * Check wheter the permission of sending and reading SMS is enabled
     * @param permission Type of permission
     * @return Boolean with the response
     */
    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this.activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request the permissions of sending and reading SMS
     * @param permissions Type of permission
     */
    public void requestReadAndSendSmsPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this.activity, permissions, SMS_PERMISSION_CODE);
    }

    // SMSTCP Utils -------------------

    /**
     * Encode a String into base64 String
     * @param base64Text String to conver
     * @return String in base64
     */
    public static String encodeBase64(String base64Text) {
        byte[] data = base64Text.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * Decode a base64 string into regular encoding
     * @param base64Text String in bas64
     * @return Decoded string
     */
    public static String decodeBase64(String base64Text) {
        byte[] data = Base64.decode(base64Text, Base64.DEFAULT);
        String text = new String(data, StandardCharsets.UTF_8);
        return text;
    }

    /**
     * Encode hexadecimal string into binary
     * @param hex Hexadecimal string
     * @return binary string
     */
    public static String hexToBinary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }

    /**
     * Generate a random key
     * @return Random key
     */
    public int generateRandKey() {
        Random rand = new Random();
        return rand.nextInt(254);
    }


    public String cipherAES(String message) throws UnsupportedEncodingException, GeneralSecurityException {
        return AESCipher.encryptAES(privateKey, message);
    }

    public String decipherAES(String AESMesage) throws UnsupportedEncodingException, GeneralSecurityException  {
        return AESCipher.decryptAES(privateKey, AESMesage);
    }


    public void unregisterReciver() {
        this.activity.unregisterReceiver(smsBroadcastReceiver);
    }

    public void registerReciver() {
        this.activity.registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }


}

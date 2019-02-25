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
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SMSTCPController {

    public CompletionHandler completionHandler;
    private AppCompatActivity activitySms;
    private SMSTCPReceiver  smstcpReceiver;
    private SMSTCPSender smstcpSender;
    private String privateKey;
    private int cipherMode;


    /**
     * Object that controlls all the activity of the SMSTCP Protocol, new implementations
     * must use this
     * @param activitySms Activity that implements the protocol
     * @param cipherMode Cipher Mode
     */
    public SMSTCPController(AppCompatActivity activitySms, int cipherMode, String privateKey) {
        this.activitySms = activitySms;
        this.cipherMode = cipherMode;
        this.privateKey = privateKey;
        this.smstcpReceiver = new SMSTCPReceiver(activitySms, cipherMode, privateKey);
        this.smstcpSender = new SMSTCPSender(activitySms, cipherMode, privateKey);

        smstcpSender.setCompletionHandler(new SMSTCPSender.CompletionHandler() {
            @Override
            public void handleMessage() {
                completionHandler.handleMessageSent();
            }

            @Override
            public void handleFinalMessage() {
                completionHandler.handleFinalMessageSent();
            }
        });

        smstcpReceiver.setCompletionHandler(new SMSTCPReceiver.CompletionHandler() {
            @Override
            public void handleFinalMessage(ArrayList<String> messages, String[] senderNumber) {
                completionHandler.handleFinalMessageReceived(messages, senderNumber);
            }

            @Override
            public void handleMessage(SMSTCPLayer smsTCP, String[] senderNumber) {
                completionHandler.handleMessageReceived(smsTCP, senderNumber);
            }
        });
    }

    public SMSTCPController(AppCompatActivity activitySms) {
        this(activitySms, 0, "");
    }


    // TODO - Implmente cipher AES


    /**
     * Send new message
     * @param sms sms to send
     * @param phoneNo phone number of the receiver
     */
    public void sendMessage(String sms, String[] phoneNo) {
        smstcpSender.createNewConverstaion(sms, phoneNo);
    }

    // UTILS ------------------------------

    /**
     * Process the message sent
     * @param messages message received
     * @return
     */
    public String processMessages(ArrayList<String> messages) throws BadKeyException {
        String finalMessage = "";
        for (String stringM : messages) {
            finalMessage += stringM;
        }

        switch(cipherMode){
            case 0:
                return SMSTCP.decodeBase64(finalMessage);
            case 1:
                try {
                    return AESCipher.decryptAES(privateKey, finalMessage);
                } catch (GeneralSecurityException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new BadKeyException();
                }
            default:
                return "";
        }
    }

    public interface CompletionHandler {
        public void handleFinalMessageReceived(ArrayList<String> messages, String[] senderNumber);
        public void handleMessageReceived(SMSTCPLayer smsTCP, String[] senderNumber);
        public void handleMessageSent();
        public void handleFinalMessageSent();
    }

    public void setCompletionHandler(CompletionHandler h) {
        this.completionHandler = h;
    }





}

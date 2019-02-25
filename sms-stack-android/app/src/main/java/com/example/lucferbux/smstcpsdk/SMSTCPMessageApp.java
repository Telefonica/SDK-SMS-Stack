package com.example.lucferbux.smstcpsdk;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.smstcplibrary.SMSTCPController;
import com.example.smstcplibrary.SMSTCPLayer;

import java.util.ArrayList;

public class SMSTCPMessageApp {

    private String[] phoneNumbers;
    private ListenerApp listener;
    private SMSTCPController smsTCPController;
    private AppCompatActivity activity;

    public SMSTCPMessageApp(AppCompatActivity activitySms, int cipherMode){
        this.activity = activitySms;
        this.smsTCPController = new SMSTCPController(activitySms, 1, "PATATA");

        smsTCPController.setCompletionHandler(new SMSTCPController.CompletionHandler() {

            @Override
            public void handleFinalMessageReceived(ArrayList<String> messages, String[] senderNumber) {
                try{
                    String message = smsTCPController.processMessages(messages);
                    listener.onTextProcessed(message);
                } catch (Exception e) {

                }

            }

            @Override
            public void handleMessageReceived(SMSTCPLayer smsTCP, String[] senderNumber) {
                try {
                    listener.onSMSRecived(smsTCP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleFinalMessageSent() {
                try {
                    onMessageSent();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void handleMessageSent() {

            }
        });
    }


    public void sendNewMessage(String text, String[] phoneNo) {
        SMSTCPMessageLayer smstcpMessageLayer = new SMSTCPMessageLayer(1, 0, 0, 0, 3, "hola que tal");
        smsTCPController.sendMessage(text, phoneNo);
    }

    public void onMessageSent() {
        Toast.makeText(this.activity.getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    public void setListener(ListenerApp listener) {
        this.listener = listener;
    }

    public interface ListenerApp {
        void onTextProcessed(String message);
        void onSMSRecived(SMSTCPLayer smstcpLayer);
    }







}

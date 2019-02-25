package com.example.smstcplibrary;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class SMSTCPSender extends SMSTCP {

    private ArrayList<String> messagesToSend = new ArrayList<String>();
    private ArrayList<String> addresses = new ArrayList<String>();
    public CompletionHandler completionHandler;

    /**
     * Send SMSTCP messages and control the volume depending of the data length
     * @param activity Activity in wich the listener is applied
     */
    public SMSTCPSender(AppCompatActivity activity, int cipherMode, String privateKey) {
        super(activity, cipherMode, privateKey);
    }

    // Sender methods ------------------------------------
    /**
     * Create new conversation to send to a single or multiple addressses
     * @param sms String with the message to send
     * @param phoneNo Phone number(s)
     */
    public void createNewConverstaion(String sms, String[] phoneNo) {
        if (!isPermissionGranted(Manifest.permission.SEND_SMS) || !isPermissionGranted(Manifest.permission.READ_PHONE_STATE)){
            requestReadAndSendSmsPermission(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE});
            return;
        }

        int key = generateRandKey();
        String textSMS = "";
        switch(super.cipherMode){
            case 0:
                textSMS = encodeBase64(sms);
                break;
            case 1:
                try {
                    textSMS = cipherAES(sms);
                } catch (UnsupportedEncodingException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
                break;
            default:
                textSMS = encodeBase64(sms);
                break;
        }

        // TOD0: CHANGE THAT TO STORE MESSAGE IN CASE IT NEED TO BE SENT
        this.messagesToSend = splitMessage(textSMS);

        for (int i = 0; i < this.messagesToSend.size(); i++ ) {
            String message = this.messagesToSend.get(i);
            int fin = i == (this.messagesToSend.size() - 1) ? 1 : 0;
            int syn = i == (this.messagesToSend.size() - 1) ? 0 : 1;
            SMSTCPLayer smsTCP = new SMSTCPLayer(ID, key, syn, 0,
                    0, fin, i, 0, message);
            sendSms(smsTCP, phoneNo);
            this.completionHandler.handleMessage();
        }
        this.completionHandler.handleFinalMessage();
    }

    //AES-CTR
    

    /**
     * Split the message in order to adapt the data to the SMS limit (may vary in each country)
     * @param base64Text Text to split
     * @return Array List with all the parts splitted
     */
    public ArrayList<String> splitMessage(String base64Text) {
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        int offset = DATALENGHT;
        while (index < base64Text.length()) {
            strings.add(base64Text.substring(index, Math.min(index + offset,base64Text.length())));
            index += offset;
        }
        return strings;
    }

    // Completion Handler ---------------------------

    /**
     * Completion handler to manage some of the behaviours of the app
     */
    public interface CompletionHandler {
        public void handleMessage();
        public void handleFinalMessage();
    }

    /**
     * Set the completion handler of the class
     * @param h Completion Handler
     */
    public void setCompletionHandler(CompletionHandler h) {
        this.completionHandler = h;
    }



}

package com.example.smstcplibrary;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class SMSTCPReceiver extends SMSTCP {

    private final int FIN_CHECK_DELAY = 500;
    private ArrayList<SMSTCPLayer> messagesReceived = new ArrayList<SMSTCPLayer>();
    private ArrayList<String> addresses = new ArrayList<String>();
    public CompletionHandler completionHandler;

    /**
     * Send SMSTCP messages and control the volume depending of the data length
     * @param activity Activity in wich the listener is applied
     */
    public SMSTCPReceiver(AppCompatActivity activity, int cipherMode, String privateKey) {
        super(activity, cipherMode, privateKey);

        smsBroadcastReceiver.setListener(new SmsBroadcastReceiver.Listener() {
            @Override
            public void onTextReceived(String text, String receiver) {
                addNewMessage(text, receiver);
            }
        });
    }

    // Receiver Methods ----------------------------------

    /**
     * Listen to new message in order to fetch de sms
     * @param sms Data with part of the sms sent
     * @param receiver Addresses that sent the sms
     */
    public void addNewMessage(final String sms, String receiver){
        final String[] phoneNo = new String[]{receiver};
        SMSTCPLayer smsTCP = null;
        try {
            smsTCP = new SMSTCPLayer(sms);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (smsTCP != null && smsTCP.checkIntegrity()){

            if(smsTCP.ack == 1) {
                // TOD0: Implement receive when error occurs
                return;
            }

            this.messagesReceived.add(smsTCP);
            this.completionHandler.handleMessage(smsTCP, phoneNo);
            final SMSTCPLayer smsTCPFinal = smsTCP;
            if (smsTCP.fin == 1 && smsTCP.ack == 0) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkSMSTCPSTream(smsTCPFinal, phoneNo);
                    }
                }, 5000);
            }
        }
    }




    /**
     * Check the stream of the SMSTCP to detect if all the messages have been sent
     * @param sms Final sms sent
     * @param phoneNo Addresses that sent the sms
     */
    private void checkSMSTCPSTream(final SMSTCPLayer sms, String[] phoneNo){
        ArrayList<String> messages = new ArrayList<String>();
        for(int i = 0; i <= sms.sBegin; i++) {
            SMSTCPLayer smsTCP = getSMSTCPByPos(i, sms.key);
            if (smsTCP != null) {
                messages.add(smsTCP.data);
            } else {
                responseConversation(sms, phoneNo, i, 0, 1, 0, 0);
                removeSMSByKey(sms.key, i);
                return;
            }
        }
        responseConversation(sms, phoneNo, sms.sBegin, 0, 1, 0, 1);
        removeSMSByKey(sms.key);
        this.completionHandler.handleFinalMessage(messages, phoneNo);
    }

    /**
     * Response to a conversation with the given flag headers
     * @param sms Data to send
     * @param phoneNo Addresses to send the message
     * @param sBeginResponse sBegin flag
     * @param synResponse Syn flag
     * @param ackResponse Ack flag
     * @param pshResponse Psh flag
     * @param finResponse Fin flag
     */
    private void responseConversation(SMSTCPLayer sms, String[] phoneNo, int sBeginResponse, int synResponse, int ackResponse, int pshResponse, int finResponse) {
        int keyResponse = sms != null ? sms.key : 0;
        int cipherResponse = sms != null ? sms.cipher : 0;
        String message = "";
        SMSTCPLayer smsTCP = new SMSTCPLayer(ID, keyResponse, synResponse, ackResponse, pshResponse, finResponse,
                sBeginResponse, cipherResponse, message);
        sendSms(smsTCP, phoneNo);
    }

    // TODO - Implement cipher AES
    private void handleCipherAES() {

    }


    // SMS Utils ----------------------------------------

    /**
     * Remove SMS from list of received by SMSTCPLayer key
     * @param key SMSTCPLayer id
     */
    private void removeSMSByKey(int key) {
        Iterator<SMSTCPLayer> it = messagesReceived.iterator();
        while(it.hasNext()){
            SMSTCPLayer smsIt = it.next();
            if(smsIt.key == key){
                it.remove();
            }
        }
    }

    /**
     * Remove SMS from a given index by SMSTCPLayer key
     * @param key SMSTCPLayer id
     * @param startingIndex Index
     */
    private void removeSMSByKey(int key, int startingIndex) {
        Iterator<SMSTCPLayer> it = messagesReceived.iterator();
        while(it.hasNext()){
            SMSTCPLayer smsIt = it.next();
            if(smsIt.key == key && smsIt.sBegin >= startingIndex){
                it.remove();
            }
        }
    }

    /**
     * Get the SMSTCPLayer Object by id and key
     * @param id index of the object
     * @param key key of the sms stream
     * @return SMSTCPLayer object or null
     */
    private SMSTCPLayer getSMSTCPByPos(int id, int key) {
        Optional<SMSTCPLayer> smsTCP = messagesReceived.stream().filter(p -> (p.sBegin == id && p.key == key)).findFirst();
        return smsTCP.orElse(null);
    }


    // Completion Handler -----------------------------------
    /**
     * Completion handler to manage some of the behaviours of the app
     */
    public interface CompletionHandler {
        void handleFinalMessage(ArrayList<String> messages, String[] senderNumber);
        void handleMessage(SMSTCPLayer smsTCP, String[] senderNumber);
    }

    /**
     * Set the completion handler of the class
     * @param h Completion handler of the class
     */
    public void setCompletionHandler(CompletionHandler h) {
        this.completionHandler = h;
    }

}

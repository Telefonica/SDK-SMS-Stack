package com.example.smstcplibrary;

import java.math.BigInteger;

public class SMSTCPLayer {


    public int id = 0b0;
    public int key = 0b0;
    public int syn = 0b0;
    public int ack = 0b0;
    public int psh = 0b0;
    public int fin = 0b0;
    public int sBegin = 0b0;
    public int cipher = 0b0;
    public int checkSum = 0b0;
    public String data;



    /**
     * Explicit constructor of SMSTCPLayer Class
     * @param id Protocol identifier
     * @param key Random key generated in a new conversation
     * @param syn Synchronized the sequence numbers in a new session/conversation
     * @param ack Flag indicating the acknowledgement of a message
     * @param psh Flag indicating the receiver that must push the data as soon as possible
     * @param fin End of session
     * @param sBegin Packet position
     * @param cipher Type of cypher
     * @param data Data of the message
     */
    public SMSTCPLayer(int id, int key, int syn, int ack, int psh, int fin, int sBegin, int cipher, String data){
        this.id = id;
        this.key = key;
        this.syn = syn;
        this.ack = ack;
        this.psh = psh;
        this.fin = fin;
        this.sBegin = sBegin;
        this.cipher = cipher;
        this.data = data;
    }


    /**
     * Entire sms to be decoded
     * @param sms smstcp given
     */
    public SMSTCPLayer(String sms) {
        decodeSMS(sms);
    }

    /**
     * Check if the message is well decrypted
     * @return Boolean with the result of the decryption
     */
    public Boolean checkIntegrity() {
        return this.id == 0;
    }


    /**
     * Decode an smstcp packet
     * @param sms smstcp given
     */
    private void decodeSMS(String sms) {
        String headersSms = fillHeader(hexToBinary(sms.substring(0,14)), 56);
        String dataSMS = sms.substring(14, sms.length());
        this.id = Integer.parseInt(headersSms.substring(0,1), 2);
        this.key = Integer.parseInt(headersSms.substring(1,9), 2);
        this.syn = Integer.parseInt(headersSms.substring(9,10), 2);
        this.ack = Integer.parseInt(headersSms.substring(10,11), 2);
        this.psh = Integer.parseInt(headersSms.substring(11,12), 2);
        this.fin = Integer.parseInt(headersSms.substring(12,13), 2);
        this.sBegin = Integer.parseInt(headersSms.substring(13,21), 2);
        this.cipher =  Integer.parseInt(headersSms.substring(21,24), 2);
        this.checkSum = Integer.parseInt(headersSms.substring(24,56), 2);
        this.data = dataSMS;
    }


    /**
     * Encode an smstcp packet
     * @return message coded
     */
    public String encondeSMS() {
        String idSMS = fillHeader(this.id, 1);
        String keySMS = fillHeader(this.key, 8);
        String synSMS = fillHeader(this.syn, 1);
        String ackSMS = fillHeader(this.ack, 1);
        String pshSMS = fillHeader(this.psh, 1);
        String finSMS = fillHeader(this.fin, 1);
        String sBeginSMS = fillHeader(this.sBegin, 8);
        String cipherSMS = fillHeader(this.cipher, 3);
        String checkSumSMS = fillHeader(this.checkSum, 32);

        String headersBinary = idSMS + keySMS + synSMS + ackSMS + pshSMS + finSMS + sBeginSMS + cipherSMS + checkSumSMS;
        String headersHexa = fillHeader(Long.toHexString(Long.parseLong(headersBinary, 2)), 14);

        return  headersHexa + this.data;
    }


    /**
     * Transforms an hexadecimal string to a binary string
     * @param hex message in hexadecimal
     * @return binary string representation
     */
    public static String hexToBinary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }


    /**
     * Fill the header parameters with 0
     * @param header header parameter
     * @param requiredSize size of the parameter
     * @return parameter with padding
     */
    public static String fillHeader(int header, int requiredSize) {
        String headerToString = Integer.toBinaryString(header);
        String filler = "";
        try {
            filler = new String(new char[requiredSize - headerToString.length()]).replace("\0", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filler + headerToString;
    }

    /**
     * Fill the header parameters with 0
     * @param header header parameter
     * @param requiredSize size of the parameter
     * @return parameter with padding
     */
    public static String fillHeader(String header, int requiredSize) {
        String filler = "";
        try {
            filler = new String(new char[requiredSize - header.length()]).replace("\0", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filler + header;
    }

}

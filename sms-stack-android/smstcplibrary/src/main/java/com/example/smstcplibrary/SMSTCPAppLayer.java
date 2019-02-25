package com.example.smstcplibrary;

public interface SMSTCPAppLayer {

    /**
     * Decode an SMS String onto a SMSTCPAppLayer object, obtaining headers and data.
     * @param sms sms with the data
     */
    public void decodeSMSApp(String sms);

    /**
     * Encode an SMSTCPAppLayer object onto an SMS String
     * @return
     */
    public String encodeSMSApp();

    /**
     * Transform an hexadecimal string into a binary string
     * @param hex header in hexadecimal
     * @return header in binary
     */
    public String hexToBinary(String hex);

}

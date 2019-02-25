package com.example.lucferbux.smstcpsdk;

import com.example.smstcplibrary.SMSTCPAppLayer;
import com.example.smstcplibrary.SMSTCPLayer;

import java.math.BigInteger;

public class SMSTCPMessageLayer implements SMSTCPAppLayer {

    public int mode = 0b0;
    public int ack = 0b0;
    public int status = 0b0;
    public int sBegin = 0b0;
    public int id = 0b0;
    public String data;
    
    public SMSTCPMessageLayer(int mode, int ack, int status, int sBegin, int id, String data) {
        this.mode = mode;
        this.ack = ack;
        this.status = status;
        this.sBegin = sBegin;
        this.id = id;
        this.data = data;
    }
    
    public SMSTCPMessageLayer(String sms) {
        decodeSMSApp(sms);
    }

    @Override
    public void decodeSMSApp(String s) {
        String headerMessage = SMSTCPLayer.fillHeader(hexToBinary(s.substring(0, 4)), 16);
        String dataMessage = s.substring(4, s.length());
        this.mode = Integer.parseInt(headerMessage.substring(0,4), 2);
        this.ack = Integer.parseInt(headerMessage.substring(4,5), 2);
        this.status = Integer.parseInt(headerMessage.substring(5,6), 2);
        this.sBegin = Integer.parseInt(headerMessage.substring(6,12), 2);
        this.id = Integer.parseInt(headerMessage.substring(12,16), 2);
        this.data = dataMessage;
    }


    @Override
    public String encodeSMSApp() {
        String modeMessage = SMSTCPLayer.fillHeader(this.mode, 4);
        String ackMessage = SMSTCPLayer.fillHeader(this.ack, 1);
        String statusMessage = SMSTCPLayer.fillHeader(this.status, 1);
        String sBeginMessage = SMSTCPLayer.fillHeader(this.sBegin, 6);
        String idMessage = SMSTCPLayer.fillHeader(this.id, 4);

        String headersBinary = modeMessage + ackMessage + statusMessage + sBeginMessage + idMessage;
        String headersHexa = SMSTCPLayer.fillHeader(Long.toHexString(Long.parseLong(headersBinary, 2)), 4);

        return headersHexa + this.data;
    }

    @Override
    public String hexToBinary(String s) {
        return new BigInteger(s, 16).toString(2);
    }
}

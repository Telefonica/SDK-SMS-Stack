import { Buffer } from 'buffer';
import { SmsBroadcaster } from './index';
import { SMSTCPlayer, TcpLayer } from './smsTcpLayer'

export class SMSTCP {

    public cipherKey: string;
    public chipherMode: number;
    protected readonly smsLenght: number = 140;
    protected readonly headerLength: number = 14;
    private readonly dataLeghth: number;
    private smsBroadcaster: SmsBroadcaster;

    /**
     * Base class with blueprints to handle the sms stream
     * @param cipherMode Cipher mode | 0, Base64 | 1, PSK AES CBC
     * @param cipherKey Pre shared key for PSK AES CBC Mode
     * @param smsBroadcaster Broadcast object to send message
     */
    constructor(cipherMode: number, cipherKey: string, smsBroadcaster: SmsBroadcaster){
        this.dataLeghth = this.smsLenght - this.headerLength;
        this.cipherKey = cipherKey;
        this.chipherMode = cipherMode;   
        this.smsBroadcaster = smsBroadcaster; 
    }


    /**
     * Encodes a tcp layer and send it through the sms broadcaster
     * @param tcpLayer Layer of the sms stack protocol
     * @param phoneNo Address to send
     */
    public sendSms(tcpLayer: TcpLayer, phoneNo: string) {
        const smsResponse = SMSTCPlayer.encodeSMS(tcpLayer);
        this.smsBroadcaster.sendSms(smsResponse, phoneNo);
    }

    /**
     * Generates random key
     */
    public generateRandomKey(): number {
        return Math.floor(Math.random() * 254) + 1  
    }

    /**
     * Encode the text in Base64
     * @param text Text to encode
     */
    public static encodeBase64(text: string): string {
        return Buffer.from(text, 'binary').toString('base64')
    }

    /**
     * Decode the Base64 text
     * @param base64 Text to decode
     */
    public static decodeBase64(base64: string): string{
        return Buffer.from(base64, 'base64').toString('binary');
    }

    // TODO -  Implement cipher AES

}
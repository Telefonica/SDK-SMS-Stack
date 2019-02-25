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

    constructor(cipherMode: number, cipherKey: string, smsBroadcaster: SmsBroadcaster){
        this.dataLeghth = this.smsLenght - this.headerLength;
        this.cipherKey = cipherKey;
        this.chipherMode = cipherMode;   
        this.smsBroadcaster = smsBroadcaster; 
    }


    public sendSms(tcpLayer: TcpLayer, phoneNo: string) {
        const smsResponse = SMSTCPlayer.encodeSMS(tcpLayer);
        this.smsBroadcaster.sendSms(smsResponse, phoneNo);
    }

    public generateRandomKey(): number {
        return Math.floor(Math.random() * 254) + 1  
    }

    public static encodeBase64(text: string): string {
        return Buffer.from(text, 'binary').toString('base64')
    }

    public static decodeBase64(base64: string): string{
        return Buffer.from(base64, 'base64').toString('binary');
    }

    // TODO -  Implement cipher AES

}
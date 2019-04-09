import { SMSTCP } from "./smsTcp";
import { Observable, Subject } from 'rxjs';
import { SMSTCPlayer, TcpLayer } from "./smsTcpLayer";
import { SmsTcpController } from "./smsTcpController";
import { SmsBroadcaster, SmsReceived } from "./index"
import { ArrayExt } from "./arrayExt";

export class SmsTcpReceiver extends SMSTCP  {

    private controller?: SmsTcpController;
    private messageReceived: TcpLayer[] = [];
    
    constructor(cipherMode: number, cipherKey: string, controller:SmsTcpController, broadcasterRec: Observable<SmsReceived>, broadcasterSend: SmsBroadcaster) {
        super(cipherMode, cipherKey, broadcasterSend);
        this.controller = controller;
        broadcasterRec.subscribe(sms => {
            this.addNewMessage(sms.MessageText, sms.Originator);
        })
    }

    public addNewMessage(sms: string, receiver: string) {
        let smsLayer: TcpLayer;
        try {
            smsLayer = SMSTCPlayer.decodeSMS(sms);
        }
        catch(e) {
            console.log('Error:', e);
            return;
        }
        this.messageReceived.push(smsLayer);
        if(this.controller !== null){
            this.controller!.handleMessageReceived(smsLayer, receiver)
        }
        if(smsLayer.fin === 1 && smsLayer.ack === 0){
            setTimeout(() => {
                this.checkSmsTcpStream(smsLayer, receiver);
            }, 5000)
        }
    }

    

    private checkSmsTcpStream(sms: TcpLayer, receiver: string){
        const smsStream = this.messageReceived
        .filter(x => x.key === sms.key)
        .sort((x, y) => x.sBegin - y.sBegin);
        const messages = smsStream.map(x => x.data);
        const smsStreamIndex =  smsStream.map(x => x.sBegin);
        const dataStream = ArrayExt.range(0, sms.sBegin, 1);
        const missing = dataStream.filter(item => smsStreamIndex.indexOf(item) < 0)
        if(missing.length > 0){
            const min = Math.min(...missing);
            const response_failed = SMSTCPlayer.messagePacketLost(sms, min);
            this.sendSms(response_failed, receiver)
            this.messageReceived = this.removeSmsBy(sms.key, min);
            return;
        }
        // Control no return
        if(sms.psh == 0){
            const response = SMSTCPlayer.messagePacketFin(sms);
            this.sendSms(response, receiver);
        }
        this.messageReceived = this.removeSmsBy(sms.key);
        this.controller!.handleFinalMessageReceived(messages, receiver);
    }

    

    private removeSmsBy(key: number, sBegin?: number): TcpLayer[]{
        if(sBegin != null){
            return this.messageReceived.filter(sms => !(sms.key === key) && (sms.sBegin >= sBegin));
        } else {
            return this.messageReceived.filter(sms => !(sms.key === key));
        }
    }

}





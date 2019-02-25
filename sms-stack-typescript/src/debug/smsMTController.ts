import { SmsBroadcaster, SMSTCPlayer, SmsTcpController, SmsTcpControllerObserver, SmsReceived } from "../index";
import { Observable } from 'rxjs';
import { TextMessage } from "./textMessage";
import { SmsMtLayer } from "./smsMTLayer";


export class SmsStackControler implements SmsTcpControllerObserver {

    
    private controller: SmsTcpController;
    private msgCallback: (textMessage: TextMessage) => void;
    
    constructor(broadcasterRec: Observable<SmsReceived>, broadcasterSend: SmsBroadcaster, callback: (textMessage: TextMessage) => void){
        this.controller = new SmsTcpController(0, "", this, broadcasterRec, broadcasterSend);
        this.msgCallback = callback;
    }

    public handleFinalMessageReceived(messages: string[], sender: string){
        const message = this.controller.processMessage(messages);
        const tcpMtLayer = SmsMtLayer.decodeSMS(message);
        const textMessage = SmsMtLayer.parseSmsLayer(tcpMtLayer);
        this.msgCallback(textMessage);
    }

    public handleMessageReceived(layer: SMSTCPlayer, sender: string) {}
    
    public handleFinalMessageSent(messages: string[], sender: string){}

    public handleMessageSent(layer: SMSTCPlayer, sender: string) {}

    public sendMessage(sms: TextMessage, senders: string[] | string) {
        const smsLayer = SmsMtLayer.parseMessage(sms);
        const message = SmsMtLayer.encodeSMS(smsLayer)
        if(this.isString(senders)) {
            this.controller.sendMessage(message, senders);
        } else {
            for(const sender in senders){
                this.controller.sendMessage(message, sender);
            }  
        }
    }

    private isNumber(x: any): x is number {
        return typeof x === "number";
    }
    
    private isString(x: any): x is string {
        return typeof x === "string";
    }
}



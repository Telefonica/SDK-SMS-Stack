import { Observable } from 'rxjs';
import { SmsTcpReceiver } from "./smsTcpReceiver";
import { SMSTCPlayer } from "./smsTcpLayer";
import { SmsTcpSender } from "./smsTcpSender";
import { SmsBroadcaster, SmsReceived } from "./index";
import { SmsTcpControllerObserver } from "./smsTcpControllerObserver";
import { SMSTCP } from "./smsTcp";


export class SmsTcpController {
    private receiver: SmsTcpReceiver;
    private sender: SmsTcpSender;
    private cipherMode: number;
    private cipherKey: string;
    private observer: SmsTcpControllerObserver;

    constructor(cipherMode: number = 0, cipherKey: string = "", observer: SmsTcpControllerObserver, broadcasterRec: Observable<SmsReceived>, broadcasterSend: SmsBroadcaster){
        this.cipherMode = cipherMode ;
        this.cipherKey = cipherKey;
        this.receiver = new SmsTcpReceiver(cipherMode, cipherKey, this, broadcasterRec, broadcasterSend);
        this.sender = new SmsTcpSender(cipherMode, cipherKey, this, broadcasterSend); 
        this.observer = observer;
    }


    public handleFinalMessageReceived(messages: string[], sender: string){
        this.observer.handleFinalMessageReceived(messages, sender);
    }

    public handleMessageReceived(layer: SMSTCPlayer, sender: string) {
        this.observer.handleMessageReceived(layer, sender);
    }
    
    public handleFinalMessageSent(messages: string[], sender: string){
        this.observer.handleFinalMessageSent(messages, sender);
    }

    public handleMessageSent(layer: SMSTCPlayer, sender: string) {
        this.observer.handleMessageSent(layer, sender);
    }

    public sendMessage(sms: string, sender: string){
        this.sender.createNewConversation(sms, sender);
    }

    public processMessage(message: string[]): string {
        const finalMessage = message.join("");
        switch(this.cipherMode) {
            case 0:
                return SMSTCP.decodeBase64(finalMessage);
            case 1:
                //TODO - implement AES cipher
                return "";
            default:
                return "";
        }
    }
}

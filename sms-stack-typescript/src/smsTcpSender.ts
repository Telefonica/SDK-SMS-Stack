import { SMSTCP } from "./smsTcp";
import { SmsBroadcaster } from "./index"
import { SmsTcpController } from "./smsTcpController";
import { TcpLayer } from "./smsTcpLayer";

export class SmsTcpSender extends SMSTCP {

    private controller: SmsTcpController;

    constructor(cipherMode: number, cipherKey: string, controller: SmsTcpController, broadcastSend: SmsBroadcaster) {
        super(cipherMode, cipherKey, broadcastSend);
        this.controller = controller;
        //TODO - COMPLETE
    }

    public createNewConversation(sms: string, sender: string, ackBack = true) {
        const key = this.generateRandomKey();
        const cipherText = this.cipherText(sms);
        const messagesToSend = this.splitMessage(cipherText);
        if(messagesToSend == null){
            return;
        }
        messagesToSend!.forEach((item, index) => {
            this.sendMessage(item, key, index, index == messagesToSend.length - 1, sender, ackBack);  
        })
        this.controller.handleFinalMessageSent(messagesToSend, sender);
    }

    private sendMessage(text: string, key: number, sBegin: number, isFin: boolean, sender: string, ackBack: Boolean) {
        const smsLayer: TcpLayer = { id: 0, key: key, syn: +!isFin, ack: 0, psh: +!ackBack, fin: +isFin, sBegin: sBegin, cipher: this.chipherMode, checkSum: 0, data: text }
        this.sendSms(smsLayer, sender);
        this.controller.handleMessageSent(smsLayer, sender)
    }

    private cipherText(text: string): string {
        let cipherText: string = "";
        switch(this.chipherMode) {
            case 0:
                cipherText = SMSTCP.encodeBase64(text);
                break;
            case 1:
                // AES CIPHER
                break;
            default:
                cipherText = SMSTCP.encodeBase64(text);
                break;
        }
        return cipherText;
    }

    private splitMessage(cipherText: string): string[] | null {
        const length = this.smsLenght;
        return cipherText.match(new RegExp('(.|[\r\n]){1,' + length + '}', 'g'));
    }

}


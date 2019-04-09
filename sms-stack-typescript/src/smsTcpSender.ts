import { SMSTCP } from "./smsTcp";
import { SmsBroadcaster } from "./index"
import { SmsTcpController } from "./smsTcpController";
import { TcpLayer } from "./smsTcpLayer";

export class SmsTcpSender extends SMSTCP {

    private controller: SmsTcpController;

    constructor(cipherMode: number, cipherKey: string, controller: SmsTcpController, broadcastSend: SmsBroadcaster) {
        super(cipherMode, cipherKey, broadcastSend);
        this.controller = controller;

    }

    /**
     * Creates a new  conversation with a given sms and recipient
     * @param sms Sms to send
     * @param recipient Phone number of the
     * @param ackBack 
     */
    public createNewConversation(sms: string, recipient: string, ackBack = true) {
        const key = this.generateRandomKey();
        const cipherText = this.cipherText(sms);
        const messagesToSend = this.splitMessage(cipherText);
        if(messagesToSend == null){
            return;
        }
        messagesToSend!.forEach((item, index) => {
            this.sendMessage(item, key, index, index == messagesToSend.length - 1, recipient, ackBack);  
        })
        this.controller.handleFinalMessageSent(messagesToSend, recipient);
    }

    /**
     * Sends a single message in an Sms Stack Packet 
     * @param text Text to send
     * @param key Stream key
     * @param sBegin SBegin flag
     * @param isFin Indicates if the packet is Fin
     * @param recipient Recipient name
     * @param ackBack Wether if we want an acknowledgment or not
     */
    private sendMessage(text: string, key: number, sBegin: number, isFin: boolean, recipient: string, ackBack: Boolean) {
        const smsLayer: TcpLayer = { id: 0, key: key, syn: +!(sBegin == 0), ack: 0, psh: +!ackBack, fin: +isFin, sBegin: sBegin, cipher: this.chipherMode, checkSum: 0, data: text }
        this.sendSms(smsLayer, recipient);
        this.controller.handleMessageSent(smsLayer, recipient)
    }

    /**
     * Cipher text with the given method
     * @param text Text to cipher
     */
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

    /**
     * Split the message in n parts to fit the sms length restriction
     * @param cipherText Final text encoded to send
     */
    private splitMessage(cipherText: string): string[] | null {
        const length = this.smsLenght;
        return cipherText.match(new RegExp('(.|[\r\n]){1,' + length + '}', 'g'));
    }

}


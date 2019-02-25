import { SmsTcpAppLayer } from "../index";
import { TextMessage } from "./textMessage";

export interface ISmsMtLayer {
    chat: number;
    state: number;
    type: number;
    timestamp: number;
    receipentId: number;
    senderId: number;
    senderName: number;
    extra: number;
    data: string;
}

export class SmsMtLayer extends SmsTcpAppLayer {
    
    public static parseMessage(sms: TextMessage): ISmsMtLayer {
        const dateString = sms.time.toString()
        const chat = sms.chat === "CHANNEL" ? 0 : 1;
        const state = 1;
        const type = 0;
        const timestamp = dateString.length;
        const receipentId = sms.recipientId.length;
        const senderId = sms.senderId.length;
        const senderName = sms.senderName.length;
        const extra = 0;
        const data = dateString + sms.recipientId + sms.senderId + sms.senderName + sms.text;
        const tcpAppLayer: ISmsMtLayer =  { chat: chat, state: state, type: type, timestamp: timestamp, receipentId: receipentId, senderId: senderId, senderName: senderName, extra: extra, data: data }
        return tcpAppLayer;
    } 

    public static parseSmsLayer(layer: ISmsMtLayer): TextMessage {
        const receipentIdPosEnd = layer.timestamp + layer.receipentId;
        const senderIdPosEnd = receipentIdPosEnd + layer.senderId;
        const senerNamePosEnd = senderIdPosEnd + layer.senderName;

        const chatMsg = layer.chat === 0 ? "CHANNEL" : "GROUP";
        const timestampMsg = new Date(+layer.data.substr(0, layer.timestamp));
        const receipentIdMsg = layer.data.substring(layer.timestamp, receipentIdPosEnd);
        const senderIdMsg = layer.data.substring(receipentIdPosEnd, senderIdPosEnd);
        const senderNameMsg = layer.data.substring(senderIdPosEnd, senerNamePosEnd);
        const textMessage = layer.data.substring(senerNamePosEnd, layer.data.length);

        const testMessage: TextMessage =  { text: textMessage, chat: chatMsg, time: timestampMsg, senderId: senderIdMsg, recipientId: receipentIdMsg, senderName: senderNameMsg, state: "SMS", type: "TEXT" }
        return testMessage;
    }

    public static decodeSMS(sms: string): ISmsMtLayer {
        const headersSms = this.fillHeaderString(this.hexToBinary(sms.substr(0, 8)), 32);
        const dataSms = sms.substr(8, sms.length);
        const chat = parseInt(headersSms.substr(0, 1), 2);
        const state = parseInt(headersSms.substr(1, 1), 2);
        const type = parseInt(headersSms.substr(2, 1), 2);
        const timestamp = parseInt(headersSms.substr(3, 5), 2);
        const receipentId = parseInt(headersSms.substr(8, 6), 2);
        const senderId = parseInt(headersSms.substr(14, 6), 2);
        const senderName = parseInt(headersSms.substr(20, 6), 2);
        const extra = parseInt(headersSms.substr(26, 6), 2);
        const data = dataSms;

        
        const tcpAppLayer: ISmsMtLayer =  { chat: chat, state: state, type: type, timestamp: timestamp, receipentId: receipentId, senderId: senderId, senderName: senderName, extra: extra, data: data }
        return tcpAppLayer;
    }

    public static encodeSMS(layer: ISmsMtLayer): string {
        const chatSMS = this.fillHeaderBinary(layer.chat, 1);
        const stateSMS = this.fillHeaderBinary(layer.state, 1);
        const typeSMS = this.fillHeaderBinary(layer.type, 1);

        const timestampSMS = this.fillHeaderBinary(layer.timestamp, 5);
        const receipentIdSMS = this.fillHeaderBinary(layer.receipentId, 6);

        const senderIdSMS = this.fillHeaderBinary(layer.senderId, 6);
        const senderNameSMS = this.fillHeaderBinary(layer.senderName, 6);
        const extraSMS = this.fillHeaderBinary(layer.extra, 6);

        const headersBinary: string = chatSMS + stateSMS + typeSMS + timestampSMS + receipentIdSMS + senderIdSMS + senderNameSMS + extraSMS;
        const headersHexa = this.fillHeaderString(this.binaryToHex(headersBinary), 8);

        return headersHexa + layer.data;
    }

}
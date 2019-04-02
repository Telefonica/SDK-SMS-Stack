import { SmsTcpAppLayer } from "./smsTcpAppLayer";

export interface TcpLayer {
    id: number;
    key: number;
    syn: number;
    ack: number;
    psh: number;
    fin: number;
    sBegin: number;
    cipher: number;
    checkSum: number;
    data: string;
}

export class SMSTCPlayer extends SmsTcpAppLayer {
    
    constructor() {
        super()
    }

    public static checkIntegrity(id: number) {
        return id === 0;
    }

    public static decodeSMS(sms: string): TcpLayer {
        const headersSms = this.fillHeaderString(this.hexToBinary(sms.substr(0, 14)), 56);
        const dataSms = sms.substr(14, sms.length);
        const id = parseInt(headersSms.substr(0, 1), 2);
        const key = parseInt(headersSms.substr(1, 8), 2);
        const syn = parseInt(headersSms.substr(9, 1), 2);
        const ack = parseInt(headersSms.substr(10, 1), 2);
        const psh = parseInt(headersSms.substr(11, 1), 2);
        const fin = parseInt(headersSms.substr(12, 1), 2);
        const sBegin = parseInt(headersSms.substr(13, 8), 2);
        const cipher = parseInt(headersSms.substr(21, 3), 2);
        const checkSum = parseInt(headersSms.substr(24, 32), 2);
        
        const tcpLayer: TcpLayer =  {id: id, key: key, syn: syn, ack: ack, psh: psh, fin: fin, sBegin: sBegin, cipher: cipher, checkSum: checkSum, data: dataSms};
        return tcpLayer;
    }

    public static encodeSMS(layer: TcpLayer): string {
        const idSMS = this.fillHeaderBinary(layer.id, 1);
        const keySMS = this.fillHeaderBinary(layer.key, 8);
        const synSMS = this.fillHeaderBinary(layer.syn, 1);
        const ackSMS = this.fillHeaderBinary(layer.ack, 1);
        const pshSMS = this.fillHeaderBinary(layer.psh, 1);
        const finSMS = this.fillHeaderBinary(layer.fin, 1);
        const sBeginSMS = this.fillHeaderBinary(layer.sBegin, 8);
        const cipherSMS = this.fillHeaderBinary(layer.cipher, 3);
        const checkSumSMS = this.fillHeaderBinary(layer.checkSum, 32);

        const headersBinary: string = idSMS + keySMS + synSMS + ackSMS + pshSMS + finSMS + sBeginSMS + cipherSMS + checkSumSMS;
        const headersHexa = this.fillHeaderString(this.binaryToHex(headersBinary), 14);

        return headersHexa + layer.data;
    }

    public static messagePacketLost(layer: TcpLayer, sBegin?: number): TcpLayer {
        const idx = sBegin !== null ? sBegin : layer.sBegin;
        const tcpLayer: TcpLayer = { id: layer.id, key: layer.key, syn: 0, ack: 1, psh: 0, fin: 0, sBegin: idx!, cipher: layer.cipher, checkSum: layer.checkSum, data: "" }
        return tcpLayer;
    }

    public static messagePacketFin(layer: TcpLayer): TcpLayer {
        const tcpLayer: TcpLayer = { id: layer.id, key: layer.key, syn: 0, ack: 1, psh: 0, fin: 1, sBegin: layer.sBegin, cipher: layer.cipher, checkSum: layer.checkSum, data: "" }
        return tcpLayer;
    }

}
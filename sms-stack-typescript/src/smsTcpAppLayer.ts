export abstract class SmsTcpAppLayer {

    protected static hexToBinary(hex: string): string {
        if (!this.checkHex(hex)) throw 'Error parsing hexadecimal';
        return parseInt(hex, 16).toString(2);
    }

    protected static binaryToHex(bin: string): string {
        if(!this.checkBin(bin)) throw 'Error parsing binary';
        return parseInt(bin,2).toString(16);
    }

    protected static checkBin(n: string): boolean{
        return/^[01]{1,64}$/.test(n);
    }

    protected static checkHex(n: string): boolean {
        return /^[0-9A-Fa-f]{1,64}$/.test(n);
    }
    
    protected static fillHeaderBinary(header: number, requiredSize: number): string {
        const headerToString = this.dec2Bin(header);
        const filler = Array((requiredSize - headerToString.length) + 1).join('0');
        return `${filler}${headerToString}`;
    }

    protected static fillHeaderString(header: string, requiredSize: number) : string {
        const filler = Array((requiredSize - header.length) + 1).join('0');
        return filler + header;
    }

    protected static dec2Bin(dec: number){
        if(dec >= 0) {
            return dec.toString(2);
        }
        else {
            return (~dec).toString(2);
        }
    }

}
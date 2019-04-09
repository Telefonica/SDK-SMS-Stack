//Abstract class with helper methods 
export abstract class SmsTcpAppLayer {

    /**
     * Convert hexadecimal string to binary string 
     * @param hex Hexadecimal string
     */
    protected static hexToBinary(hex: string): string {
        if (!this.checkHex(hex)) throw 'Error parsing hexadecimal';
        return parseInt(hex, 16).toString(2);
    }

    /**
     * Convert Binary string to hexadecimal string
     * @param bin Binary string
     */
    protected static binaryToHex(bin: string): string {
        if(!this.checkBin(bin)) throw 'Error parsing binary';
        return parseInt(bin,2).toString(16);
    }


    /**
     * Checks if the Binary string is well formed
     * @param text Binary string
     */
    protected static checkBin(text: string): boolean{
        return/^[01]{1,64}$/.test(text);
    }

    /**
     * Checks if the Hexadecimal string is well formed
     * @param text Hexadecimal string
     */
    protected static checkHex(text: string): boolean {
        return /^[0-9A-Fa-f]{1,64}$/.test(text);
    }
    
    /**
     * Fill the binary header with required zeroes
     * @param header Binary header 
     * @param requiredSize Requiered size of the header
     */
    protected static fillHeaderBinary(header: number, requiredSize: number): string {
        const headerToString = this.dec2Bin(header);
        return this.fillHeaderString(headerToString, requiredSize);
    }

    /**
     * Fill the binary string header with required zeroes
     * @param header Binary String header 
     * @param requiredSize Requiered size of the header
     */
    protected static fillHeaderString(header: string, requiredSize: number): string {
        const filler = Array((requiredSize - header.length) + 1).join('0');
        return `${filler}${header}`;
    }


    /**
     * Transform decimal number to binary string
     * @param dec Decilmal number
     */
    protected static dec2Bin(dec: number): string{
        if(dec >= 0) {
            return dec.toString(2);
        }
        else {
            return (~dec).toString(2);
        }
    }

}
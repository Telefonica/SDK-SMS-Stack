import binascii

class SmsTcpLayerFormatter:
    #TOCHECK *** ¿Deberiamos comprobar si es hexadecimal, o binario antes de proceder? (try..catch)
    ## ¿Estamos seguros que siempre se pasa el dato correcto?

    ###def is_hex(s):
    ###    h = re.fullmatch(r"[0-9a-fA-F]*", s or "") is not None
    ###    h2 = len(s) % 2 == 0
    ###    return (h and h2)
    @staticmethod
    def hex_to_binary(hex):
        """Convert hex string to binary string
        
        Args:
            hex (str): Hex string
        
        Returns:
            str: Binary string
        """

        return bin(int(hex, 16))[2:]
    
    @staticmethod
    def binary_to_hex(bin):
        """Convert bin string to hex string
        
        Args:
            bin (str): Binary strign
        
        Returns:
            str: Hex string
        """

        return hex(int(bin, 2))[2:]
    
    @staticmethod
    def dec_to_binary(dec):
        """Convert decimal number to binary string
        
        Args:
            dec (int): Decimal Number
        
        Returns:
            str: Binary string
        """

        return "{0:b}".format(dec)

    @staticmethod
    def fill_header_string(header, req_size):
        """Fill a binary header with requiered zeroes
        
        Args:
            header (str): Binary string
            req_size (int): Lenght required for the binary string
        
        Returns:
            str: Binary string filled with zeroes
        """
        return header.zfill(req_size)

    @staticmethod
    def fill_header_binary(header, req_size):
        """Fill a binary header with requiered zeroes
        
        Args:
            header (binary): Binary header
            req_size (int): Lenght requiered
        
        Returns:
            str: Binary string filled with zeroes
        """

        header_to_string = SmsTcpLayerFormatter.dec_to_binary(header)
        return header_to_string.zfill(req_size)
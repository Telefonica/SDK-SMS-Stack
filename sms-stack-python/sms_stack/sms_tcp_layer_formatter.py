import binascii

class SmsTcpLayerFormatter:
    #TOCHECK *** ¿Deberiamos comprobar si es hexadecimal, o binario antes de proceder? (try..catch)

    @staticmethod
    def hex_to_binary(hex):
        """Convert hex string to binary string
        
        Args:
            hex (str): Hex string
        
        Returns:
            str: Binary string
        """
        try:
            binary = bin(int(hex, 16))[2:]
        except:
            binary = None
            print("Error converting hexadecimal to binary... input: {}".format(hex))

        return binary
    
    @staticmethod
    def binary_to_hex(bin):
        """Convert bin string to hex string
        
        Args:
            bin (str): Binary strign
        
        Returns:
            str: Hex string
        """
        try:
            hexa  = hex(int(bin, 2))[2:]
        except:
            hexa  = None
            print("Error converting binary to hexadecimal... input: {}".format(hex))

        return hexa 
    
    @staticmethod
    def dec_to_binary(dec):
        """Convert decimal number to binary string
        
        Args:
            dec (int): Decimal Number
        
        Returns:
            str: Binary string
        """
        try:
            binary  = "{0:b}".format(dec)
        except:
            binary  = None
            print("Error converting decimal to binary... input: {}".format(hex))

        return binary

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
from sms_stack.sms_tcp_layer_formatter import SmsTcpLayerFormatter

class SmsTcpLayerRasp:

    def __init__(self, sms='', id_origin=None, id_target=None, mod=None, status=None, data=""):
        """Sms Stack Layer implementation
            sms (str, optional): Defaults to ''. Sms to decoded
            id_origin (int, optional): Defaults to None. Identifier of the origin device
            id_target (int, optional): Defaults to None. Identifier of the targeted device
            mod (int, optional): Defaults to None. ???
            status (int, optional): Defaults to None. [description]
            data (str, optional): Defaults to "". Data to send, currently unavailable
        """

        if(sms != ''):
            self._decode_sms(sms)
        elif(id_origin != None, id_target != None, mod != None, status != None,):
            self.id_origin = id_origin
            self.id_target = id_target
            self.mod = mod
            self.status = status
            self.data = data


    def _decode_sms(self, sms):
        """Decodes the sms received
        
        Args:
            sms (string): Encoded sms
        """

        try:
            header_sms = SmsTcpLayerFormatter.fill_header_string(SmsTcpLayerFormatter.hex_to_binary(sms[0:6]), 24)
            self.data = sms[6:]
            self.id_origin = int(header_sms[0:8], 2)
            self.id_target = int(header_sms[8:16], 2)
            self.mod = int(header_sms[16:21], 2)
            self.status = int(header_sms[21:], 2)
        except Exception as e:
            print("Bad length exception --->" + e)
        
    
    def enconde_sms(self):
        """Encode the Sms Stack Layer into a string sms 
        
        Returns:
            str: Sms Encoded
        """

        id_origin = SmsTcpLayerFormatter.fill_header_binary(self.id_origin, 8)
        id_target = SmsTcpLayerFormatter.fill_header_binary(self.id_target, 8)
        mod = SmsTcpLayerFormatter.fill_header_binary(self.mod, 5)
        status = SmsTcpLayerFormatter.fill_header_binary(self.status, 3)

        headers_binary = id_origin + id_target + mod + status
        headers_hexa = SmsTcpLayerFormatter.fill_header_string(SmsTcpLayerFormatter.binary_to_hex(headers_binary), 6)

        return headers_hexa + self.data


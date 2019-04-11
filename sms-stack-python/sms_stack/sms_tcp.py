import base64
from .sms_tcp_layer import SmsTcpLayer
from .sms_broadcaster import SmsBroadcaster
from .sms_tcp_layer import SmsTcpLayer
from random import randint
# from debug.test import TestBrod


class SmsTcp(object):

    def __init__(self, cipher_mode, cipher_key, sms_broadcaster):
        """Base class with some utility methods
        
        Args:
            cipher_mode (int): Cipher mode of the controller (0 Base64 | 1 AES CTR)
            cipher_key (str): Pre shared key in the AES cipher
            broadcast_send (SmsTcpBroadcaster): Class to send messages
        """

        self.sms_lenght = 140
        self.header_lenght = 14
        ##TOCHECK ***
        self.data_lenght = self.sms_lenght - self.header_lenght
        self.cipher_mode = cipher_mode
        self.cipher_key = cipher_key
        self.sms_broadcaster = sms_broadcaster

    def send_sms(self, tcp_layer, phone):
        """Send message throught the broadcaster
        
        Args:
            tcp_layer (SmsTcpLayer): Packet of sms stack
            phone ([str]): List of senders
        """

        sms_response =  tcp_layer.enconde_sms()
        self.sms_broadcaster.send_sms(sms_response, phone)
    
    def generate_random_key(self):
        """Generate a random key
        
        Returns:
            int: Key
        """

        return randint(0, 254)
    
    @staticmethod
    def encode_base_64(text):
        return base64.b64encode(bytes(text, "utf-8")).decode("ascii")
    
    @staticmethod
    def decode_base_64(text):
        return base64.b64decode(text).decode("ascii")

    @staticmethod
    def decode_base_64_byte(text):
        return base64.b64decode(text)

        

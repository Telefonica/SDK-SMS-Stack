from .sms_tcp import SmsTcp
from .sms_tcp_layer import SmsTcpLayer
from .cipher import AESCipher

class SmsTcpSender(SmsTcp):

    def __init__(self, cipher_mode, cipher_key, controller, broadcast_send):
        """Control the flow of a new conversation, dividing the message into n packets and creating an stream of data
        
        Args:
            cipher_mode (int): Cipher mode of the controller (0 Base64 | 1 AES CTR)
            cipher_key (str): Pre shared key in the AES cipher
            controller (SmsTcpController): Controller class to call the functions
            broadcast_send (SmsTcpBroadcaster): Class to send messages
        """

        super().__init__(cipher_mode, cipher_key, broadcast_send)
        self._controller = controller


    def create_new_conversation(self, sms, sender, ack_back = True):
        """Create a new conversation of a given message, sending the packets into the network
        
        Args:
            sms (string): String to send
            sender ([string]): list of senders
        """

        key = super().generate_random_key()
        ciphered_text = self.cipher_text(sms)
        message_to_send = self.split_message(ciphered_text)
        if(message_to_send is None):
            return
        for idx, message in enumerate(message_to_send):
            self.send_message(message, key, idx, idx == len(message_to_send) -1, sender, ack_back)
        self._controller.handle_final_message_sent(message_to_send, sender)
    
    def send_message(self, text, key, s_begin, is_fin, sender, ack_back):
        """Send a single packet
        
        Args:
            text (str): String to send
            key (int): Flag of the key
            s_begin (int): flag of the position of the packet
            is_fin (bool): To determine the flag of fin
            sender ([str]): List of sender
        """

        sms_layer = SmsTcpLayer(id=0, key=key, syn= + (not is_fin), ack=0, psh=+ (not ack_back), fin=+is_fin, s_begin=s_begin, cipher=self.cipher_mode, check_sum=0, data=text)
        self.send_sms(sms_layer, sender)
        self._controller.handle_message_sent(sms_layer, sender)

    def cipher_text(self, text):
        """Encode the message with the given cipher methdo
        
        Args:
            text (str): Message to encode   
        
        Returns:
            str: Message encoded
        """

        cipher_text = ""
        if(self.cipher_mode == 0):
            cipher_text = SmsTcp.encode_base_64(text)
        elif (self.cipher_mode == 1):
            aes_cypher = AESCipher()
            cipher_text = aes_cypher.encrypt_message(self.cipher_key, text)
        else:
            cipher_text = SmsTcp.encode_base_64(text)

        return cipher_text

    def split_message(self, cipher_text):
        """Split message in n given parts
        
        Args:
            cipher_text (str): Encoded message to split
        
        Returns:
            [str]: List with all parts splitted
        """

        sms_len = self.sms_lenght
        return [cipher_text[i:i+sms_len] for i in range(0, len(cipher_text), sms_len)]
        
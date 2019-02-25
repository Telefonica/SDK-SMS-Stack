from .sms_tcp import SmsTcp
from .sms_tcp_layer import SmsTcpLayer
from threading import Timer
# from sms_tcp_layer import TcpLayer

class SmsTcpReceiver(SmsTcp):

    def __init__(self, cipher_mode, cipher_key, controller, broadcast_send):
        """Handle the reception of the sms stream, checking the streams and constructing the packet
        
        Args:
            cipher_mode (int): Cipher mode of the controller (0 Base64 | 1 AES CTR)
            cipher_key (str): Pre shared key in the AES cipher
            controller (SmsTcpController): Controller class to call the functions
            broadcast_send (SmsTcpBroadcaster): Class to send messages
        """

        super().__init__(cipher_mode, cipher_key, broadcast_send)
        self._controller = controller
        self._message_received = []

    def add_new_message(self, sms, receiver):
        """Add new message to the stream list
        
        Args:
            sms (str): String with a SmsTcpLayer coded
            receiver ([str]): list of senders
        """

        try:
            sms_layer = SmsTcpLayer(sms=sms)
            self._message_received.append(sms_layer)
            if(self._controller != None):
                # check the name of methods
                self._controller.handle_message_received(sms_layer, receiver)
            
            if(sms_layer.fin == 1 and sms_layer.ack == 0):
                t = Timer(0.5, self.check_sms_tcp_stream, args=(sms_layer, receiver))
                t.start()
        except Exception as e:
            print("Error implementing sms layer {}".format(e))
            return
    
    def check_sms_tcp_stream(self, sms, receiver):
        """Check the end of the strem in order to detect packet losss
        
        Args:
            sms (SmsTcpLayer): Layer with the fin flag
            receiver ([str]): List of senders
        """

        sms_stream = sorted((x for x in self._message_received if x.key == sms.key),
            key=lambda x: x.s_begin
        )
        missing = self.missing_numbers(sms_stream)
        if(len(missing) > 0):
            response_failed = SmsTcpLayer.message_packet_lost(sms, missing[0])
            super().send_sms(response_failed, receiver)
            self._message_received = self.remove_sms_by(sms.key, missing[0])
            return

        response =  SmsTcpLayer.message_packet_lost(sms)
        super().send_sms(response, receiver)
        self._message_received = self.remove_sms_by(sms.key)
        self._controller.handle_final_message_received([x.data for x in sms_stream], receiver)

    
    def missing_numbers(self, num_list):
        """Get the position of the packet lost in the stream
        
        Args:
            num_list ([SmsTcpLayer]): List with all the packets of the stream
        
        Returns:
            set(int): Set with the numbers of the lost packets
        """

        sms_index = [x.s_begin for x in num_list]
        original_list = [x for x in range(sms_index[0], sms_index[-1] + 1)]
        sms_index_set = set(sms_index)
        return (list(sms_index_set ^ set(original_list)))

    
    def remove_sms_by(self, key, s_begin=None):
        """Removes the stream of sms by a given key
        
        Args:
            key (int): Key to identify the stream of sms
            s_begin (int, optional): Defaults to None. Position in which the stream must be deleted
        
        Returns:
            [SmsTcpLayer]: List without the stream of packets
        """

        if(s_begin != None):
            return [x for x in self._message_received if x.key != key and x.s_begin >= s_begin]
        else:
            return [x for x in self._message_received if x.key != key]
    
    
   

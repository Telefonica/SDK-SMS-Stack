from sms_stack.sms_controller_observer import SmsControllerObserver
from sms_stack.sms_tcp_controller import SmsTcpController
from sms_stack.sms_tcp_layer import SmsTcpLayer

class SmsStackController(SmsControllerObserver):

    def __init__(self, broadcast_send, callback_message):
        """Implementation of Sms Stack Controller
        
        Args:
            broadcast_send (BroadcastSender): The broadcast object to send sms
            callback_message (function): Callback function to retrieve the message
        """

        super().__init__()
        # Initialize the controller with the chipher key PATATA in AES CBC
        self.controller = SmsTcpController(self, broadcast_send, cipher_mode=1, cipher_key='PATATA')
        self.callback_message = callback_message

    # Handler message methods
    def handle_final_message_received(self, messages, sender):
        message = self.controller.process_message(messages)
        self.callback_message(message)


    def handle_message_received(self, layer, sender):
        return


    def handle_final_message_sent(self, messages, recipient):
        return


    def handle_message_sent(self, layer, recipient):
        return
    
    def send_message(self, sms, recipient="", recipients=[]):
        """Send message to a number
        
        Args:
            sms (string): Sms Stack encoded sms
            recipient (str, optional): Defaults to "". Recipient to send the message
            recipients (list, optional): Defaults to []. List of recipients to send the message
        """
        sms_layer = SmsTcpLayer(sms=sms)
        message = sms_layer.enconde_sms()
        
        recipients.append(recipient)
        for rec in recipients:
            self.controller.send_message(message, rec)

    def message_received(self, sms, sender):
        """Method to control a new message received
        
        Args:
            sms (string): Message encoded
            sender (string): Number of the sender
        """
        self.controller.receive_message(sms, sender)





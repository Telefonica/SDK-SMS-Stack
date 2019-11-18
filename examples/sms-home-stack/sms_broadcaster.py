from sms_stack.sms_broadcaster import SmsBroadcaster
import time, sys

class SmsBroadcast(SmsBroadcaster):

    def __init__(self, serial):
        """Implementation class of broadcaster, it sends the message through serial port encoding
        
        Args:
            serial (int): Serial code used to send the message
        """

        self.serial_port = serial
        self.send_message = 'AT+CMGS="{}"\r'


    def send_sms(self, sms, recipient):
        """Send and sms to a given recipient
        
        Args:
            sms (string): Sms Stack encoded sms
            recipient (string): Recipient of the sms
        """

        self.serial_port.write(str.encode(self.send_message.format(recipient)))
        time.sleep(1)
        self.serial_port.write(str.encode(sms + chr(26)))
        time.sleep(1)


# Ideas Locas CDO
# SMS Stack test server
# Based on SIMSMS1.py
# http://www.python-exemplary.com/

import RPi.GPIO as GPIO
import serial
import time, sys
import datetime
import re

from sms_stack_controller import SmsStackController
from sms_broadcaster import SmsBroadcast
from sms_layer_rasp import SmsTcpLayerRasp

# GPIO management
P_BUTTON = 24 # Example, response wiring button GPIO

# Serial port
#SERIAL_PORT = "/dev/ttyAMA0"  # Raspberry Pi 2
SERIAL_PORT = "/dev/ttyS0"    # Raspberry Pi 3

class StackServerSMS(object):

    def __init__(self):
        self.text_mode = 'AT+CMGF=1\r'
        self.read_first_message = 'AT+CMGR=1\r'
        self.del_all_message = 'AT+CMGDA="DEL ALL"\r'
        self.read_unread_messages = 'AT+CMGL="REC UNREAD"\r'
        self.del_read_messages = 'AT+CMGD=0,1\r'
        self.lightbull = False
        self.serial_port = serial.Serial(SERIAL_PORT, baudrate = 9600, timeout = 5)
        self.sender = SmsBroadcast(self.serial_port)
        self.sms_stack_controller = SmsStackController(self.sender, self.message_processed)
        self.enable_sms()

    # GPIO initialization and definitions
    def setup_gpio(self):
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(P_BUTTON, GPIO.IN, GPIO.PUD_UP)
        
    def enable_sms(self):
        """Enables all the sms capabilities in the GPIO base
        """

        # GPIO Initialization
        self.setup_gpio() # init
        self.serial_port.write(str.encode(self.text_mode)) # set to text mode
        time.sleep(1)
        self.serial_port.write(str.encode(self.del_all_message))
        time.sleep(1)
        self.serial_port.read(self.serial_port.inWaiting()) # Clean buf

    def receive_sms(self):
        print("Listening for incomming Stack SMS...")
        while True:
           try:
              self.serial_port.write(str.encode(self.read_unread_messages))
              time.sleep(1)
              reply = self.serial_port.read(self.serial_port.inWaiting()).decode()

              if "CMGL:" in reply:
                self.process_messages(reply)
                self.delete_messages()
            
              time.sleep(1)
           except KeyboardInterrupt:
              break
    
    def process_messages(self, reply):
        response_list = self.parse_sms(reply)
        for response in response_list:
            self.sms_stack_controller.message_received(response[0], response[1])

    
    @staticmethod
    def message_processed(message):
        """Process message received
        
        Args:
            message (str): Message received
        """

        stack_layer = SmsTcpLayerRasp(sms=message)
        print("Hemos recibido id_origin: {}".format(stack_layer.id_origin))
        print("Hemos recibido id_sender: {}".format(stack_layer.id_target))
        print("Hemos recibido mod: {}".format(stack_layer.mod))
        print("Hemos recibido status: {}".format(stack_layer.status))

    
    def delete_messages(self):
        """Delete a message once is read
        """

        self.serial_port.write(str.encode(self.del_read_messages)) # delete read nessages
        time.sleep(1)
        self.serial_port.read(self.serial_port.inWaiting()) # Clear buf

   
    def parse_sms(self, sms):
        """Due to bad formatting in GPIO, parsing method to retrieve the actual message
        
        Args:
            sms (str): Bad formatted messsage
        
        Returns:
            str: Message retrieved
        """

        res = []
        match = re.findall("\+CMGL: (\d+),""(.+)"",""(.+)"",(.*),""(.+)""\n(.+)\n", sms)

        for each in match:
            res.append((each[5], each[2][1:-1]))
        return res
            
if __name__ == "__main__":
    print("Stack SMS server, initializing ...")
    stacksms=StackServerSMS()

    stacksms.receive_sms()




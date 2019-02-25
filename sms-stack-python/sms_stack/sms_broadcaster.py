from abc import ABC, abstractmethod

class SmsBroadcaster(ABC):

    @abstractmethod
    def send_sms(self, sms, sender):
        """Abstract method to implement in child class to work with the framework
        
        Args:
            sms (str): message to send
            sender (str): sender number
        """
        return NotImplemented
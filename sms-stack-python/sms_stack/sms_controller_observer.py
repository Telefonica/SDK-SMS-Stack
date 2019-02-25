from abc import ABC, abstractmethod

class SmsControllerObserver(ABC):

    @abstractmethod
    def handle_final_message_received(self, messages, sender):
        """Handle the final message received, endpoint to retreive the message received, use process_message() of SmsTcpController to unwrap them
        
        Args:
            messages ([str]): List of messages returned
            sender ([str]): List of senders
        """

        return NotImplemented

    @abstractmethod
    def handle_message_received(self, layer, sender):
        """Called each time a packet is received
        
        Args:
            layer (SmsTcpLayer): Packet
            sender ([str]): List of senders
        """

        return NotImplemented

    @abstractmethod
    def handle_final_message_sent(self, messages, sender):
        """Called when a stream of messages is sent
        
        Args:
            messages ([str]): list of messages
            sender ([str]): List of senders
        """

        return NotImplemented

    @abstractmethod
    def handle_message_sent(self, layer, sender):
        """Called each time a message is sent
        
        Args:
            layer ([str]): layer
            sender ([str]): List of senders
        """

        return NotImplemented
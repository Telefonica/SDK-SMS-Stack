import pytest


import sys, os
current_path = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, current_path + "/../")

from ..sms_tcp_receiver import SmsTcpReceiver


import pytest


import sys, os
current_path = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, current_path + "/../")

from ..sms_tcp_layer import SmsTcpLayer
from ..sms_tcp import SmsTcp


def test_sms_tcp_layer_encode():
    result = SmsTcpLayer(id=0, 
            key=17, 
            syn=1, 
            ack=1, 
            psh=1, 
            fin=1, 
            s_begin=0,
            cipher=0, 
            check_sum=0, 
            data="hola que tal").enconde_sms()

    assert result == '08f80000000000hola que tal'

def test_sms_tcp_layer_decode():
    sms_tcp = SmsTcpLayer()
    sms_tcp._decode_sms('08f80000000000hola que tal')
    assert sms_tcp.id == 0
    assert sms_tcp.key == 17
    assert sms_tcp.syn == 1
    assert sms_tcp.ack == 1
    assert sms_tcp.psh == 1
    assert sms_tcp.fin == 1
    assert sms_tcp.s_begin == 0
    assert sms_tcp.cipher == 0
    assert sms_tcp.check_sum == 0
    assert sms_tcp.data == "hola que tal"

def test_sms_tcp_encode_base64():
    assert SmsTcp.encode_base_64("Hello world") == "SGVsbG8gd29ybGQ="
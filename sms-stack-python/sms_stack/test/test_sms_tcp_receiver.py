import pytest


import sys, os
current_path = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, current_path + "/../")

from ..sms_tcp_receiver import SmsTcpReceiver
from ..sms_tcp_layer import SmsTcpLayer


def test_add_message(capsys):
    mock = SmsTcpReceiver(0, "key", None, None)
    before = len(mock._message_received)
    hex = "12fab"
    mock.add_new_message(hex, "receiver")
    captured = capsys.readouterr() 
    assert len(mock._message_received) > before
    assert "Error" not in captured.out


def test_add_message_launch_exception(capsys):
    mock = SmsTcpReceiver(1, "key", None, None)
    not_hex = "ZZZ"
    mock.add_new_message(not_hex, "receiver")
    captured = capsys.readouterr() 
    assert "Error implementing sms layer" in captured.out

#Check fails
def test_missing_number_fail():
    mock = SmsTcpReceiver(1, "key", None, None)
    mock.missing_numbers([])
    assert len(mock.missing_numbers([])) == 0

def test_missing_number_fail_2():
    mock = SmsTcpReceiver(1, "key", None, None)
    my_list = [SmsTcpLayer(s_begin = 0), SmsTcpLayer(s_begin=0)]
    mock.missing_numbers(my_list)
    assert len(mock.missing_numbers([])) == 0
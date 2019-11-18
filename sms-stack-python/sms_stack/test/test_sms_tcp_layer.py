import pytest


import sys, os
current_path = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, current_path + "/../")

from ..sms_tcp_layer_formatter import SmsTcpLayerFormatter


def test_hex_to_binary():
    test1 = SmsTcpLayerFormatter.hex_to_binary("abcd1234")
    test2 = SmsTcpLayerFormatter.hex_to_binary("noHex")
    assert (test1 is not None) and (test2 is None)


def test_binary_to_hex():
    test1 = SmsTcpLayerFormatter.binary_to_hex("011001")
    test2 = SmsTcpLayerFormatter.binary_to_hex(12390)
    assert (test1 is not None) and (test2 is None)

def test_dec_to_binary():
    test1 = SmsTcpLayerFormatter.dec_to_binary(1323)
    test2 = SmsTcpLayerFormatter.dec_to_binary("ab1d")
    assert (test1 is not None) and (test2 is None)

def test_fill_header_string():
    b = "010110"
    length = 10
    response = SmsTcpLayerFormatter.fill_header_string(b, length)
    assert len(response) == length

def test_fill_header_binary():
    b = "010110"
    length = 59
    response = SmsTcpLayerFormatter.fill_header_string(b, length)
    assert len(response) == length
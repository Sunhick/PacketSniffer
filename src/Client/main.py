#!/usr/bin/python
# -*- coding: utf-8 -*-

"""
main.py

client
"""

__author__ = "Sunil"
__copyright__ = "Copyright 2016, Packetsniffer Project"
__license__ = "GPL 3.0"
__version__ = "0.0.0"
__email__ = "sunhick@gmail.com"


import sys
import logging
import os
import logging.config


def start(*argv):
    sniffer = ClientSniffer()
    sniffer.run()

if __name__ == '__main__':
    start(sys.argv)

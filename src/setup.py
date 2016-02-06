"""
setup.py

client
"""

__author__ = "Sunil"
__copyright__ = "Copyright 2016, Packetsniffer Project"
__license__ = "GPL 3.0"
__version__ = "0.0.0"
__email__ = "sunhick@gmail.com"


# from distutils.core import setup
from setuptools import setup

#This is a list of files to install, and where
#(relative to the 'root' dir, where setup.py is)
#You could be more specific.
files = ["Server/*","Client/*"]

setup(name = "PacketSniffer",
    version = "0.0.0",
    description = "Packet sniffer tool",
    author = "sunil",
    author_email = "sunhick@gmail.com",
    install_requires=['pypcap>=1.1.4'],
    url = "whatever",
    #Name the folder where your packages live:
    #(If you have other packages (dirs) or modules (py files) then
    #put them into the package directory - they will be found 
    #recursively.)
    packages = ['Server', 'Client'],
    #'package' package must contain files (see list above)
    #I called the package 'package' thus cleverly confusing the whole issue...
    #This dict maps the package name =to=> directories
    #It says, package *needs* these files.
    package_data = {'PacketSniffer' : files },
    #'runner' is in the root.
    scripts = ["runner"],
    long_description = """Packet Sniffer""" 
    #
    #This next part it for the Cheese Shop, look a little down the page.
    #classifiers = []

    # install_requires = ['pypcap>=1.1.4']
)


<a name = "logo"/>
<div align = "center">
<img src = "https://github.com/Sunhick/PacketSniffer/blob/master/misc/logo.png" alt = "Packet Sniffer Logo" width = "142" height = "142" />
</a>
</div>

Windows, Linux, OSX: [![Build Status](https://travis-ci.org/Sunhick/keylogger.svg?branch=travis)](https://travis-ci.org/Sunhick/keylogger)

<a name = "CSCI-5448-Spring-2016-Project-Packet-Sniffer"/>
# CSCI 5448 Spring 2016 Project - Packet Sniffer

A packet sniffer application using Java and having a GUI that can capture and 
analyze packets being transmitted and received over a network. The captured packets can be 
used to gain information about the type and content of messages being transmitted over the 
network.

<a name="Currently-Supported-Platforms"/>
## Currently Supported Platforms

- **GNU/Linux**
- **Darwin/OS X**
- **Windows**

<a name = "getting-sources"/>
## Getting the sources

Get the latest stable code of the Packet Sniffer. You can download the code either as zipped file by clicking on the "download ZIP" button in the repository or forking it. You may also get sources by cloning the git repository:

    git clone git@github.com:Sunhick/PacketSniffer.git

(If you are behind a firewall, you may need to use the `https` protocol instead of the `git` protocol:

    git config --global url."https://".insteadOf git://

Be sure to also configure your system to use the appropriate proxy settings, e.g. by setting the `https_proxy` and `http_proxy` variables.)


<a name = "prerequisites"/>
## Installing prerequisites

1.	To compile the above code you must have Java SDK 7.0 and JRE installed. Java SDK and JRE can be installed from http://www.oracle.com/technetwork/java/javase/downloads/index.html.

2.	The downloaded code has dependencies on libpcap and jpcap libraries. Which can be dowload from their sites
	libpcap: https://wiki.wireshark.org/libpcap
	jpcap: http://jpcap.gitspot.com/download.html

	or you can use any of the package managers to install. For eg: mac port, brew, apt-get etc.

3.	Copy the jpcap.jar into one of the many java path locations. For eg: /Library/Java/Extensions/

4.	Along with the jar file you must also copy jpcap.dll in windows / libjpcap.jnilib in Mac into the same location(where jar was copied)

<a name = "compiling"/>
## Compiling the code and running

Open the code in your favourite IDE like eclipse etc. which has java environment setup for compiling, debugging and running the code. Select the PacketSniffer server and click the "Run as Java Application" button.  If prequisites are setup properly the application will launch. 

<a name = "troubleshooting"/>
### Troubleshooting
1.	Project not compiling?
	
	If you get any compilation error then try adding the jpcap.jar file to the project as a external jar in the build configuration. 

2.	Run time error in launching the application?
	
	If you get this error, it usually means that the libpcap or jpcap is not properly installed. JRE is not able to resolve the jar files which could happen if the jpcap.jar and libjpcap.jnilib/jpcap.dll is copied in to different location and it's not added in the CLASS/PATH variable.

<a name = "resources"/>
## Resources:
- **Homepage:** <http://github.com/Sunhick/PacketSniffer>
- **Git clone URL:** <git@github.com/Sunhick/PacketSniffer.git>
- **Source code:** <https://github.com/Sunhick/PacketSniffer>
- **JAVA SDK:** <http://www.oracle.com/technetwork/java/javase/downloads/index.html>
- **JRE:** <http://www.oracle.com/technetwork/java/javase/downloads/index.html>
- **LibPcap:** <https://wiki.wireshark.org/libpcap>
- **JPcap:** <http://jpcap.gitspot.com/download.html>

## 

<a name = "Authors"/>
## Authors

* **Sunil Murthy**
* **Nehal Kamat**
* **Apoorva Bapat**

<a name = "license"/>
## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

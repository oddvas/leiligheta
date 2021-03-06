Leiligheta
==========

"Leiligheta" is a system for controlling both light, audio and other appliances in my appartment.

Server
----
This is the main part of the software. It has the X10 controller module, a connection to the receiver and the database. 

Client
----
This is the client class. Both the server and client use the same CommandStrings-class for implementing the communication protocol. This class is used both for the desktop GUI, and the Android client. 

GUI
----
This is the user interface and connects to the server through the Client-class. It contains buttons for turning lights on/off or sets the brightness value for dimmable lights. It also contains a volume slider for the main volume on the receiver, and buttons for changing the input source.

X10
----
For controlling lights and appliances, I use X10. Each appliance/lamp is connected through their own X10 module, which has an unique address. The server is connected to a RS232 X10 controller, and transmit signals to each module through the power line.

Receiver
----
This class is used to connect to the Onkyo receiver. The receiver uses a standard TCP-socket and communicate with the ISCP-protocol. Instead of letting each client connect to the receiver directly, the communication goes through the server. Whenever the server receives an update, it notifies all connected clients and through UDP multicast.

Database
----
The database is used for permanent storage, i.e. whenever a lamp is turned on/off/dimmed, the value is stored in the database. If the Leiligheta-server has to reboot, it looks up in the database and set its own internal variables accordingly. The database also holds an entry for every X10 module connected to the system.

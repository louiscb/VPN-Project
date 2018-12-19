# VPN Project

Virtual Private Network software built with Java using OpenSSL.

## How to run the VPN

In order to run the VPN you need to run the ForwardServer class.

You should run the ForwardServer class with specific program arguments:

`--handshakeport=2206 --usercert=certs/server.pem --cacert=certs/CA.pem --key=certs/serverprivatekey.pem`

These are program arguments for the client:

`--handshakehost=localhost --handshakeport=2206 --targethost=localhost --targetport=1337 --usercert=certs/client.pem --cacert=certs/CA.pem --key=certs/clientprivatekey.der`

## How to test it

Open a netcat listening to the target port specified by the client.

Connect a netcat to the client's forward port that it specifies in the logs of its program after the handshake is complete.

You should then be able to write text between the two netcats.

Test the encryption is working by looking at the ForwardThread file and uncomment the block of code specified in the file.

## Certificates

This program requires that the server's and client's certificates have been signed by the same CA.

Generate the CA certificate and its private key:

`openssl req -new -x509 -newkey rsa:2048 -keyout CAprivatekey.pem -out CA.pem`

Generate the CSR for the client/server:

`openssl req -out client.csr -new -newkey rsa:2048 -keyout clientprivatekey.pem`  

Sign the CSR with the CA:

`openssl x509 -req -in client.csr -CA CA.pem -CAkey CAprivatekey.pem -CAcreateserial -out client.pem`

We also need to convert .pem formatted key to a .der:

`openssl pkcs8 -nocrypt -topk8 -inform PEM -in clientprivatekey.pem -outform DER -out clientprivatekey.der`

## How does it work?

Below is a detailed set of steps breaking down how, using this project, a client and server create a secure communication channel.

The step numbers are referenced in the code at the points where each task is completed. 

1. ClientHello 
    1. the first message the client sends to the server
    2. MessageType = ClientHello
    3. Certificate = client’s x509 certificate (as a string)
2. Server Verifies Client certificate
3. ServerHello (if verified)
    1. MessageType = ServerHello
    2. Certificate = server’s x509 certificate
4. Client verifies server certificate
5. Client requests port forwarding to target host & port
    1. MessageType = forward
    2. TargetHost = server.kth.se (target server)
    3. TargetPort = 6789 (port of server)
6. If server agrees on target, sets up session:
    1. Generate Session Key & IV
    2. Encrypt Session & IV with client's public key (taken from client's cert)
    3. Create socket endpoint, and port number for session communication
7. Server sends client session message
    1. MessageType = session
    2. SessionKey = AES key encrypted with client’s Public Key (as a string)
    3. SessionIV = AES CTR IV, encrypted with …
    4. ServerHost = name of host to which client should connect (address of VPN server)
    5. ServerPort = TCP port which client should connect (VPN Server’s port it has decided on)
8. Client receives session message and knows information for a session, handshake is complete
9. VPN client connects with user
    1. VPN client decides on internal communication host and port for the user
    2. User creates TCP connection with client host and client port
    3. Now data that user sends goes through VPN client, gets encrypted, gets sent to VPN server, gets decrypted and sent to target.
10. Setup of secure communication channel is now complete 

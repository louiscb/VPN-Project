# VPN Project

Virtual Private Network software built with Java using OpenSSL.

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

## Useful Commands

Convert .pem formatted key to a .der:

`openssl pkcs8 -nocrypt -topk8 -inform PEM -in private-pkcs1.pem  -outform DER
-out private-pkcs8.der`

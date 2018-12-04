# VPN Project

Virtual Private Network software built using Java using OpenSSL.

## Useful Commands

Convert .pem formatted key to a .der:

'openssl pkcs8 -nocrypt -topk8 -inform PEM -in private-pkcs1.pem  -outform DER
-out private-pkcs8.der'

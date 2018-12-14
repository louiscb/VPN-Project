package communication.handshake;/*
 * Handshake message encoding/decoding and transmission
 * for IK2206 project.
 *
 */

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/*
 * A Handshake message is represented as a set of parameters -- <key, value> pairs.
 * Extends Properties class.
 */

public class HandshakeMessage extends Properties {
    
    /*
     * Get the value of a parameter 
     */
    public String getParameter(String param) {
        return this.getProperty(param);
    }

    /* 
     * Assign a parameter 
     */
    public void putParameter(String param, String value) {
        this.put(param, value);
    }

    /*
     * Send a handshake message out on a socket
     *
     * Use the built-in encoding of Properties as XML:
     *   - Encode the message in XML
     *   - Convert XML to a byte array, and write the byte array to the socket
     *
     * Prepend the byte array with an integer string with the length of the string. 
     * The integer string is terminated by a whitespace.
     */
    public void send(Socket socket) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        String comment = "From " + InetAddress.getLocalHost() + ":" + socket.getLocalPort() +
            " to " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.storeToXML(byteOutputStream, comment);
        byte[] bytes = byteOutputStream.toByteArray();
        socket.getOutputStream().write(String.format("%d ", bytes.length).getBytes(StandardCharsets.UTF_8));        
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();
        
    }

    /*
     * Receive a handshake message on a socket
     *
     * First read a string with an integer followed by whitespace, 
     * which gives the size of the message in bytes. Then read the XML data
     * and convert it to a HandshakeMessage.
     */
    public void recv(Socket socket) throws IOException {
        int length = 0;

        for (int n = socket.getInputStream().read(); !Character.isWhitespace(n); n = socket.getInputStream().read()) {
            length = length*10 + Character.getNumericValue(n);
        }

        byte[] data = new byte[length];
        int nread = 0;

        while (nread < length) {
            nread += socket.getInputStream().read(data, nread, length-nread);
        }
        this.loadFromXML(new ByteArrayInputStream(data));
    }

};

package communication.threads; /**
 * ForwardThread handles the TCP forwarding between a socket input stream (source)
 * and a socket output stream (destination). It reads the input stream and forwards
 * everything to the output stream. If some of the streams fails, the forwarding
 * is stopped and the parent thread is notified to close all its connections.
 */

import communication.handshake.Handshake;
import communication.session.SessionDecrypter;
import communication.session.SessionEncrypter;

import javax.crypto.CipherOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
 
public class ForwardThread extends Thread  {
    private static final int READ_BUFFER_SIZE = 8192;
    InputStream mInputStream = null;
    OutputStream mOutputStream = null;
    ForwardServerClientThread mParent = null;
    SessionEncrypter sessionEncrypter;
    SessionDecrypter sessionDecrypter;

    /**
     * Creates a new traffic forward thread specifying its input stream,
     * output stream and parent thread
     */
    public ForwardThread(ForwardServerClientThread aParent, InputStream aInputStream, OutputStream aOutputStream)
    {
        mInputStream = aInputStream;
        mOutputStream = aOutputStream;
        mParent = aParent;
        sessionEncrypter = new SessionEncrypter(Handshake.sessionKey, Handshake.iv);
        sessionDecrypter = new SessionDecrypter(Handshake.sessionKey, Handshake.iv);
    }
 
    /**
     * Runs the thread. Until it is possible, reads the input stream and puts read
     * data in the output stream. If reading can not be done (due to exception or
     * when the stream is at his end) or writing is failed, exits the thread.
     */
    public void run() {
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        try {
            while (true) {
                /**
                 * If we can call getListenSocket then we know we are being called by a server
                 * so we should encrypt what is being sent. Otherwise we know it is the client and we should decrypt.
                 *
                 * To test that the encryption is working uncommment the block of code in the else and comment the default code.
                 * You should be see that any plaintext you send appears as gibberish on the recieving side.
                 */
                if (mParent.getListenSocket() != null) {
                    int bytesRead = mInputStream.read(buffer);
                    if (bytesRead == -1)
                        break; // End of stream is reached --> exit the thread

                    CipherOutputStream cipherOutputStream = sessionEncrypter.openCipherOutputStream(mOutputStream);
                    cipherOutputStream.write(buffer, 0, bytesRead);
//                } else {
//                    CipherInputStream cipherInputStream = sessionDecrypter.openCipherInputStream(mInputStream);
//                    int bytesRead = cipherInputStream.read(buffer);
//                    if (bytesRead == -1)
//                        break; // End of stream is reached --> exit the thread
//                    mOutputStream.write(buffer, 0, bytesRead);
//                    mOutputStream.flush();
//                }
                //UNCOMMENT THIS TO TEST THAT ENCRYPTION IS WORKING
                } else {
                    int bytesRead = mInputStream.read(buffer);
                    if (bytesRead == -1)
                        break;

                    mOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            // Read/write failed --> connection is broken --> exit the thread
            e.printStackTrace();
        }
 
        // Notify parent thread that the connection is broken and forwarding should stop
        mParent.connectionBroken();
    } 
}

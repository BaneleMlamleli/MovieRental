package MovieRental;
/*
 * ClientApp.java
 *
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**

 * @author
 */
public class ClientApp
{    
    public static Socket server;
    
    /** Creates a new instance of ClientApp */
    public ClientApp()
    {
        // Attempt to establish connection to server
        try
        {
            // Create socket
            server = new Socket("127.0.0.1", 12345);
        }
        catch (IOException ioe)
        {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }
    
    public static void communicate()
    {
        // The connection has been established - now send/receive.
        
        try
        {        
            // Step 1: create channels
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(server.getInputStream());
            
            // Step 2: communicate
            out.writeObject("Request received. Now processing database insertion");
            out.flush();
            String response = (String)in.readObject();
            System.out.println("Server response >>> " + response);
            
            // Step 3: close down
            out.close();
            in.close();
            server.close();        
        }
        catch (IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }
      
//    public static void main(String[] args)
//    {
//        ClientApp client = new ClientApp();
//        client.communicate();
//    }    
}

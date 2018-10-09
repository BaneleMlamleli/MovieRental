package MovieRental;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 *
 * @author 
 */
public class ServerApp
{
    
    // Server socket
    public static ServerSocket listener;
    
    // Client connection
    public static Socket client;
    
    /** Creates a new instance of ServerApp */
    public ServerApp()
    {
        // Create server socket
        try {
            listener = new ServerSocket(12345, 10);
        }
        catch (IOException ioe)
        {
          System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    
    public static void listen()
    {
        // Start listening for client connections
        try {
          System.out.println("Server is listening...");
          client = listener.accept();  
          System.out.println("Request received. Now processing database insertion");
          
          processClient();
        }
        catch(Exception ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    
    public static void processClient()
    {
        // Communicate with the client
        
        // First step: initiate channels
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            
            // Step 2: communicate
            String msg = (String)in.readObject();
            System.out.println("Client request >>> " + msg);
            out.writeObject("... "+msg+" ...");
            out.flush();
            
            // Step 3:close down
            out.close();
            in.close();
            client.close();        
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
//  
//    public static void main(String[] args)
//    {
//        // Create application
//        ServerApp server = new ServerApp();
//        
//        // Start waiting for connections
//        server.listen();
//    }    
}

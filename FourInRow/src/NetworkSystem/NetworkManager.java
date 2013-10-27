package NetworkSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkManager {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String serverName;
    private String clientName;
    
    public boolean createServer(String name, int port) {        
        try {
            if(serverSocket != null)
                serverSocket.close();
            
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
                
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
            output.writeUTF(name);
            
            serverName = name;
            clientName = input.readUTF();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    public boolean createClient(String name, String ip, int port) {
        try {
            if(socket != null)
                socket.close();
            
            socket = new Socket(ip, port);
            
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            
            serverName = input.readUTF();
            clientName = name;
            output.writeUTF(name);
            return true;
        } catch (UnknownHostException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
    
    public void sendMove(int column) throws IOException {
        output.writeInt(column);
    }
    
    public int readMove() throws IOException {
        return input.readInt();
    }
    
    public String getServerName() {
        return serverName;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void closeConnection() {
        try {
            if(socket != null)
                socket.close();
            if(serverSocket != null)
                serverSocket.close();    
            
            serverSocket = null;
            socket = null;
        } catch (IOException ex) {
        }
    }
}

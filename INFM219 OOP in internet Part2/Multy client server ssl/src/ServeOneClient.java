import java.io.*;
import java.net.*;

class ServeOneClient extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public ServeOneClient(Socket s)  throws IOException {
        socket = s;
        in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
        // Enable auto-flush:
        out = new PrintWriter( new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),true);
        System.out.println ("new client call");
        start();    // Calls run()
    }
    
    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                System.out.println("Client sent: " + str);
                if (str.equalsIgnoreCase("END")){
                    out.println("client sent \"End\" - closing connection...");
                    System.out.println("Closing connection...");
                    break;
                }else {
                    out.println("the client's line /"+str+ "/ has "+str.length()+" characters");
                }         
            }
            System.out.println("client sent \"End\" -closing...");
        } catch (IOException e) {  }
        finally {
            try {
                socket.close();
            } catch(IOException e) {}
        }
    }
    
}

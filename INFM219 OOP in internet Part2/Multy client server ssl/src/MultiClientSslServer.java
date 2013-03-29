import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class MultiClientSslServer {
    private static int port = 2001;

    public static void main(String args[])throws IOException {
    	
        Properties props = System.getProperties();       
        props.put("javax.net.ssl.trustStore","serts\\samplecacerts"); 
        props.put("javax.net.ssl.trustStorePassword","changeit");
        
        ServerSocket ss=null;
        boolean auth = false;
        
        if (args.length >= 1) {
            if(args[0].equalsIgnoreCase("auth")){
                auth=true;
            }
        }
        
        try {
            ServerSocketFactory ssf = getServerSocketFactory();
            ss = ssf.createServerSocket(port);
            System.out.print("SslServer started");
            if(auth){
                ((SSLServerSocket)ss).setNeedClientAuth(true);
                System.out.println(" with client authentication");
            }
            else{
                System.out.println(" without client authentication");
            }
        } catch (IOException e) {
            System.out.println("Unable to start MultiClientSslServer: " +
                    e.getMessage());
            e.printStackTrace();
        }
        try {
            while(true) {
                // Blocks until a connection occurs:
                Socket socket = ss.accept();
                try {
                    new ServeOneClient(socket);
                } catch(IOException e) {
                    socket.close();
                }
            }
        } finally {
            ss.close();
        }
    }
    
    private static ServerSocketFactory getServerSocketFactory() {
        SSLServerSocketFactory ssf = null;
        try {
            // set up key manager to do server authentication
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "passphrase".toCharArray();
            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("serts\\server\\testkeys"), passphrase);
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null); //подразбиращи се TrustManagerFactory
            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }       
    }
}
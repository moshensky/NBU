import java.io.*;
import javax.net.ssl.*;
public class HTTPSClient {
    public static void main(String[] args) {
        int port = 443; // подразбиращ се  https порт
        String host = "www.verisign.com";;
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            Writer out = new OutputStreamWriter(socket.getOutputStream());
            socket.startHandshake();
            out.write("GET http://" + host + "/ HTTP/1.1\r\n");
            out.write("Host: " + host + "\r\n");
            out.write("\r\n");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                    // четене на заглавната част ( header)
            String s;
            while (!(s = in.readLine()).equals("")) {
                System.out.println(s);
            }
            System.out.println();
                    // четене на броя символи
            String contentLength = in.readLine();
            int length = Integer.MAX_VALUE;
            try {
                length = Integer.parseInt(contentLength.trim(), 16);
            } catch (NumberFormatException ex) {
                System.out.println("This server doesn't send the content-length");
                return;
            }
            System.out.println("will send "+contentLength+ " = "+length+"characters\n");
            int c;
            int i = 0;
            while ((c = in.read()) != -1 && i++ < length) {
                System.out.write(c);
            }
            System.out.println();
            socket.close();    
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
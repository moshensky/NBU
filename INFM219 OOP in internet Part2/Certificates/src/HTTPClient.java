import java.io.*;
import java.net.*;
public class HTTPClient {
    public static void main(String[] args) {
        int port = 80; // подразбиращ се http порт
        String host = "www.dir.bg";
        try {
            InetAddress addr = InetAddress.getByName(host);
            Socket socket = new Socket(addr, port);
            Writer out = new OutputStreamWriter(socket.getOutputStream());
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
                socket.close();
                return;
            }
            System.out.print("Content Length = ");
            System.out.println(contentLength+ " ("+length+" characters)");
            int c;
            int i = 0;            
            while ((c = in.read()) != -1 && i++ < length) {
                System.out.write(c);
            }
            System.out.println();
            socket.close();    
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
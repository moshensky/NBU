import java.util.Properties;
import java.security.KeyStore;
import java.io.*;
import javax.net.ssl.*;

public class SSLAuthClient {
	public static void main(String[] args) throws Exception {
		String message = "";
		
        Properties props = System.getProperties();       
        props.put("javax.net.ssl.trustStore","serts\\samplecacerts"); 
        props.put("javax.net.ssl.trustStorePassword","changeit");
        
		SSLSocketFactory factory = null;
		try {
			factory = getSocketFactory();
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		SSLSocket socket = (SSLSocket) factory.createSocket("127.0.0.1", 2001);
		socket.startHandshake();
		BufferedReader sin = new BufferedReader(
				new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		while (!message.equalsIgnoreCase("END")) {
			System.out.print("input a line (\"END\" to finish): ");
			message = sin.readLine();
			out.println(message);
			out.flush();
			if (out.checkError())
				System.out
						.println("SSLSocketClient:  java.io.PrintWriter error");
			String inputLine;
			if ((inputLine = in.readLine()) != null) {
				System.out.println("Server sends: " + inputLine);
			} else {
				System.out.println("No response from server");
			}
		}
		socket.close();
	}

	private static SSLSocketFactory getSocketFactory() {
		SSLSocketFactory factory = null;
		try {
			// set up key manager to do server authentication
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks;
			char[] passphrase = "passphrase".toCharArray();
			ctx = SSLContext.getInstance("TLS");
			kmf = KeyManagerFactory.getInstance("SunX509");
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("serts\\client\\testkeys"), passphrase);
			kmf.init(ks, passphrase);
			ctx.init(kmf.getKeyManagers(), null, null);
			factory = ctx.getSocketFactory();
			return factory;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
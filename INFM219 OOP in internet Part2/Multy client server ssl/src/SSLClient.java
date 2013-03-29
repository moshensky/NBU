import java.io.*;
import java.util.Properties;

import javax.net.ssl.*;

public class SSLClient {
	public static void main(String[] args) throws Exception {
		
        Properties props = System.getProperties();       
        props.put("javax.net.ssl.trustStore","serts\\samplecacerts"); 
        props.put("javax.net.ssl.trustStorePassword","changeit");
		
		String message = "";
		try {
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
					.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket("127.0.0.1",
					2001);

			socket.startHandshake();
			BufferedReader sin = new BufferedReader(new InputStreamReader(
					System.in));
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

				/* read response */
				String inputLine;
				if ((inputLine = in.readLine()) != null) {
					System.out.println("Server sends: " + inputLine);
				} else {
					System.out.println("No response from server");
				}
			}
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
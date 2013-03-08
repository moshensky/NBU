import java.net.*;
import java.io.*;

public class RecieveMulticastFile {
	private static MulticastSocket mcScocket;
	private static InetAddress ia;
	private static FileOutputStream out;
	private static String filePath = "C:\\Users\\Nikita\\Desktop\\tankCopy.png";
	
	public static void main(String arg[]) throws IOException {
		try {
			int port = 4000;
			mcScocket = new MulticastSocket(port);

			// Join group
			ia = InetAddress.getByName("224.2.234.3");
			mcScocket.joinGroup(ia);
			
			
			out = new FileOutputStream(filePath);
			

			byte[] buffer = new byte[2048];
			DatagramPacket dpRecieve = new DatagramPacket(buffer, buffer.length);
			System.out.println("started server");
			int length;
			
			//mcScocket.setSoTimeout(9000);
			
			while (true) {
				// receiving
				mcScocket.receive(dpRecieve);
				length = dpRecieve.getLength();
				out.write(dpRecieve.getData(), 0, length);
				System.out.println(length);
				if (length < 2048)
					break;
			}
			System.out.println("done!");
			
		} catch (IOException ex) {
			System.err.println(ex);
		}
		finally {
			// leave group
			mcScocket.leaveGroup(ia);
			mcScocket.close();
		}
	}
}
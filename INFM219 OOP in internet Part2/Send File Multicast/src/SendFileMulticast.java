import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class SendFileMulticast {
	
	private static FileInputStream in;
	private static String filePath = "C:\\Users\\Nikita\\Desktop\\tank.png";
	
	public static void main(String arg[]) {
		try {
			int port = 4000;
			MulticastSocket multiCastSocket = new MulticastSocket(port);

			// Join group
			InetAddress ia = InetAddress.getByName("224.2.234.3");
			multiCastSocket.joinGroup(ia);

			// Sending information
			int ttl = 2; // between 1 and 127
					

			// Send file
			try {
				int length;
				byte data[] = new byte[2048];
				in = new FileInputStream(filePath);
				
				DatagramPacket dpSend = new DatagramPacket(data, data.length, ia, port);
				System.out.println("time to live: " + multiCastSocket.getTimeToLive());
				multiCastSocket.setTimeToLive(ttl);
				System.out.println("new time to live: " + multiCastSocket.getTimeToLive());
				
				
				while ((length = in.read(data)) != -1) {
					// send packets
					dpSend.setData(data);
					System.out.println(length);
					dpSend.setLength(length);
					multiCastSocket.send(dpSend);
					
				}
				System.out.println("file sent");
			} catch (FileNotFoundException ex) {
				System.out.println("file not found");
			} finally {
				if (in != null)
					in.close();				
			}
			
			
			// leave group
			multiCastSocket.leaveGroup(ia);
			multiCastSocket.close();
		} catch (IOException ex) {
			System.err.println(ex);
		}
		finally {
		}
	}
}

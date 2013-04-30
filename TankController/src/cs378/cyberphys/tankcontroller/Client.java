package cs378.cyberphys.tankcontroller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.util.Log;

public class Client {
	private static final String SERVER_IP = "192.168.2.3"; //fill in rasberry pis hostname
	private static final int PI_PORT = 8000; //fill in with correct pi port	
	private Socket clientSock;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private static String logtag = "CLIENT";

	public Client() {
		Log.d(logtag, "In client constructor");
		try {
			clientSock = new Socket(SERVER_IP, PI_PORT);
			Log.d(logtag, "Socket created");
			out = new ObjectOutputStream(clientSock.getOutputStream());
			in = new ObjectInputStream(clientSock.getInputStream());
			Log.d(logtag, "Streams created");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(logtag, "EXCEPTION! Client could not be created");
		}
	}

	public Client(String piIP) {
		Log.d(logtag, "In String client constructor");
		try {
			clientSock = new Socket(piIP, PI_PORT);
			Log.d(logtag, "Socket created");
			out = new ObjectOutputStream(clientSock.getOutputStream());
			in = new ObjectInputStream(clientSock.getInputStream());
			Log.d(logtag, "Streams created");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(logtag, "EXCEPTION! Client could not be created");
		}
	}
	
	public void writeMessage (String msg) {
		try {
			out.writeChars(msg.trim() + "\n");
			out.flush();
			Log.d(logtag, "Client message written " + msg);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(logtag, "EXCEPTION! Message could not be written");
		}
	}

	public void closeConnection () {
		try {
			in.close();
			out.close();
			Log.d(logtag, "Streams closed");
			clientSock.close();
			Log.d(logtag, "Client closed");
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(logtag, "EXCEPTION! Client could not be closed");
		}
	}
}
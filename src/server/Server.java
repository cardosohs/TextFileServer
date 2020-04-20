package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import server.DealWithClient;;

public class Server {
	
	static int port;
	static String dirPath;
	
	
	public static void main(String[] args) {
		dirPath = args[0];
		port = Integer.parseInt(args[1]);
		try {
			new Server().startServing();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startServing() throws IOException {
		ServerSocket s = new ServerSocket(port);
		System.out.println("Lancou ServerSocket: " + s);
		try {
			while (true) {
				Socket socket = s.accept();
				System.out.println("Conexao aceite: " + socket);
				new DealWithClient(socket).start();
			}
		} finally {
			s.close();
		}
	}

}

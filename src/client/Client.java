package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import resources.Message;



public class Client{

	private static String serveraddrs;
	private static int serverport;
	RemoteDirectory remoteDir = new RemoteDirectory();

	static ObjectOutputStream out;
	static ObjectInputStream in;
	private Socket socket;
	
	boolean guiWasClosed = false;
	

	public static void main(String[] args) throws IOException {
		serveraddrs = args[0];
		serverport = Integer.parseInt(args[1]);
		new Client().runClient();
	}

	public void runClient() throws IOException {
		
		GUI clientGUI = new GUI(this);
		connectToServer();
		consultFilesInfo();
		clientGUI.init();
		
	}
	

	public void connectToServer() throws IOException {
		socket = new Socket(serveraddrs, serverport);

		System.out.println("Socket = " + socket);
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}
	
	
	public void disconnectFromServer() {
		try {
			out.writeObject(new Message("DISCONNECT"));
			socket.close();
			//this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia um objecto Message do tipo CONSULT_LOCALFILES_INFO e aguarda o input do servidor
	 * com o nome/tamanho de cada ficheiro presente na pasta partilhada. Após isso, preenche a
	 * lista de PCDFiles presente na RemoteDirectory
	 * @throws IOException
	 */
	public void consultFilesInfo() throws IOException {
		out.writeObject(new Message("CONSULT_LOCALFILES_INFO"));
		try {
			Message msgIn = (Message) in.readObject();
			remoteDir.convertToRemoteFiles(msgIn.getFiles());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Envia um objecto Message do tipo NEW_FILE_INFO com o nome do novo ficheiro
	 * recebido na GUI
	 * @param name String
	 */
	public void sendNewFileName(String filename) {
		try {
			out.writeObject (new Message("NEW_FILE_INFO", filename));
			System.out.println("NEW_FILE_INFO: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*como foi invocado o método RemoteFile.read(), este método deverá ficar bloqueado até resposta
	 * do servidor com confirmação de obtenção do cadeado de leitura
	 * O METODO AINDA NÃO PAGA DO LOCALFILE DIRECTORY: ainda é necessário mecanismo de obtenção do cadeado de consulta!!!
	 * */
	public void sendDeleteRequest(String filename) {
		try {
			out.writeObject (new Message("DELETE_FILE", filename));
			System.out.println("DELETE_FILE: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyReleaseReadLock(String filename) {
		try {
			out.writeObject (new Message("RELEASE_READ_LOCK", filename));
			System.out.println("RELEASE_READ_LOCK: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyReleaseWriteLock(String filename) {
		try {
			out.writeObject (new Message("RELEASE_WRITE_LOCK", filename));
			System.out.println("RELEASE_WRITE_LOCK: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyAquireWriteLock(String filename) {
		try {
			out.writeObject (new Message("AQUIRE_WRITE_LOCK", filename));
			System.out.println("AQUIRE_WRITE_LOCK: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

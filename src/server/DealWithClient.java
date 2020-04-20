package server;


import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

import resources.Message;
import resources.PCDFile;

public class DealWithClient extends Thread {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private LocalDirectory localDir = LocalDirectory.getInstance();
	

	public DealWithClient(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void doConnections(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}
	
	/*função responsável por limpar todos os recursos utilizados pelo cliente
	 * assim que o mesmo termina a sua ligação com servidor
	 */
	private void cleanClientResources() {
		
	}

	
	/**
	 * A função DealWithClient.serve() permanece a espera até que o cliente envie MessageType.DISCONNECT"
	 * 		- DISCONNECT: usada pelo Client para terminar a ligação com o servidor
	 * 		- CONSULT_LOCALFILES_INFO: para obter nome de cada LocalFile no LocalDirectory
	 * 		- NEW_FILE_INFO: para receber o nome de um ficheiro a ser criado
	 * 		- DELETE_FILE: para receber o nome de um ficheiro a ser eliminado
	 * 		- READ_FILE: pedido de leitura de um ficheiro;
	 * 		- WRITE_FILE: pedido de escrita em um ficheiro;
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void serve() {
		boolean serving = true;
		while (serving) {
			try {
				Message msgIn = (Message)in.readObject();
				switch (msgIn.getMessageType()) {
				
					case "CONSULT_LOCALFILES_INFO": 
						localFilesInfo(); break;
						
					case "NEW_FILE_INFO": 
						newFileInfo(msgIn.getFileName()); break;
						
					case "DELETE_FILE": 
						deleteRequest(msgIn.getFileName()); break;
						
					case "READ_FILE":
						readRequest(msgIn.getFileName()); break;
						
					case "WRITE_FILE": {
						informReleaseWriteLock(msgIn.getFileName());
						writeRequest(msgIn.getFileName(), msgIn.getData());
						break;
					}
						
					case "DISCONNECT": {
						cleanClientResources();
						serving = false;
						break;
					}

					case "RELEASE_READ_LOCK": 
						informReleaseReadLock(msgIn.getFileName()); break;
						
					case "AQUIRE_WRITE_LOCK":
						informAquireWriteLock(msgIn.getFileName()); break;
				}
				System.out.println("MsgType: " + msgIn.getMessageType());
			}catch(IOException | ClassNotFoundException e) {
				System.out.println("houve um erro na leitura de objectos em " + this.getClass());
				return;
			}		
		}
	}
	
	private void localFilesInfo() {
		try {
			localDir.updateLocalFilesList();
			out.writeObject(new Message("RETURN_LOCALFILES_INFO", localDir.allLocalFiles));
		} catch (IOException e) {
			System.out.println("Erro no envio da lista de ficheiros");
		}
	}
	
	private void newFileInfo(String filename) {
		try {
			PCDFile file = localDir.newFile(filename);
			localDir.allLocalFiles.add(file);
		}catch (IOException e) {
			System.out.println("Erro na recepção do nome do novo ficheiro");
		}
	}
	
	private void deleteRequest(String filename) {
		try {
			localDir.delete(filename);
		} catch (IOException e) {
			System.out.println("Erro: DELETE_FILE");
		}
	}
	
	
	private void readRequest(String filename) {
		try {
			if (localDir.fileExists(filename)) {
				String content = localDir.getFile(filename).read();
				if(!content.equals(null))
					out.writeObject(new Message("RETURN_FILE", filename, content));
				else
					System.out.println("Erro: READ_FILE");
			}
		} catch (IOException e) {
			System.out.println("Erro: READ_FILE");
		}
	}
	
	private void writeRequest(String filename, String data) {
		System.out.println("WRITEREQUEST");
		try {
			if (localDir.fileExists(filename)) {
				localDir.getFile(filename).write(data);
			}
		} catch (IOException e) {
			System.out.println("Erro: WRITE_FILE");
		}
	}
	
	private void informReleaseReadLock (String filename) {
		try {
			if (localDir.fileExists(filename)) {
				localDir.getFile(filename).readUnlock();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void informReleaseWriteLock (String filename) {
		System.out.println("FOI FEITO O UNLOCK");
		try {
			if (localDir.fileExists(filename)) {
				localDir.getFile(filename).writeUnlock();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void informAquireWriteLock (String filename) {
		try {
			if (localDir.fileExists(filename)) {
				((LocalFile)localDir.getFile(filename)).incrementWriteRequests();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}

package client;

import java.io.IOException;

import resources.Message;

public class RemoteFile {

	
	private String fileName;
	private int fileLength;
	
	
	public RemoteFile(String fileName, int fileLength) {
		this.fileName = fileName;
		this.fileLength = fileLength;
	}

	
	/**
	 * O m�todo read envia uma mensagem ao servidor com a identifica��o do ficheiro (nome).
	 * O m�todo bloqueia at� resposta do servidor, com o conte�do do ficheiro em quest�o.
	 * @return String ficheiro lido
	 */
	public String read() {
		String result = "";
		try {
			Client.out.writeObject(new Message("READ_FILE", fileName));
			System.out.println("READ_FILE: " + fileName);
			Message msgIn = (Message)Client.in.readObject();
			//String msgType = msgIn.getMessageType();
//				while (!msgType.equals("RETURN_FILE")) {
//					System.out.println("Remote File: estou em wait");
//					wait();
//				}
			result = msgIn.getData();
		
		} catch (IOException | ClassNotFoundException /*| InterruptedException*/ e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * O m�todo write envia uma mensagem ao servidor com nome do ficheiro (String)
	 * mais o conteudo a ser escrito (String)
	 */
	public void write(String content) {
		try {
			Client.out.writeObject(new Message("WRITE_FILE", fileName, content));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("WRITE_FILE: " + fileName + " <-- " + content);
	}
	
	
	public int getLength(){
		return fileLength;
	}

	
	public String getName() {
		return fileName;
	}

}

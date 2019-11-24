package resources;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
	
	/**
	 * Classe responsável por transportar informação entre cliente e servidor.
	 * Contém um enumerado privado que indica o tipo de mensagem que está a ser enviada.
	 * Tem como atributos as estruturas de dados utilizadas por essas mensagens.
	 */
	private static final long serialVersionUID = 1L;

	private enum MessageType{
		DISCONNECT, //mensagem contém apenas um enumerado que indica que o cliente vai disconectar-se
		CONSULT_LOCALFILES_INFO, //mensagem contém apenas um enumerado.
		RETURN_LOCALFILES_INFO, //mensagem contém uma lista de PCDFiles presentes no LocalDirectory
		RETURN_FILE, //mensagem contém o nome do ficheiro e o conteúdo do mesmo.
		RELEASE_READ_LOCK, //mensagem contém apenas um enumerado que indica que liberta o cadeado de leitura
		RELEASE_WRITE_LOCK, //mensagem contém apenas um enumerado que indica que liberta o cadeado de escrita
		AQUIRE_WRITE_LOCK,
		
		NEW_FILE_INFO, //mensagem contém apenas o nome (String) do ficheiro a ser criado
		DELETE_FILE, //mensagem contém apenas o nome (String) do ficheiro a ser apagado
		READ_FILE, //mensagem contém apenas o nome do ficheiro (String) a ser lido.
		WRITE_FILE; //mensagem contém o nome do ficheiro (String) e os dados a escrever (String)
	}
	
	private MessageType msgType;
	
	private String fileName;
	private String data;
	private List<PCDFile> files;

	public Message (String msgType) {
		this.msgType = MessageType.valueOf(msgType);
	}
	
	public Message(String msgType, String fileName){
		this.msgType = MessageType.valueOf(msgType);
		this.fileName = fileName;
	}
	
	public Message(String msgType, List<PCDFile> files){
		this.msgType = MessageType.valueOf(msgType);
		this.files = files;
	}
	
	public Message(String msgType, String fileName, String data){
		this.msgType = MessageType.valueOf(msgType);
		this.fileName = fileName;
		this.data = data;
	}



	public String getMessageType(){
		return msgType.toString();
	}
	
	public String getData(){
		return data;
	}
	
	public List<PCDFile> getFiles(){
		return files;
	}
	
	public String getFileName(){
		return fileName;
	}

}
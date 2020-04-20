package resources;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystemException;

public interface PCDDirectory extends Serializable {
	/**
	 * Método que verifica se existe um ficheiro ou uma sub-pasta nesta pasta com o
	 * nome name.
	 *
	 * @param name nome da entrada a procurar
	 * @return true caso exista um ficheiro ou pasta com o nome name false caso contrário
	 * @throws IOException
	 */
	public abstract boolean fileExists(String name) throws IOException;
	public abstract PCDFile newFile(String name) throws FileSystemException, IOException;
	public abstract void delete(String name) throws FileSystemException, IOException;
	/**
	 * Método que devolve um array com o nome de todas as pastas e ficheiros existentes
	 * nesta pasta.
	 *
	 * @return um array com todos os nomes
	 * @throws FileSystemException caso a entrada não exista.
	 * @throws IOException
	 */
	public abstract String[] getDirectoryListing() throws FileSystemException, IOException;
	/**
	 * Método que devolve uma entrada que representa o ficheiro (PCDFile)
	 * existente nesta pasta com o nome name.
	 *
	 * @param name nome do ficheiro
	 * @return a entrada que representa o ficheiro
	 * @throws IOException
	 * @throws FileSystemException
	 */
	public abstract PCDFile getFile(String name) throws FileSystemException, IOException;
}

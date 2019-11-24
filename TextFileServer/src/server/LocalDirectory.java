package server;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileFilter;

import resources.PCDDirectory;
import resources.PCDFile;

/**
 * Classe que representa o directório físico onde os ficheiros se encontram.
 * Utiliza SINGLETON como padrão de desenho e, por isso,
 * teve-se o cuidado de limitar o seu acesso (package private)
 * @author
 *
 */

public class LocalDirectory implements PCDDirectory{
	
	
	private static final long serialVersionUID = 1L;
	List<PCDFile> allLocalFiles = new ArrayList<PCDFile>();
	String path;
	private static LocalDirectory INSTANCE = null;
	
	private LocalDirectory() {
		path = Server.dirPath;
		updateLocalFilesList();
	}
	
	public static LocalDirectory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocalDirectory();
		}
		return INSTANCE;
	}
	
	/**
	 * Lê os ficheiros .txt presentes no LocalDirectory e os introduz em uma lista de LocalFiles
	 */
	public void updateLocalFilesList() {
		File [] tempFiles = new File(path).listFiles(new FileFilter() {
			public boolean accept (File f) {
				return f.isFile() && f.getName().endsWith(".txt");
			}
		});
		allLocalFiles.clear();
		for (int i=0; i<tempFiles.length; i++) 
			allLocalFiles.add(new LocalFile (tempFiles[i]));
	}
	
	
	@Override
	public boolean fileExists(String name) throws IOException {
		for (PCDFile lf : allLocalFiles)
			if (lf.getName().equals(name))
				return true;
					return false;
	}

	@Override
	public PCDFile newFile(String name) throws FileSystemException, IOException {
		if (!fileExists(name)) {
			File fileToAdd = new File (path + "/" + name + ".txt");
			fileToAdd.createNewFile();
			System.out.println("File " + name + " created!");
			return new LocalFile(fileToAdd);
		} else
			System.out.println("File " + name + " ja existe!");
		return null;
	}
	

	@Override
	public void delete(String name) throws FileSystemException, IOException {
		if (fileExists(name)) {
			File fileToRemove = new File (path + "/" + name);
			PCDFile toRemove = getFile(name);
	        fileToRemove.delete();
	        allLocalFiles.remove(toRemove);
			System.out.println("File " + name + " deleted!");
		}else 
			System.out.println("File " + name + " nao existe!");
	}

	@Override
	public String [] getDirectoryListing() throws FileSystemException, IOException {
		String [] result = new String [allLocalFiles.size()];
		for(int i=0; i<allLocalFiles.size(); i++)
			result[i] = allLocalFiles.get(i).getName();
		return result;
	}
	

	@Override
	public PCDFile getFile(String name) throws FileSystemException, IOException {
		PCDFile result = null;
			for (PCDFile lf : allLocalFiles) {
				if (lf.getName().equals(name)) {
					result = lf;
					break;
				}
			}
		return result;
	}

}

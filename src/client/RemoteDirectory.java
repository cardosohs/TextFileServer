package client;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

import resources.PCDFile;
/**
 * Classe que representa o directório abstrato onde os ficheiros se encontram.
 * CADA CLIENTE tem o seu próprio RemoteDirectory que deve ser actualizado de acordo com
 * o LocalDirectory. Por esse motivo, não se justifica incluir métodos CRUD.
 * @author cardosohs
 *
 */

public class RemoteDirectory{
	

	private List<RemoteFile> allRemoteFiles = new ArrayList<RemoteFile>();
	

	public String [] getDirectoryListing() {
		String [] result = new String [allRemoteFiles.size()];
		for (int i=0; i<result.length; i++) 
			result[i] = allRemoteFiles.get(i).getName();
		return result;
	}
	
	public int length (String fileName) {
		int result = 0;
		for (RemoteFile rf : allRemoteFiles)
			if (rf.getName().equals(fileName))
					result = rf.getLength();
		return result;
	}
	
	public boolean fileExists(String name) throws IOException {
		for (RemoteFile lf : allRemoteFiles)
			if (lf.getName().equals(name))
				return true;
					return false;
	}
	
	public RemoteFile getFile(String name) throws FileSystemException, IOException {
		RemoteFile result = null;
		if (fileExists(name)) {
			for (RemoteFile lf : allRemoteFiles) {
				if (lf.getName().equals(name)) {
					result = lf;
					break;
				}
			}
		}
		return result;
	}
	
	public List<RemoteFile> getAllRemoteFiles() {
		return allRemoteFiles;
	}
	
	
	public void convertToRemoteFiles (List<PCDFile> files) {
		allRemoteFiles.clear();
		for (PCDFile f : files) {
			try {
				allRemoteFiles.add(new RemoteFile(f.getName(), f.length()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
}

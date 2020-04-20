package server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystemException;

import resources.PCDFile;

public class LocalFile implements PCDFile{
	

	private static final long serialVersionUID = 1L;
	private static final String path = "C:/Users/cardo/git/PCD-2019/TextFileServer/LocalDirectory/";
	private File localFile;
	public static final int MAX_READERS = 2;
	private int readers = 0;
	private int writers = 0;
	private int writeRequests = 0;
	
	
	public LocalFile (File localFile) {
		this.localFile = localFile;
	}
	

	@Override
	public synchronized boolean readLock() throws IOException{
		if(exists()) {
			while(readers >= MAX_READERS || writeRequests > 0){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		readers++;
		
		return true;

		} else
			return false;
	}
	
	
	@Override
	public synchronized boolean writeLock() throws IOException {
		System.out.println("writers apos metodo:"+writers);
		System.out.println("writersReq apos metodo:"+writeRequests);

		if(exists()) {
			while(readers > 0 || writers > 0){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		writers++;
		System.out.println(this);
		System.out.println("writers no fim:"+writers);
		System.out.println("writersReq no fim:"+writeRequests);
		return true;
		
		} else
			return false;
	}
	
	
	@Override
	public synchronized void readUnlock() throws IOException {
		readers--;
		notifyAll();
	}

	@Override
	public synchronized void writeUnlock() throws IOException {
		writeRequests--;
		notifyAll();
	}
	

	@Override
	public String read() throws FileSystemException, IOException {
		String result = null;
		System.out.println("writers apos metodoREAD:"+writers);
		System.out.println("writersReq apos metodoREAD:"+writeRequests);
		if(readLock()) {
			result = new String(read(0, length()));
		}else
			throw new IOException ("O ficheiro foi apagado e não pode ser acedido!");
		return result;
	}

	private byte[] read(int offset, int length) throws FileSystemException {
		ByteBuffer buffer = null;
		RandomAccessFile aFile = null;
		try {
			aFile = new RandomAccessFile(path + getName(), "r");
			FileChannel channel = aFile.getChannel();
			if (offset > aFile.length())
				return null;
			if (offset + length > aFile.length()) {
				length = (int) aFile.length() - offset;
			}
			channel.position(offset);
			buffer = ByteBuffer.allocate(length);
			channel.read(buffer);
		} catch (IOException e) {
			throw new FileSystemException(e.getMessage());
		} finally {
			if (aFile != null)
				try {
					aFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return buffer.array();
	}
		

	@Override
	public void write(String dataToWrite) throws FileSystemException, IOException {
		if(writeLock()) {
				write(dataToWrite.getBytes(), 0);
		}else
			throw new IOException ("O ficheiro foi apagado e não pode ser acedido!");
	}
	
	private void write(byte[] dataToWrite, int offset) throws FileSystemException {
		RandomAccessFile aFile = null;
		try {
			aFile = new RandomAccessFile(path + getName(), "rw");
			FileChannel channel = aFile.getChannel();
			if (offset > aFile.length())
				offset = (int) aFile.length();
			ByteBuffer buf = ByteBuffer.wrap(dataToWrite);
			channel.position(offset);
			while (buf.hasRemaining()) {
				channel.write(buf);
			}
		} catch (IOException e) {
			throw new FileSystemException(e.getMessage());
		} finally {
			if (aFile != null)
				try {
					aFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	
	@Override
	public String getName() {
		return localFile.getName();
	}
	
	public void incrementWriteRequests() {
		writeRequests++;
	}

	
	@Override
	public int length() throws FileSystemException, IOException {
		return (int)localFile.length();
	}
	
	@Override
	public boolean exists() throws IOException {
		return localFile.exists();
	}
}

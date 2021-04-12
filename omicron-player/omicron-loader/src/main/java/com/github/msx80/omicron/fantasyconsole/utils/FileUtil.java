package com.github.msx80.omicron.fantasyconsole.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class FileUtil {
	
	
	public static  Properties loadProps(byte[] prop) throws IOException {
		Properties p = new Properties();
		p.load(new ByteArrayInputStream(prop));
		return p;
	}
	
	public static void copy(InputStream in, OutputStream out)
			   throws IOException
			{
			   // Read bytes and write to destination until eof

			   byte[] buf = new byte[1024*4];
			   int len = 0;
			   while ((len = in.read(buf)) >= 0)
			   {
			      out.write(buf, 0, len);
			   }
			}

	
	/**
	 * 
	 * @param fileToRead
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileToBytes(File fileToRead) {
		try {
			return readData(new FileInputStream(fileToRead));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	   public static byte[] readData(InputStream inputStream) {
	        try {
				return readDataNice(inputStream);
			} finally {
	        	close(inputStream);
			}
	    }

	    public static byte[] readDataNice(InputStream inputStream) {
			ByteArrayOutputStream boTemp = null;
	        byte[] buffer = null;
	        try {
	            int read;
				buffer = new byte[8192];
	            boTemp = new ByteArrayOutputStream();
	            while ((read=inputStream.read(buffer, 0, 8192)) > -1) {
	                boTemp.write(buffer, 0, read);
	            }
	            return boTemp.toByteArray();
	        } catch (IOException e) {
				throw new RuntimeException(e);
	        }
		}
		
		
	    /**
	     * Close streams (in or out)
	     * @param stream
	     */
	    public static void close(Closeable stream) {
	        if (stream != null) {
	            try {
	                if (stream instanceof Flushable) {
	                    ((Flushable)stream).flush();
	                }
	                stream.close();
	            } catch (IOException e) {
	                // When the stream is closed or interupted, can ignore this exception
	            }
	        }
	        
	        
	        
	        
	        
	    }
	
	    
	    
/*
    public static FileInputStream fileInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static FileInputStream fileInputStream(String file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

	
    public static void eachFile(File path, P2<File, String> f) {
        eachFile(path, f, null);
    }
	
    public static void eachFile(File path, P2<File, String> f, F1<File, Boolean> exclude) {
    	eachFile(path, Fs.f2(f, true), exclude);
    }
	
    public static void eachFile(File path, F2<File, String,Boolean> f, F1<File, Boolean> exclude) {

        ArrayList<String> relPath = new ArrayList<>();

        if (path.isFile()) {
            f.e(path, Cols.join(relPath, File.separator));
        } else {
            if (!eachFileInDir(path, f, relPath, exclude)) return;
        }
    }
	
    private static boolean eachFileInDir(File path, F2<File, String,Boolean> f, ArrayList<String> relPath, F1<File, Boolean> exclude) {
        if (!path.exists() || !path.isDirectory()) {
            throw new RuntimeException("Invalid path: " + path);
        }
        for (File child : path.listFiles()) {
            if (exclude != null && exclude.e(child)) {
//            	System.out.println("Excluded " + child);
                continue;
            }
//        	System.out.println("Accepted " + child);

            if (child.isFile()) {
                if (!f.e(child, Cols.join(relPath, File.separator))) return false;
            } else {
                relPath.add(child.getName());
                if (!eachFileInDir(child, f, relPath, exclude)) return false;
                relPath.remove(relPath.size() - 1);
            }
        }
        return true;
    }
    */
}

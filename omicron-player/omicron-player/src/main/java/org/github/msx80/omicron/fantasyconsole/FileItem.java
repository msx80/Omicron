package org.github.msx80.omicron.fantasyconsole;

import java.nio.file.Path;

public class FileItem implements Comparable<FileItem> {
	public Path path;
	public boolean isDirectory;
	public String name;
	
	public FileItem(Path path, boolean isDirectory, String name) {
		this.path = path;
		this.isDirectory = isDirectory;
		this.name = name;
	}

	@Override
	public int compareTo(FileItem o) {
		int n = Boolean.compare(o.isDirectory, this.isDirectory);
		if(n==0) n = String.CASE_INSENSITIVE_ORDER.compare(this.name, o.name);
		return n;
	}



	
	
}

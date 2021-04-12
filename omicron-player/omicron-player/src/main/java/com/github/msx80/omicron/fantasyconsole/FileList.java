package com.github.msx80.omicron.fantasyconsole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileList {
	public Path path;
	public List<FileItem> files;	
	
	public FileList(Path path) throws IOException
	{
		this.path = path;
		this.files = new ArrayList<>();
				
		files.add(new FileItem(path.toAbsolutePath().getParent(), true, ".."));
		
		Files.list(path).forEach( p-> {
			if(Files.isDirectory(p))
			{
				files.add(new FileItem(p, true, p.getFileName().toString()));
			}
			else if (p.getFileName().toString().endsWith(".omicron"))
			{
				files.add(new FileItem(p, false, p.getFileName().toString()));
			}
		});
				
		Collections.sort(files);
		
	}
}

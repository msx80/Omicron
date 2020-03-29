package org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader;

import static org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader.BytesLoader.findFile;

import java.io.File;

import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class DirectoryBytesLoader implements BytesLoader {


		private File dir;
	
		public DirectoryBytesLoader(File dir) {
			this.dir = dir;
		}

		@Override
		public byte[] loadFile(String filePath) {
			File file = findFile(filePath, dir);
			if (file == null) {
				return null;
			}

			return FileUtil.readFileToBytes(file);
		}
		
		@Override
		public void close() {
			
			
		}

}

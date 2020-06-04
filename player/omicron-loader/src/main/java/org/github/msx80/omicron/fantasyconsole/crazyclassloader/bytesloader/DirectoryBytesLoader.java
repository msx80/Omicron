package org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader;

import java.io.File;

import org.github.msx80.omicron.fantasyconsole.cartridges.BytesLoader2;
import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class DirectoryBytesLoader implements BytesLoader2 {


		private File dir;
	
		public DirectoryBytesLoader(File dir) {
			this.dir = dir;
		}

		static File findFile(String filePath, File classPath) {
			File file = new File(classPath, filePath);
			return file.exists() ? file : null;
		}

		
		@Override
		public byte[] loadFile(String filePath) {
			File file = findFile(filePath, dir);
			if (file == null) {
				return null;
			}

			return FileUtil.readFileToBytes(file);
		}
		

}

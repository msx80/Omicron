package com.github.msx80.omicron.plugins.builtin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class FileOpPlugin implements HardwarePlugin {

	@Override
	public void init(HardwareInterface hw) {
		
	}

	@Override
	public Object exec(String command, Object params) throws Exception {
		if ("FILEWRITE".equals(command)) {
			String filename = (String) params;
			OutputStream os = new FileOutputStream(filename);
			return os;
		}
		else if ("FILEREAD".equals(command)) {
			String filename = (String) params;
			InputStream os = new FileInputStream(filename);
			return os;
		}
		else if("CHOOSEOPEN".equals(command))
		{
			JFileChooser chooser = new JFileChooser();
			if(params!=null) chooser.setDialogTitle((String) params);
	        //FileNameExtensionFilter filter = new FileNameExtensionFilter("All files", "*.*");
	        //chooser.setFileFilter(filter);
	        int returnVal = chooser.showOpenDialog(null);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
	            return chooser.getSelectedFile().getAbsolutePath();
	        }
	        return null;
		}
		else if("CHOOSESAVE".equals(command))
		{
			JFileChooser chooser = new JFileChooser();
			if(params!=null) chooser.setDialogTitle((String) params);
	        //FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
	        //chooser.setFileFilter(filter);
	        int returnVal = chooser.showSaveDialog(null);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	            System.out.println("You chose to save this file: " + chooser.getSelectedFile().getAbsolutePath());
	            return chooser.getSelectedFile().getAbsolutePath();
	        }
	        return null;
		}		
		throw new RuntimeException("Unknown command");
		
	}

}

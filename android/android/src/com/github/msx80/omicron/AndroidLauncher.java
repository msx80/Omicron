package com.github.msx80.omicron;

import android.os.Bundle;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;
import android.app.Activity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.*;
import com.github.msx80.omicron.fantasyconsole.cartridges.*;

import android.content.pm.*;

public class AndroidLauncher extends AndroidApplication implements HardwareInterface {
	
	private static final int SAVE_FILE = 3345993;
	
	private byte[] bytesToSave = null;
	private Consumer<String> fileResult = null;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExceptionHandler.addHandler(this);
		Cartridge c = null;
		try {
		//    ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		    //Bundle bundle = ai.metaData;
		    //String gameClass = bundle.getString("gameClass");
			
			
			String gameClass = BuildConfig.gameClass;
			int n = gameClass.lastIndexOf('.');
			String pkg = gameClass.substring(0,  n);
			String main = gameClass.substring(n+1);
		
			 c = new ClasspathCartridge("Gamex", pkg, main);
		    //g = (Game)Class.forName(gameClass).newInstance();
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GdxOmicron(c, this), config);
	}
	
	@Override
	public void openUrl(String url) throws Exception {
      Uri uri = Uri.parse( url );
      Intent intent = new Intent( Intent.ACTION_VIEW, uri );
      this.startActivity( intent );
   }
   
   	@Override
	public void saveFile(String mimeType, String filename, byte[] content, Consumer<String> result) {
		this.bytesToSave = content;
		this.fileResult = result;
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType(mimeType); //not needed, but maybe usefull
		intent.putExtra(Intent.EXTRA_TITLE, filename); //not needed, but maybe usefull
		startActivityForResult(intent, SAVE_FILE);
   }
   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if(requestCode == SAVE_FILE ) {
		  // clean stuff
		  byte[] bytes = bytesToSave;
		  bytesToSave = null;
		  Consumer<String> onResult = fileResult;
		  fileResult = null;
		  
		if (resultCode == Activity.RESULT_OK) {
		Uri uri = data.getData();
		Toast.makeText(this, "Info: "+uri, Toast.LENGTH_SHORT).show();
		//just as an example, I am writing a String to the Uri I received from the user:
		String error = null;
		try {
		  OutputStream output = getContext().getContentResolver().openOutputStream(uri);

		  output.write(bytes);
		  output.flush();
		  output.close();
		}
		catch(Exception e) {
		  Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		  error = e.getMessage();
		}
		onResult.accept(error);
		
	  }
	  else
	  {
		  Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
		  onResult.accept("dismissed");
	  }
	  }
}
   
   
   @Override public String[] startupArgs()
   {
	   return new String[0];
   }
}

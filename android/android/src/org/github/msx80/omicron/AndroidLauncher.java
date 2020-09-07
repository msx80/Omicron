package org.github.msx80.omicron;

import android.os.Bundle;
import android.net.Uri;
import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.github.msx80.omicron.GdxOmicron;
import org.github.msx80.omicron.HardwareInterface;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.adv.*;
import org.github.msx80.omicron.fantasyconsole.cartridges.*;

import android.content.pm.*;

public class AndroidLauncher extends AndroidApplication implements HardwareInterface {
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
   @Override public String[] startupArgs()
   {
	   return new String[0];
   }
}

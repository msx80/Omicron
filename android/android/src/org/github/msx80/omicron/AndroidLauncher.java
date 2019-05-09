package org.github.msx80.omicron;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.github.msx80.omicron.GdxOmicron;
import org.github.msx80.omicron.api.Game;

import android.content.pm.*;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Game g = null;
		try {
		//    ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		    //Bundle bundle = ai.metaData;
		    //String gameClass = bundle.getString("gameClass");
			
			String gameClass = BuildConfig.gameClass;
			
		    g = (Game)Class.forName(gameClass).newInstance();
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GdxOmicron(g), config);
	}
}

package omicron.teavm;
import java.util.Arrays;

import com.github.msx80.omicron.DefaultHardwareInterface;
import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.ClasspathCartridge;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;


public class TeaLauncher {

    public static void main(String[] args) {
        try {
        	System.out.println("Starting Application");
    		
        	String classname = args[0];
        	System.out.println("Main class: "+classname);
        	
        	
        	WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
			config.width = 0;
			config.height = 0;
			config.showDownloadLogs = false;
			config.useGL30 = false;

			String b = classname.substring(classname.lastIndexOf(".")+1);
			System.out.println(b);
			String d = classname.substring(0, classname.lastIndexOf("."));
			System.out.println(d);
			
			Cartridge c = new ClasspathCartridge("Omicron", d, b);
			
			HardwareInterface hi = new DefaultHardwareInterface(new String[0]) {

				@SuppressWarnings("unchecked")
				@Override
				public Class<? extends HardwarePlugin> loadPluginClass(String module) throws Exception {
					// teavm doesn't support loading of arbitrary classes
					return (Class<? extends HardwarePlugin>) Class.forName(module);
				}
								
			};
			new WebApplication(new GdxOmicron( c, hi) , config);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
    }
}
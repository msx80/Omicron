package omicron.teavm;
import com.github.msx80.omicron.DefaultHardwareInterface;
import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.ClasspathCartridge;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;

public class TeaLauncher {

    public static void main(String[] args) {
        try {
			TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
			config.width = 0;
			config.height = 0;
			config.showDownloadLogs = false;
			config.useGL30 = false;

			/*config.preloadListener = assetLoader -> {
			    assetLoader.loadScript("freetype.js");
			};*/

			
			String classname = Class.forName("emitter.Emitter").newInstance().toString();
			System.out.println("Loading game class2: "+classname);
						
			// there's something very weird going on with teavm and reflection.. looks like some classes
			// are included or excluded based on some Class.forName mention.
			
			//Cartridge c = new ClassCartridge(gameClass.getName(), gameClass); //
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
			new TeaApplication(new GdxOmicron( c, hi) , config);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
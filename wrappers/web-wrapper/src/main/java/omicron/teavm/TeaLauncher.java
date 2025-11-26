package omicron.teavm;
import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.HardwareInterface;
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
			
			Cartridge c = new ClasspathCartridge("Prova", d, b);
			
			HardwareInterface hi = new HardwareInterface() {
				
				@Override
				public String[] startupArgs() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Object hardware(String module, String command, Object param) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void gameRestored() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void gamePaused() {
					// TODO Auto-generated method stub
					
				}
			};
			new TeaApplication(new GdxOmicron( c, hi) , config);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
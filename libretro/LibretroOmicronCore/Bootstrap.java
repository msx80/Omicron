import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map.Entry; 

/**
 * Bootstrap class used by libretro frontend to load the main omicron.jar
 * args are: 
 * - class to load
 * - full path of jar to load
 * This all could be done in C probably but i hacked this up first.
 *
 *
 * NOTE THIS CLASS IS HARDCODED IN bootstrap.h
 */
public class Bootstrap {

	public static Object m;

	public static void main(String[] args)  {
		try
		{
			System.out.println("Bootstrap class started. Loading jar:\n"+ args[1] +"\nClass:\n"+args[0]);
			// trace some stuff
			System.out.println("Current properties:");
			for(Entry e : System.getProperties().entrySet())
			{
				System.out.println(e.getKey()+"\t"+e.getValue());
			}
			
			
			if(args.length!=2) throw new RuntimeException("Args must be: className Jarfile");
			File jarFile = new File(args[1]);
			String className = args[0];
			URL fileURL = jarFile.toURI().toURL();
			String jarURL = "jar:" + fileURL + "!/";
			URL urls [] = { new URL(jarURL) };
			URLClassLoader ucl = new URLClassLoader(urls);
			m = Class.forName(className, true,   ucl).newInstance();
		}
			catch (Exception e) {
				e.printStackTrace();
		}
	}

	public static Class getMainClass()
	{
		return m.getClass();
	}
	
}

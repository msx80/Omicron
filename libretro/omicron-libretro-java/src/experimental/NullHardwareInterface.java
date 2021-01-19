package experimental;

import java.util.function.Consumer;

import org.github.msx80.omicron.HardwareInterface;

public class NullHardwareInterface implements HardwareInterface {

	@Override
	public void openUrl(String url) throws Exception {
		

	}

	@Override
	public String[] startupArgs() {
		return new String[0];
	}

	@Override
	public void saveFile(String arg0, String arg1, byte[] arg2, Consumer<String> arg3) {
		
		
	}

}

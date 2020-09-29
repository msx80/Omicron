package experimental;

import org.github.msx80.omicron.HardwareInterface;

public class NullHardwareInterface implements HardwareInterface {

	@Override
	public void openUrl(String url) throws Exception {
		

	}

	@Override
	public String[] startupArgs() {
		return new String[0];
	}

}

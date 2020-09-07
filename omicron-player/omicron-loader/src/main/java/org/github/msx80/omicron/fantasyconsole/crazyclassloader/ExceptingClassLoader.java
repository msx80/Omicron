package org.github.msx80.omicron.fantasyconsole.crazyclassloader;

import java.util.function.Predicate;

import org.github.msx80.omicron.fantasyconsole.cartridges.BytesLoader2;

/**
 * This class loader will not load certain classes, instead delegate to parent
 * class loader to do the job
 */
public class ExceptingClassLoader extends DynamicClassLoader {

	private Predicate<String> except;

	public ExceptingClassLoader(Predicate<String> except, BytesLoader2... l) {
		super(l);
		this.except = except;
	}

	@Override
	protected byte[] loadNewClass(String name) throws Exception {
		if (except.test(name)) {
            return null;
		}

		return super.loadNewClass(name);
	}
	

}

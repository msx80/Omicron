package org.github.msx80.omicron.api;

public interface Acceptor<E> {
	void accept(E e) throws Exception;
}

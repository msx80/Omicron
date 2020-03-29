package org.github.msx80;

import java.io.Serializable;

public class Pair<A, B> implements Serializable, Comparable<Pair<A, B>> {
	
	private static final long serialVersionUID = 8502630524181161902L;
	
	public final A a;
	public final B b;
	
	public Pair(A a, B b) {
		super();
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return a + " - " + b;
	}

	@Override
	public int compareTo(Pair<A, B> o) {
		if(a instanceof Comparable)
		{
			int n = ((Comparable) a).compareTo(o.a);
			if(n!=0) return n;
		}
		else if (b instanceof Comparable)
		{
			int n = ((Comparable) b).compareTo(o.b);
			if(n!=0) return n;
		}
		return 0;
			
	}

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<>(a, b);
	}
	
	
	
}

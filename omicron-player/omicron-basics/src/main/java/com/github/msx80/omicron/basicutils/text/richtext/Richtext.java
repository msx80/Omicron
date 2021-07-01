package com.github.msx80.omicron.basicutils.text.richtext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.msx80.omicron.basicutils.Colors;

/**
 * Represent a rich text, with colored strings, icons and/or custom elements.
 * It's basically an array of RichtextItem
 */
public class Richtext implements Iterable<RichtextItem> {

	
	private RichtextItem[] tokens;
	public Object userdata;
	
	public Richtext(RichtextItem... tokens) {
		this.tokens = tokens;
	}
	
	private static RichtextItem[] arrToTokens(Object[] tt) {
		RichtextItem[] res = new RichtextItem[tt.length/2];
		
		for (int i = 0; i < tt.length; i+=2) {
			ColoredString cs = new ColoredString((Integer)tt[i], (String)tt[i+1]);
			res[i/2] = cs;
		}
		return res;
	}

	public static Richtext of(Object... colorsAndStrings)
	{
		return new Richtext( arrToTokens(colorsAndStrings));
	}	
	public static Richtext with(Object userdata, Object... colorsAndStrings)
	{
		Richtext r= new Richtext(arrToTokens(colorsAndStrings));
		r.userdata = userdata;
		return r;
	}
	
	public String toString()
	{
		return (userdata == null ? "" : "["+userdata+"] ")+Arrays.toString(tokens);
	}
	public int size()
	{
		return tokens.length;
	}
	public RichtextItem item(int i)
	{
		return tokens[i];
	}

	public static Richtext from(List<RichtextItem> buffer) {
		return new Richtext(buffer.toArray(new RichtextItem[buffer.size()]));
	}

	public static Richtext from(String s) {
		return new Richtext(new RichtextItem[] {new ColoredString(Colors.WHITE, s)});
	}

	public static Richtext from(int color, String text) {
		return new Richtext(new RichtextItem[] {new ColoredString(color, text)});
	}

	@Override
	public Iterator<RichtextItem> iterator() {
		return new ArrayIterator<>(tokens);
	}
	
	public int width(RichtextDrawingContext ctx) {
		int num = 0;
		for (RichtextItem cs : this.tokens) {
			num += cs.width(ctx);
		}
		return num;
	}
	
	public int height(RichtextDrawingContext ctx) {
		int h = ctx.getDefaultTextDrawer().height(); // default to a minimum of the current font height
		for (RichtextItem cs : this.tokens) {
			int hh = cs.height(ctx);
			if(hh>h) h = hh;
		}
		return h;
		
	}
	private class ArrayIterator<E> implements Iterator<E> {

	    private final E[] array;
	    private final int count;
	    private int cursor = 0;
	    
	    public ArrayIterator(E[] contents) {
	        array = contents;
	        count = contents.length;
	    }

	    public boolean hasNext() {
	        if( cursor == count ) {
	            return false;
	        } else {
	            return true;
	        }
	    }

	    public E next() {
	        E sig = array[cursor];
	        cursor++;
	        return sig;
	    }

	    public void remove() throws UnsupportedOperationException {}
	}
	
}

package com.github.msx80.omicron.basicutils.text.richtext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class Buffer
{
	List<Richtext> lines = new ArrayList<Richtext>();
	List<RichtextItem> buffer = new ArrayList<RichtextItem>();
	private int currentWidth = 0;
	
	private String rtrim(String s)
	{
		int i = s.length()-1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
		    i--;
		}
		return s.substring(0,i+1);
		
	}
	public void newLine() {
		// remove trailing spaces from current line.
		if(!buffer.isEmpty())
		{
			int lastIdx = buffer.size()-1;
			RichtextItem x = buffer.get(lastIdx);
			if(x instanceof ColoredString)
			{
				buffer.set(lastIdx, new ColoredString(((ColoredString)x).color, rtrim(((ColoredString)x).text)));
			}
		}
		// complete a Richtext line
		Richtext t = Richtext.from(buffer);
		lines.add(t);
		// start a new buffer
		buffer = new ArrayList<>();
		currentWidth  = 0;
	}
	
	private void concat(int color, String text)
	{
		if(buffer.isEmpty())
		{
			buffer.add(new ColoredString(color,text));
		}
		else
		{
			int lastIdx = buffer.size()-1;
			RichtextItem x = buffer.get(lastIdx);
			// if the last item is also a ColoredString, and is the same color, concatenate instead of adding a new Item
			if(x instanceof ColoredString && ( ((ColoredString)x).color == color))
			{
				// concat!
				buffer.set(lastIdx, new ColoredString(color, ((ColoredString)x).text+text));
			}
			else
			{
				// no luck, add as new item.
				buffer.add(new ColoredString(color,text));
			}
		}
	}
	
	public void pushSpace(int color, int spaceSpace, int width)
	{
		// special treatment: always add the space at the end of the line. They will eventually overflow
		// but it's better than to start with a space on a newline
		currentWidth += spaceSpace;
		concat(color," ");
	}
	public void pushWord(int color, String word, RichtextDrawingContext ctx, int width)
	{
		// push a single word. If there's space, add it to the end, otherwise issue a newline and recurse.
		if(buffer.size() == 0)
		{
			// if it's empty, always add the first word (otherwise if it's wider than width it will loop forever)
			concat(color, word);
			currentWidth = ctx.getDefaultTextDrawer().width(word);
		}
		else
		{
			int w = ctx.getDefaultTextDrawer().width(word);
			if(w+currentWidth > width)
			{
				// overflow, flush line and recurse
				newLine(); 
				pushWord(color, word, ctx, width);
			}
			else
			{
				// there's enought space to concatenate the word in the current line
				currentWidth += w;
				concat(color, word);
			}
		}
	}
	public void pushFixedItem(RichtextItem next, RichtextDrawingContext ctx, int width) {
		int w = next.width(ctx);
		if(w+currentWidth > width)
		{
			// overflow, flush line and retry
			newLine(); 
			pushFixedItem(next, ctx, width);
		}
		else
		{
			currentWidth += w;
			buffer.add(next);
		}
		
	}
}

public class WordWrapper {
	
	private static Pattern REGEX_SPACES = Pattern.compile("(?<= )|(?= )");
	private static Pattern REGEX_NEWLINES = Pattern.compile("(?<=\n)|(?=\n)");
	
	
	/**
	 * Reflow a Richtext line so that it word-wraps at the specified width. It breaks up/merge ColoredStrings, any other type of item is passed as is.
	 * @param text
	 * @param td the TextDrawer used to measure the text.
	 * @param width width at which to wrap the text
	 * @return An array of Richtext, each item is a line to render. Each line should not exceed "width" (unless a single word exceed it)
	 */
	public static Richtext[] wrap(Richtext text, RichtextDrawingContext ctx, int width)
	{
		int spaceSpace = ctx.getDefaultTextDrawer().width(" ");
		Buffer b = new Buffer();
		dowrap(text, b, ctx, width, spaceSpace);
		
		// flush last buffer
		if(b.buffer.size()>0)
		{
			b.newLine();
			
		}
		return b.lines.toArray(new Richtext[b.lines.size()]);
	}

	private static void dowrap(Richtext text, Buffer b, RichtextDrawingContext ctx, int width, int spaceSpace) {
		// push all lines into the buffer taking newlines into account
		for (int i = 0; i < text.size(); i++) {
			RichtextItem next = text.item(i);
			pushWithNewlines(b, next, width, ctx, spaceSpace);
		}
		
	}

	private static void pushWithNewlines(Buffer b, RichtextItem next, int width, RichtextDrawingContext ctx, int spaceSpace) {
		
		// split the items containing newlines to divide the string. Use newLine to issue a new line to the buffer
		// and push the unbroken lines.
		if(next instanceof ColoredString)
		{
			String[] lines = REGEX_NEWLINES.split( ((ColoredString)next).text );
			for (int i = 0; i < lines.length; i++) {
				if(lines[i].equals("\n"))
				{
					b.newLine();
				}
				else
				{
					pushString(b, new ColoredString(((ColoredString)next).color, lines[i]), width, ctx, spaceSpace );
				}
			}
		}
		else
		{
			// things that are not ColoredStrings are passed as-is to be pushed, they're not split or merged.
			b.pushFixedItem(next, ctx, width);
		}
	}

	private static void pushString(Buffer b, ColoredString cs, int width, RichtextDrawingContext ctx, int spaceSpace) {
		// push a string in the buffer, passing words and spaces one at a time.
		String[] words = REGEX_SPACES.split(cs.text);
		for (int i = 0; i < words.length; i++) {
			if(words[i].equals(" "))
			{
				b.pushSpace(cs.color, spaceSpace, width);
			}
			else
			{
				b.pushWord(cs.color, words[i], ctx, width);
			}
		}
		
	}


}

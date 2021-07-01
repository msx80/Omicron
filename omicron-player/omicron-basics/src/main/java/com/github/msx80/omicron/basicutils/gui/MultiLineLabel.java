package com.github.msx80.omicron.basicutils.gui;

import java.util.ArrayList;
import java.util.List;

import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.text.TextDrawer;

public class MultiLineLabel extends BaseWidget {

	private String[] lines;
	private TextDrawer td;
	private Sys sys;
	private int color;
	
	public MultiLineLabel(String text, int color, TextDrawer font, Sys sys, int w) 
	{
		
		super(w, 0);
		this.td = font;
		this.sys = sys;
		this.color = color;
		setText(text);
	}
	public void setText(String text)
	{
		this.lines = splitLines(text, w);
		this.h = lines.length * (td.height()+1);
		invalidate();
	}
	private String[] splitLines(String text, int w) 
	{
		ArrayList<String> res = new ArrayList<>();
		String[] tok = text.split("\n");
		for (String string : tok) {
			split(string.split(" +"), 0, res);
		}
		return res.toArray(new String[res.size()]);
		
	
	}
	private void split(String[] tok, int idx,  List<String> res) {
		
		StringBuffer s = new StringBuffer();
		// always add first word
		s.append(tok[idx]);
		idx++;
		while (idx<tok.length) {
			String next = tok[idx]; 
			
			if (td.width(s+" "+next) > w) {
				res.add(s.toString());
				split(tok, idx, res);
				return;
			}
			else
			{
				s.append(" ");
				s.append(next);
				idx++;
			}
		}
		if(s.length()>0) res.add(s.toString());
	}
	
	@Override
	public void draw() {
		sys.color(this.color);
		for (int i = 0; i < lines.length; i++) {
			td.print(lines[i], 0,i*(td.height()+1));
		}
		sys.color(Colors.WHITE);


	}

}

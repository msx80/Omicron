package org.github.msx80.omicron.api;

public interface Sys {
	
    void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate, int flip);
    
    int newSurface(int w, int h);
    
    int getPix(int sheetNum, int x, int y);
    void setPix(int sheetNum, int x, int y, int color);
    
	void offset(int x, int y);

	void clear(int color);
	void color(int color);
	
	int fps();
	
	String mem(String key);
	void mem(String key, String value);
	
	Mouse mouse();

	Controller[] controllers();
	
	void sound(int soundNum, float volume, float pitch);
	
	String hardware(String module, String command, String param);

}

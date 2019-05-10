package org.github.msx80.omicron.api;

public interface Sys {
	
    void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h);
    void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate);
    
    int newSurface(int w, int h);
    
    int getPix(int sheetNum, int x, int y);
    void setPix(int sheetNum, int x, int y, int color);
    
	void offset(int x, int y);

	void http(String url, String method, String request, Acceptor<String> onSuccess, Acceptor<Exception> onError);
	void clear(int color);
	void color(int color);
	
	int fps();
	
	Mouse mouse();

	Controller[] controllers();
	
	String dbg();
}

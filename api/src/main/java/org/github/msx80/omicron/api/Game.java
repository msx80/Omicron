package org.github.msx80.omicron.api;
public interface Game {

    ScreenConfig screenConfig();
    
    void init(Sys sys);
    
    boolean update();
    
    void render();
    
}

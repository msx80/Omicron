package com.github.msx80.omicron.api;
public interface Game {

    SysConfig sysConfig();
    
    void init(Sys sys);
    
    boolean loop();
    
}

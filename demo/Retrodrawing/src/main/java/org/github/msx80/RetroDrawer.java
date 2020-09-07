package org.github.msx80;


import java.util.Stack;

// RETRO DOODLE ?


import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Pointer;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.TextDrawer;
import org.github.msx80.omicron.basicutils.TextDrawerFixed;

public class RetroDrawer implements Game, Ctx {
	
	ToolItem[] tools = {
			new ToolItem(0, SmallPen.class),
			new ToolItem(0, MediumPen.class),
			new ToolItem(0, BigPen.class),
			new ToolItem(0, SlowFill.class),
			new ToolItem(0, CleanAll.class),
			new ToolItem(0, Undo.class),
			
			
	};
	
	public static final int HEIGHT = 144;
	public static final int WIDTH = 256;
	
	public static final int SURFWIDTH = WIDTH-20;
	
	private Sys sys;
	int surface = 0;
	int cur = 5;
	Window w = null;
	Tool tool = null;
	
	Stack<byte[]> undos = new Stack<byte[]>();
	
	TextDrawer td;
	
	boolean cooldownMouse = false;
	
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
        surface = sys.newSurface(SURFWIDTH, HEIGHT);
        sys.fill(surface, 0,0,SURFWIDTH, HEIGHT, Palette.P[0]);
        
        tool = new SmallPen() ;
        
        td = new TextDrawerFixed(sys, 1, 6, 6, 6);
    }

    public void render() 
    {
    	sys.clear(Colors.WHITE);
    	ShapeDrawer.outline(sys,SURFWIDTH, 0, WIDTH-SURFWIDTH, HEIGHT,0, Colors.from(30, 30, 50));
    	
    	sys.fill(0, SURFWIDTH+2, 2, 16, 16, Palette.P[cur]);
    	
    	for (int i = 0; i < tools.length; i++) {
			ToolItem ti = tools[i];
			if(ti.toolClass.isInstance(tool)) 
	    	{
	    		sys.color(Colors.WHITE);
	    	}
	    	else
	    	{
	    		sys.color(Colors.from(255, 255, 255,100));
	    	}
	    	sys.draw(2, SURFWIDTH+2, 20*(i+1), i*16, 0, 16,16, 0,0);
	    	
		}
    	
    	sys.color(Colors.WHITE);
    	sys.draw(surface, 0,0,0,0, SURFWIDTH, HEIGHT, 0, 0);
    	
    	if(w!=null) w.draw(sys, 0);
    	
    	/*
    	sys.color(Colors.BLACK);
    	int i=0;
    	for (Pointer p : sys.pointers()) {
    		td.print(""+p, 2,i*10+2);
    		i++;
			
		}
		*/
    }

	public boolean update() 
	{
		Pointer m = sys.pointers()[0];
		if(tool!=null && tool.isBusy())
		{
			tool.update(this, m);
			return true;
		}
		
		// prevent window click to continue on the canvas
		if(cooldownMouse)
		{
			cooldownMouse = m.btn(0);
			return true;
		}
		
		
		if(m.x()<SURFWIDTH)
		{
			if(w!=null) 
			{
				if (!w.update(m)) 
				{
					w = null;
				}
			}
			else if(tool != null)
			{
				tool.update(this, m);
			}
		}
		else if (w == null && m.btn(0) && (!m.btnp(0)) )
		{
			if(m.y()<20)
			{
				w = new ColorWindow(i -> {cur = i; cooldownMouse = true;});
			}
			else
			{
				
				int tn = ((m.y()-20) / 20);
				if(tn<tools.length)
				{
					try {
						BaseTool bt = tools[tn].toolClass.newInstance(); 
						if (bt instanceof ClickyTool) {
							((ClickyTool) bt).execute(this);
						}
						else
						{
							tool = (Tool) bt;
						}
				
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			
			}
			cooldownMouse = true;
		}
		
		
        return false;
    }

	public boolean loop() 
	{
		boolean res = update();
		render();
		
        return res;
    }


	@Override
	public SysConfig sysConfig() 
	{
		return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.STRETCH_FULL, "RetroDrawer!", "retrodrawer");
	}

	@Override
	public int currentColor() {
		return cur;
	}

	@Override
	public Sys getSys() {
		return sys;
	}

	@Override
	public void recordUndo() {
		byte[] buf = SurfUtils.surfaceToBuffer(sys, surface, 0, 0, SURFWIDTH, HEIGHT);
		System.out.println(buf.length);
		undos.push(buf);
		if(undos.size()>10)
			undos.remove(0);
		
	}

	@Override
	public void undo() {
		if (!undos.isEmpty())
		{
			byte[] buf = undos.pop();
			SurfUtils.bufferToSurface(buf, sys, surface, 0, 0, SURFWIDTH, HEIGHT);
		}
		
	}

	@Override
	public int getSurface() {
		
		return surface;
	}
  
}

package net.lugato.fc.generic.demo;

import java.util.*;

import com.github.msx80.omicron.*;

public class OseoGame implements Game {

	Random r = new Random(System.currentTimeMillis());
	Image background;
	Image[] baloon;
	Image[][] Font;
	Sound pop;
	List<Item> items = new ArrayList<Item>();
			
	Map<Controller, Oseo> osei = new HashMap<Controller, Oseo>();
	private Sys sys;
	
	public void init(Sys sys) {
		this.sys = sys;
		
		Font = sys.loadSpritesheet("font6x8.png",6,8);
		pop = sys.loadSound("pop1.wav");
	
		background = sys.loadImage("cielo.png");

		baloon = new Image[]{ sys.loadImage("baeon1.png"),sys.loadImage("baeon2.png"),sys.loadImage("baeon3.png"),sys.loadImage("baeon4.png") };

		addBaloon();
		addBaloon();
		addBaloon();
		addBaloon();
	}

	private boolean collides(Oseo p2, Baeon b) {
		// find where the beak is (pos is the middle of the image)
		
		// x is on the edge of the image, either left or right.
		float beccox = p2.pos.x + (p2.flip ? -16 : +16);
		
		// about halfway y, so ok.
		float beccoy = p2.pos.y;
		
		// approximate baloon to a sphere of radius 16, check distance to beak
		return b.pos.dst2(beccox, beccoy)<(16*16);
	}

	public void update() {
				
		for (int i = 0; i < sys.availableControllers(); i++) {
			Controller c = sys.controller(i);
			if(!osei.containsKey(c))
			{
				if(c.wasPressed(0))
				{
				  Oseo p = new Oseo(sys.controller(i));
				  p.pos.set(100+i*40,100);
				  p.text = sys.loadImage("oseo"+((i%3)+1)+".png");;
				  items.add(p);
				  osei.put(c, p);
				}
			}
		}

		
		for (Item item : items) {
			item.update();
		}

		for (Oseo p : osei.values()) {
			for (int i = items.size()-1; i >= 0; i--) {
				Item b = items.get(i);
				if(b instanceof Baeon)
				{
					if(collides(p,(Baeon) b))
					{
						items.remove(i);
						sys.playSound(pop);
						p.poppedBaloon++;
						addBaloon();
						
					}
				}
			}
		}
		
	}

	private void addBaloon() {
		Baeon bb = new Baeon();
		bb.pos.set((float) (r.nextInt(Sys.SCREEN_WIDTH-32)+16), 220f);
		bb.text = baloon[r.nextInt(4)];
		items.add(bb);
	}

	public void render() {
			sys.draw(background, 0, 0);
			for(Item i : items)
			{
				sys.draw(i.text, (int)i.pos.x-16,(int)i.pos.y-16,i.flip, false);			
			}
			sys.color(0, 0, 1, 1);
			
			String s = "";
			
			for (int i = 0; i < sys.availableControllers(); i++) {
				Controller c = sys.controller(i);
				s += c.name()+": ";
				Oseo o = osei.get(c);
				if(o == null)
				{
						s+="Enter!";
				}
				else
				{
						s+=(""+o.poppedBaloon);
				}
				s+=" ";
			}
			
			
			
			sys.write(Font, 1, 1, "Score: "+s);
			
			if (osei.size() == 0) {
				sys.color(1, 0, 0, 1);
				sys.write(null, 10, 80, "Press Fire on any controller to enter!");
			}
				
	}

	public void close() {
		
	}
}

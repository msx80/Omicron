package omicron.demo;

public class Room {

	public int[][] tiles;

	public Room() {
		super();
		tiles = new int[Plat.ROOM_WIDTH][];
		for (int x = 0; x < Plat.ROOM_WIDTH; x++) {
			tiles[x] = new int[Plat.ROOM_HEIGHT];
			for (int y = 0; y < Plat.ROOM_HEIGHT; y++) {
				tiles[x][y] = 0;
			}
		}
		
		
		for (int x = 0; x < Plat.ROOM_WIDTH; x++) {
				tiles[x][Plat.ROOM_HEIGHT-1] = 1;
		}

		for (int x = 5; x < 10; x++) {
			tiles[x][18] = 1;
		}

		for (int x = 8; x < 15; x++) {
			tiles[x][14] = 1;
		}

		for (int x = 19; x < 25; x++) {
			tiles[x][14] = 1;
		}

		for (int x = 3; x < 8; x++) {
			tiles[x][10] = 1;
		}
		
		for (int x = 11; x < 30; x++) {
			tiles[x][7] = 1;
		}
		
	}
	
	public boolean blocked(int x, int y)
	{
		return tiles[x][y] == 1;
	}
	
}

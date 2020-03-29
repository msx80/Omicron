package org.github.msx80.omicron.basicutils;

import org.github.msx80.omicron.api.Sys;

/**
 * Utility to draw 2d tile based maps 
 *
 */
public class MapDrawer {

	public static interface MapData
	{
		int getTile(int tx, int ty) throws Exception;
	}
	
	private final static class MapDataArray implements MapData
	{
		private int[][] mapData;
		
		
		public MapDataArray(int[][] mapData) {
			this.mapData = mapData;
		}

		@Override
		public int getTile(int tx, int ty) throws Exception {
			
			return mapData[ty][tx];
		}
		
	}
	
	private Sys sys;
	private int tileWidth;
	private int tileHeight;
	private final MapData map;
	private int tilesPerRowOnSheet;

	public MapDrawer(Sys sys, int tileWidth, int tileHeight, int tilesPerRowOnSheet, int[][] mapData)
	{
		this.sys = sys;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tilesPerRowOnSheet = tilesPerRowOnSheet;
		this.map = new MapDataArray(mapData);
	}
	
	public MapDrawer(Sys sys, int tileWidth, int tileHeight, int tilesPerRowOnSheet, MapData mapData)
	{
		this.sys = sys;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tilesPerRowOnSheet = tilesPerRowOnSheet;
		this.map = mapData;
	}
	
	public void draw(int tileSheetNum, int destx, int desty, int srctx, int srcty, int twidth, int theight)
	{
		try {
			for (int y = 0; y < theight; y++) {
				for (int x = 0; x < twidth; x++) {
					
					int c = map.getTile(x+srctx, y+srcty);
					int dx = c % tilesPerRowOnSheet;
					int dy = c / tilesPerRowOnSheet;
					
					sys.draw(tileSheetNum, destx + x*tileWidth, desty + y*tileHeight, dx*tileWidth, dy*tileHeight, tileWidth, tileHeight, 0, 0);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Map drawing error: "+e.getMessage(), e);
		}
	}
}

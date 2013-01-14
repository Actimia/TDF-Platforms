package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class Map implements Entity, GameConstants {

    private Matrix<Tile> tiles;
    private TiledMap tiled;
    private int width;
    private int height;

    // public static final float TILESIZE = 32f;
    // public static final Vec2 tilesize_ = new Vec2(tilesize, tilesize);
    private Vec2 spawn;

    public Map(String ref) throws SlickException {
        tiled = new TiledMap(ref);
        width = tiled.getWidth();
        height = tiled.getHeight();
        tiles = new Matrix<Tile>(width, height);
        int layer = tiled.getLayerIndex("layer1");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int tileID = tiled.getTileId(x, y, layer);
                createTileAt(x, y, tiled.getTileProperty(tileID, "type", "air"));
            }
        }
        // System.out.println(tiled.getObjectGroupCount());
        for (int gid = 0; gid < tiled.getObjectGroupCount(); gid++) {
            for (int oid = 0; oid < tiled.getObjectCount(gid); oid++) {
                String type = tiled.getObjectType(gid, oid);
                if (type.equals("spawn")) {
                    int x = tiled.getObjectX(gid, oid);
                    int y = tiled.getObjectY(gid, oid);
                    spawn = new Vec2(x, y);
                }
            }
        }
    }

    private void createTileAt(int x, int y, String type) {
        switch (type) {
        case "air":
            tiles.set(x, y, new AirTile(x * TILESIZE, y * TILESIZE));
            break;
        case "ground":
            tiles.set(x, y, new GroundTile(x * TILESIZE, y * TILESIZE));
            break;
        case "antigrav":
            tiles.set(x, y, new AntiGravityTile(x * TILESIZE, y * TILESIZE));
            break;
        case "drift":
            tiles.set(x, y, new DriftTile(x * TILESIZE, y * TILESIZE));
            break;
        case "oneway":
            tiles.set(x, y, new OneWayPlatformTile(x * TILESIZE, y * TILESIZE));
            break;
        case "ladder":
            tiles.set(x, y, new LadderTile(x * TILESIZE, y * TILESIZE));
            break;
        default:
            System.out.println("unregnozed tile: " + type + " @ " + x + "," + y);
            // create air anyway to prevent nullpointers
            tiles.set(x, y, new AirTile(x * TILESIZE, y * TILESIZE));
            break;
        }

    }

    private Vec2 fromCoords(int x, int y) {
        return new Vec2(TILESIZE * x, TILESIZE * y);
    }

    public Tile tileAt(float x, float y) {
        if (x < 0 || y < 0) {
            return null;
        }
        return tiles.get((int) (x / TILESIZE), (int) (y / TILESIZE));
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {

        tiled.render(0, 0); // already offset by camera
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile t = tiles.get(i, j);
                if (t != null) {
                    t.render(c, s, g, game);
                }
            }
        }
    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        return true;
    }

    @Override
    public Shape getBounds() {
        return new Rectangle(0, 0, width * TILESIZE, height * TILESIZE);
    }

    public Vec2 getSpawn() {
        return spawn;
    }

    public boolean isBlocking(float x, float y) {
        Tile tile = tileAt(x, y);
        return tile != null ? tile.isBlocking() : true;
    }

    public TileType getTileType(float x, float y) {
        Tile tile = tileAt(x, y);
        return tile != null ? tile.getType() : TileType.OUTOFBOUNDS;
    }

    public float getGravity(float x, float y) {
        Tile tile = tileAt(x, y);
        return tile != null ? tile.getGravity() : AirTile.GRAVITY;
    }
}
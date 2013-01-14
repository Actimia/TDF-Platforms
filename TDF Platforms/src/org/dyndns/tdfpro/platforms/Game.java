package org.dyndns.tdfpro.platforms;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class Game extends BasicGameState {

    private static final float PAN_LIMIT = 0.4f;

    private Map map;
    private Player player;
    private int tilesize;

    private Timers timers = new Timers();
    private Monsters monsters = new Monsters();

    private Vec2 camera = Vec2.ZERO;
    private static HashMap<String, Image> sprites = new HashMap<String, Image>();
    private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
    private UnicodeFont font;

    @SuppressWarnings("unchecked")
    @Override
    public void init(GameContainer c, StateBasedGame s) throws SlickException {
        long time = System.currentTimeMillis();
        loadSprites();
        loadSounds();
        font = new UnicodeFont(new java.awt.Font("consolas", java.awt.Font.PLAIN, 20));
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect());
        font.loadGlyphs();
        map = new Map("res/maps/test1.tmx");
        player = new Player(map.getSpawn());
        camera.x = Utils.clamp(player.getBounds().getCenterX() - c.getWidth() / 2, 0, map
                .getBounds().getWidth());

        time = System.currentTimeMillis() - time;
        Log.info("Initialization took: " + time + " ms.");
    }

    private void loadSprites() throws SlickException {
        Image spritesheet = new Image("res/gfx/sprites.png");
        sprites.put("arrow", spritesheet.getSubImage(32, 56, 30, 4));
        sprites.put("player", spritesheet.getSubImage(0, 224, 30, 29));
        sprites.put("bow", spritesheet.getSubImage(0, 50, 32, 64));
        sprites.put("sword", spritesheet.getSubImage(0, 20, 119, 30));
        sprites.put("gun", spritesheet.getSubImage(31, 67, 63, 18));
        sprites.put("monster", spritesheet.getSubImage(0, 115, 26, 57));

        // SpriteSheet tilesheet = new SpriteSheet("res/gfx/tiles.png", (int)
        // Map.tilesize_.x,
        // (int) Map.tilesize_.y);
        // sprites.put("air", tilesheet.getSubImage(0, 0, 32, 32));
        // sprites.put("ground", tilesheet.getSubImage(32, 0, 32, 32));
        // sprites.put("ladder", tilesheet.getSubImage(64, 0, 32, 32));
    }

    /**
     * Loads all sounds in the res/sfx folder
     * 
     * @throws SlickException
     */
    private void loadSounds() throws SlickException {
        String ref = "res/sfx";
        File folder = new File(ref);
        for (File f : folder.listFiles()) {
            if (f.isFile()) {
                String name = f.getName();
                if (name.contains(".")) {
                    sounds.put(name.substring(0, name.lastIndexOf('.')), new Sound(f.getPath()));
                }
            }
        }
    }

    public static Image getSprite(String name) {
        return sprites.get(name);
    }

    public static Sound getSound(String name) {
        return sounds.get(name);
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g) throws SlickException {
        g.resetTransform();
        // g.setAntiAlias(true);
        g.translate(-camera.x, -camera.y);
        g.setWorldClip(camera.x, camera.y, c.getWidth(), c.getHeight());
        map.render(c, s, g, this);
        player.render(c, s, g, this);
        // g.resetTransform();
        // font.drawString(100, 100, "lorem ipsur lathund flyger");

    }

    @Override
    public void update(GameContainer c, StateBasedGame s, int delta) throws SlickException {
        Input in = c.getInput();
        if (in.isKeyDown(Input.KEY_ESCAPE)) {
            c.exit();
        }
        timers.update(delta);
        player.update(c, s, delta, this);

        // pan camera X-wise if player is outside the panlimit
        if ((player.getBounds().getCenterX() < camera.x + c.getWidth() * PAN_LIMIT && player
                .getXVelo() < 0)
                || (player.getBounds().getCenterX() > camera.x + c.getWidth() * (1 - PAN_LIMIT) && player
                        .getXVelo() > 0)) {
            camera.x = Utils.clamp(
                    (float) (camera.x + Math.signum(player.getXVelo())
                            * Math.ceil(Math.abs(player.getXVelo()) * delta / 1000f)), map
                            .getBounds().getMinX(), map.getBounds().getMaxX() - c.getWidth());
        }
    }

    public void addTimer(Timer timer) {
        timers.addTimer(timer);
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    @Override
    public int getID() {
        return Launcher.GAME;
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public Vec2 getCamera() {
        return camera;
    }

}

package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

enum TileType {
    AIR, GROUND, LADDER, OUTOFBOUNDS;

}

public interface Tile extends Entity, GameConstants {
    boolean isBlocking();

    float getGravity();

    boolean standOnTop();

    TileType getType();

    boolean driftToCenter();

    float terminalSpeed();
}

abstract class AbstractTile implements Tile {
    protected Rectangle bounds;

    public AbstractTile(float x, float y) {
        bounds = new Rectangle(x, y, TILESIZE, TILESIZE);
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {

    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        return true;
    }

    @Override
    public Shape getBounds() {
        return bounds;
    }

    @Override
    public boolean standOnTop() {
        return isBlocking();
    }

    @Override
    public float terminalSpeed() {
        return 640;
    }

}

class GroundTile extends AbstractTile {

    public GroundTile(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public float getGravity() {
        return 1200;
    }

    @Override
    public TileType getType() {
        return TileType.GROUND;
    }

    @Override
    public boolean driftToCenter() {
        return false;
    }

}

class AirTile extends AbstractTile {
    // public static final float GRAVITY = 2400;
    private Rectangle bounds;

    public AirTile(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public float getGravity() {
        return GRAVITY;
    }

    @Override
    public TileType getType() {
        return TileType.AIR;
    }

    @Override
    public boolean driftToCenter() {
        return false;
    }
}

class DriftTile extends AirTile {

    public DriftTile(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean driftToCenter() {
        return true;
    }
}

class OneWayPlatformTile extends AirTile {

    public OneWayPlatformTile(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean standOnTop() {
        return true;
    }

}

class LadderTile extends AbstractTile {

    public LadderTile(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public float getGravity() {
        return 0;
    }

    @Override
    public boolean standOnTop() {
        return true;
    }

    @Override
    public TileType getType() {
        return TileType.LADDER;
    }

    @Override
    public boolean driftToCenter() {
        return true;
    }

    @Override
    public float terminalSpeed() {
        return 128f;
    }

}

class AntiGravityTile extends AirTile {

    public AntiGravityTile(float x, float y) {
        super(x, y);
    }

    @Override
    public float getGravity() {
        return GRAVITY * -1f / 10f;
    }

    @Override
    public boolean driftToCenter() {
        return true;
    }

    @Override
    public boolean standOnTop() {
        return true;
    }

    @Override
    public float terminalSpeed() {
        return 160f;
    }

}

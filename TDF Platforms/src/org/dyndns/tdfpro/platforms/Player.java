package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Player implements Entity {

    public static final Vec2 hand = new Vec2(15, 20);

    private static final float TERMINAL_SPEED = 640;
    private static final float JUMP_IMPULSE = -704;
    private static final float X_SPEED_WALKING = 192;
    private static final float X_SPEED_RUNNING = 192;
    private Rectangle bounds;
    private boolean airborne = false;
    private boolean canjump = true;
    private Vec2 velo = Vec2.ZERO;
    private float yvelo;
    private float xvelo;
    private Weapon weapon;
    private Image img;

    public Player(Vec2 spawn) {
        bounds = new Rectangle(spawn.x + 1, spawn.y + 3, 30, 29);
        weapon = new Rifle(this);
        img = Game.getSprite("player");
    }

    @Override
    public Shape getBounds() {
        return bounds;
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
        g.setColor(Color.black);
        // g.draw(bounds);
        g.drawImage(img, bounds.getX(), bounds.getY());

        weapon.render(c, s, g, game);

    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        Input in = c.getInput();
        Map map = game.getMap();
        Tile tile = map.tileAt(bounds.getCenterX(), bounds.getMaxY());
        float xmove = 0;
        float ymove = 0;
        boolean ladder = tile.getType() == TileType.LADDER;

        // resolve frame acceleration.
        // x axis
        if (in.isKeyDown(Input.KEY_A)) {
            xmove--;
        }
        if (in.isKeyDown(Input.KEY_D)) {
            xmove++;
        }
        // y axis
        if (ladder) {
            yvelo = 0;
            airborne = false;
            if (in.isKeyDown(Input.KEY_W)) {
                ymove--;
            }
            if (in.isKeyDown(Input.KEY_S)) {
                ymove++;
            }
            ymove = ymove * tile.terminalSpeed() * delta / 1000f;
        } else {
            if (in.isKeyDown(Input.KEY_SPACE) && !airborne && canjump) {
                airborne = true;
                canjump = false;
                yvelo = JUMP_IMPULSE;
                Game.getSound("jump1").play(1f, 0.5f);
            }
            float grav = map.getGravity(bounds.getCenterX(), bounds.getMaxY());
            float terminalspeed = tile.terminalSpeed();
            yvelo = Utils.clamp(yvelo + grav * delta / 1000f, -terminalspeed, terminalspeed);
            ymove = yvelo * delta / 1000f;
        }
        boolean ignoreoneways = in.isKeyDown(Input.KEY_S);
        if (xmove == 0 && tile.driftToCenter()) { // if not moving, might drift
                                                  // to center
            float desiredX = tile.getBounds().getCenterX();
            float diff = desiredX - bounds.getCenterX();
            if (Math.abs(diff) > 2) { // do it with acceleration
                xmove = Math.signum(diff);
            } else { // do it manually the last bit
                xmove = 0;
                bounds.setCenterX(desiredX);
            }
        }

        // reset jump key
        if (!in.isKeyDown(Input.KEY_SPACE) && !airborne) {
            canjump = true;
        }

        // Y collision check
        boolean yblocked = false;
        float testy = 0;
        if (ymove > 0) { // Y collision moving down
            float edge = bounds.getMaxY();
            while (!yblocked && testy < ymove) {
                float testedge = edge + testy + 1;
                if (!ignoreoneways && testedge % TILESIZE == 0) {
                    // if
                    // S
                    // is
                    // pressed,
                    // ignore
                    // onewayplatforms
                    // yblocked = (map.tileAt(bounds.getMinX(),
                    // testedge).standOnTop() || map.tileAt(
                    // bounds.getMaxX(), testedge).standOnTop());
                    yblocked = map.tileAt(bounds.getCenterX(), testedge).standOnTop()
                            || map.isBlocking(bounds.getMinX(), testedge)
                            || map.isBlocking(bounds.getMaxX(), testedge);
                    // } else if (ladder && testedge % TILESIZE == 0) {
                    // Tile min = map.tileAt(bounds.getMinX(), testedge);
                    // Tile max = map.tileAt(bounds.getMaxX(), testedge);
                    // System.out.println(min);
                    // yblocked = min.getType() == TileType.LADDER ? false :
                    // min.standOnTop()
                    // || max.getType() == TileType.LADDER ? false :
                    // max.standOnTop();
                } else {
                    yblocked = map.isBlocking(bounds.getMinX(), testedge)
                            || map.isBlocking(bounds.getMaxX(), testedge);
                }

                if (!yblocked) {
                    testy++;
                }
            }
            if (yblocked && airborne) { // landing, enable jumps again
                airborne = false;
            } else if (!yblocked && !airborne) { // falling, disable jumps
                airborne = true;
            }
            ymove = testy;
        } else { // moving up
            float edge = bounds.getMinY();
            while (!yblocked && testy > ymove) {

                yblocked = map.isBlocking(bounds.getMinX(), edge + testy - 1)
                        || map.isBlocking(bounds.getMaxX(), edge + testy - 1);
                if (!yblocked) {
                    testy--;
                }
            }
            if (tile.getGravity() < 0) {
                airborne = true;
            }
            ymove = testy;
        }
        if (yblocked) { // hitting an obstacle, reset velocity
            yvelo = 0;
        }
        bounds.setY(bounds.getY() + ymove);
        // X collision check

        float xtargetspeed = xmove != 0 ? xmove * X_SPEED_WALKING : 0;
        // 0 if not moving,else terminal speed in the direction of movement

        xvelo = Utils.lerp(xvelo + xmove, xtargetspeed, 0.3f); // linear
                                                               // interpolation
        if (Math.abs(xvelo) < 0.1) { // dropoff if small enough
            xvelo = 0;
        }
        xmove = xvelo * delta / 1000f;
        boolean xblocked = false;
        float testx = 0;
        if (xmove > 0) { // X collision moving right
            float edge = bounds.getMaxX();
            while (!xblocked && testx < xmove) {
                xblocked = map.isBlocking(edge + testx + 1, bounds.getMaxY())
                        || map.isBlocking(edge + testx + 1, bounds.getCenterY())
                        || map.isBlocking(edge + testx + 1, bounds.getMinY());
                if (!xblocked) {
                    testx++;
                }
            }
            xmove = testx;
        } else { // moving left
            float edge = bounds.getMinX();
            while (!xblocked && testx > xmove) {
                xblocked = map.isBlocking(edge + testx - 1, bounds.getMaxY())
                        || map.isBlocking(edge + testx - 1, bounds.getCenterY())
                        || map.isBlocking(edge + testx - 1, bounds.getMinY());
                if (!xblocked) {
                    testx--;
                }
            }
            xmove = testx;
        }
        if (xblocked) { // reset velocity if an obstacle was hit
            xvelo = 0;
        }
        bounds.setX(bounds.getX() + xmove);
        weapon.update(c, s, delta, game);
        return true;
    }

    private Vec2 botLeft() {
        return new Vec2(bounds.getX(), bounds.getY() + bounds.getHeight());
    }

    private Vec2 botRight() {
        return new Vec2(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());
    }

    private Vec2 topLeft() {
        return new Vec2(bounds.getX(), bounds.getY());
    }

    private Vec2 topRight() {
        return new Vec2(bounds.getX() + bounds.getWidth(), bounds.getY());
    }

    private Vec2 center() {
        return new Vec2(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight()
                / 2);
    }

    public float getXVelo() {
        return xvelo;
    }
}

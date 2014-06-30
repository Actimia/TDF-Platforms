package org.dyndns.tdfpro.platforms;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.StateBasedGame;

public class Rifle implements Weapon {

    private static final Vec2 hold = new Vec2(-18, -9);
    private static final Vec2 offset = Player.hand.add(hold);
    private static final Vec2 bulletoriginoffset = new Vec2(0, -3);
    private static final int SHOT_COOLDOWN = 100;

    private Image imgright;
    private Image imgleft;
    private Rectangle bounds;
    private Shape curbounds;
    private Vec2 rotcenter;
    private float angle;

    private ArrayList<Shot> shots = new ArrayList<Shot>();
    private int cooldown;

    private Player player;

    public Rifle(Game g, Player player) {
        imgright = Game.getSprite("gun");
        imgleft = imgright.getFlippedCopy(false, true);
        bounds = new Rectangle(player.getBounds().getX() + offset.x, player.getBounds().getY()
                + offset.y, 63, 18);
        curbounds = bounds;
        rotcenter = new Vec2(bounds.getX() + hold.x, bounds.getY() + hold.y);

        this.player = player;
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
        for (Shot bullet : shots) {
            bullet.render(c, s, g, game);
        }
        g.rotate(rotcenter.x, rotcenter.y, angle);
        if (Math.abs(angle) < 90f) {
            g.drawImage(imgright, bounds.getX(), bounds.getY());
        } else {
            g.drawImage(imgleft, bounds.getX(), bounds.getY());
        }

        g.rotate(rotcenter.x, rotcenter.y, -angle);

        // g.draw(curbounds);
        // g.fill(new Circle(rotcenter.x, rotcenter.y, 3));
    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        Input in = c.getInput();

        bounds.setLocation(player.getBounds().getX() + offset.x, player.getBounds().getY()
                + offset.y);

        rotcenter.x = bounds.getX() - hold.x;
        rotcenter.y = bounds.getY() - hold.y;
        Vec2 cam = game.getCamera();
        Vec2 mousepos = new Vec2(in.getMouseX(), in.getMouseY()).addl(cam);
        angle = rotcenter.angle(mousepos);
        curbounds = bounds.transform(Transform.createRotateTransform((float) Math.toRadians(angle),
                rotcenter.x, rotcenter.y));

        cooldown -= delta;
        if (in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && cooldown <= 0) { // firing
            Vec2 origin = rotcenter.add(bulletoriginoffset);
            shots.add(new Shot(origin, angle));
            Game.getSound("shot1").play();
            cooldown = SHOT_COOLDOWN - 50 + (int) (100 * Math.random()); // some
                                                                         // randomness
        }
        for (Shot bullet : shots) {
            bullet.update(c, s, delta, game);
        }
        Iterator<Shot> it = shots.iterator();
        while (it.hasNext()) {
            if (!it.next().update(c, s, delta, game)) {
                it.remove();
            }
        }
        return true;
    }

    @Override
    public Shape getBounds() {
        return curbounds;
    }

    private class Shot implements Entity {

        private static final float BULLET_VELOCITY = 300f;

        private Circle bounds;
        private Vec2 velocity;

        public Shot(Vec2 origin, float angle) {
            bounds = new Circle(origin.x, origin.y, 2);
            velocity = Vec2.fromAngleAndLength(angle, BULLET_VELOCITY);
        }

        @Override
        public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
            // g.setColor(Color.black);
            // g.fill(bounds);
            g.setColor(Color.red);
            g.setLineWidth(3f);
            Vec2 end = velocity.scale(-20f).add(new Vec2(bounds.getCenterX(), bounds.getCenterY()));
            g.drawLine(bounds.getCenterX(), bounds.getCenterY(), end.x, end.y);
        }

        @Override
        public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
            Vec2 velo = velocity.mul(delta / 1000f);
            bounds.setLocation(bounds.getX() + velo.x, bounds.getY() + velo.y);
            Shape mapbounds = game.getMap().getBounds();
            boolean alive = !game.getMap().isBlocking(bounds.getCenterX(), bounds.getCenterY())
                    || mapbounds.contains(bounds);
            Tile tile = game.getMap().tileAt(bounds.getCenterX(), bounds.getCenterY());
            if (tile != null && tile.isBlocking()) {
                Game.getSound("impact1").playAt(
                        (bounds.getX() - game.getPlayer().getBounds().getX()) / 20,
                        (bounds.getY() - game.getPlayer().getBounds().getY()) / 20, 0);
            }
            return alive;

        }

        @Override
        public Shape getBounds() {
            return bounds;
        }

    }

}

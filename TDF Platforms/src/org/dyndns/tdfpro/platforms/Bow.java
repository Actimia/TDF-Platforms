package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

class Bow implements Weapon {
    private static final Vec2 hold = new Vec2(8, -32);
    private static final Vec2 offset = Player.hand.add(hold);
    private static final Vec2 upperStringPoint = new Vec2(7, 2);
    private static final Vec2 lowerStringPoint = new Vec2(7, 61);

    private Image img;
    private Vec2 rotcenter;
    private float angle;
    private Rectangle bounds;

    private Player player;

    class Arrow implements Entity {
        private Image img;
        private float angle;
        private Shape bounds;

        public Arrow(Game g, Player p, float a) {
            img = g.getSprite("arrow");
        }

        @Override
        public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {

        }

        @Override
        public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
            // TODO Auto-generated method stub
            return true;

        }

        @Override
        public Shape getBounds() {
            return bounds;
        }

    }

    public Bow(Game g, Player p) {
        player = p;
        img = g.getSprite("bow");
        bounds = new Rectangle(p.getBounds().getX() + offset.x, p.getBounds().getY() + offset.y,
                32, 64);

        rotcenter = new Vec2(bounds.getX() - hold.x, bounds.getY() - hold.y);
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
        g.rotate(rotcenter.x, rotcenter.y, angle);
        g.drawImage(img, bounds.getX(), bounds.getY());
        Vec2 stringpoint = new Vec2(bounds.getMinX(), bounds.getCenterY());
        Vec2 topleft = new Vec2(bounds.getX(), bounds.getY());
        Vec2 upperString = upperStringPoint.add(topleft);
        Vec2 lowerString = lowerStringPoint.add(topleft);
        g.drawLine(stringpoint.x, stringpoint.y, upperString.x, upperString.y);
        g.drawLine(stringpoint.x, stringpoint.y, lowerString.x, lowerString.y);
        // g.draw(bounds);
        g.rotate(rotcenter.x, rotcenter.y, -angle);

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
        return true;

    }

    @Override
    public Shape getBounds() {
        return bounds; // no bounds
    }

}
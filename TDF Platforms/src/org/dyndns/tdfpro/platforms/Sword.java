package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.StateBasedGame;

public class Sword implements Weapon {
    private static final Vec2 hold = new Vec2(-16, -15);
    private static final Vec2 offset = Player.hand.add(hold);

    private Image img;
    private Rectangle bounds;
    private Shape curbounds;
    private boolean attacking;
    private Vec2 rotcenter;
    private float angle;

    private Player player;

    public Sword(Game g, Player player) {
        img = g.getSprite("sword");
        bounds = new Rectangle(player.getBounds().getX() + offset.x, player.getBounds().getY()
                + offset.y, 120, 31);
        curbounds = bounds;
        rotcenter = new Vec2(bounds.getX() + hold.x, bounds.getY() + hold.y);

        this.player = player;
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {

        g.rotate(rotcenter.x, rotcenter.y, angle);
        g.drawImage(img, bounds.getX(), bounds.getY());

        g.rotate(rotcenter.x, rotcenter.y, -angle);
        g.draw(curbounds);

        g.fill(new Circle(rotcenter.x, rotcenter.y, 3));
    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        Input in = c.getInput();

        // attacking = in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);

        bounds.setLocation(player.getBounds().getX() + offset.x, player.getBounds().getY()
                + offset.y);
        // rotcenter = Player.hand.add(new Vec2(player.getBounds().getX(),
        // player.getBounds().getY()));
        rotcenter.x = bounds.getX() - hold.x;
        rotcenter.y = bounds.getY() - hold.y;
        Vec2 cam = game.getCamera();
        Vec2 mousepos = new Vec2(in.getMouseX(), in.getMouseY()).addl(cam);
        angle = rotcenter.angle(mousepos);
        curbounds = bounds.transform(Transform.createRotateTransform((float) Math.toRadians(angle),
                rotcenter.x, rotcenter.y));
        return true;
    }

    @Override
    public Shape getBounds() {
        return curbounds;
    }
}

interface Weapon extends Entity {

}
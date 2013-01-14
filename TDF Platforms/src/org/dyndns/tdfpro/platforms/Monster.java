package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Monster implements Entity {
    private Rectangle bounds;
    private Image img;

    private static final int MAXHEALTH = 100;
    private int health = MAXHEALTH;

    public Monster(float x, float y) {
        img = Game.getSprite("monster");
        bounds = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
        g.drawImage(img, bounds.getX(), bounds.getY());
    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        return health > 0;
    }

    @Override
    public Shape getBounds() {
        return bounds;
    }

    public void damage(int damage) {
        health -= damage;
    }

}

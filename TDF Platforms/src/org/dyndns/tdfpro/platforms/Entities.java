package org.dyndns.tdfpro.platforms;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Entities implements Entity {
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
        for (Entity e : entities) {
            e.render(c, s, g, game);
        }

    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
            if (!it.next().update(c, s, delta, game)) {
                it.remove();
            }
        }
        return true;
    }

    @Override
    public Shape getBounds() {
        return null;
    }

    public void add(Entity e) {
        entities.add(e);
    }
}

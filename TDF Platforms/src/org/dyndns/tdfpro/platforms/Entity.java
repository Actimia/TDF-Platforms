package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public interface Entity extends GameConstants {
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game);

    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game);

    public Shape getBounds();
}

package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class MessageArea implements Entity {

    private static final int PADDING = 10;
    private static final int CORNER_RADIUS = 10;
    private static final float Y_OFFSET = 75f;

    private Rectangle bounds;
    private String msg;
    private boolean show;
    private int textwidth = -1;
    private int textheight = -1;
    private Rectangle upperborder;
    private Rectangle lowerborder;

    public MessageArea(Rectangle bounds, String msg) {
        this.bounds = bounds;
        this.msg = msg.replaceAll("\n", System.lineSeparator());
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g, Game game) {
        if (show) {
            if (textwidth == -1) {
                Font f = g.getFont();
                textwidth = f.getWidth(msg);
                textheight = f.getLineHeight();
                upperborder = new Rectangle((c.getWidth() - textwidth) / 2 - PADDING, Y_OFFSET,
                        textwidth + 2 * PADDING, 2 * PADDING + textheight);
                lowerborder = new Rectangle((c.getWidth() - textwidth) / 2 - PADDING, c.getHeight()
                        - Y_OFFSET - 2 * PADDING - textheight, textwidth + 2 * PADDING, 2 * PADDING
                        + textheight);
            }
            Rectangle border;
            if (game.getPlayer().getBounds().getCenterY() < c.getHeight() / 2) {
                border = lowerborder;
            } else {
                border = upperborder;
            }
            g.setLineWidth(4.0f);
            g.setColor(Color.black);
            g.fill(border);
            g.setColor(Color.green);
            g.draw(border);
            g.drawString(msg, border.getX() + PADDING, border.getY() + PADDING);
        }
    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta, Game game) {
        Vec2 player = game.getPlayer().center();
        boolean prev = show;
        show = bounds.contains(player.x, player.y);
        if (show && show != prev) { // if show was false before but is now true
            Game.getSound("info1").play(1f, 0.3f);
        }
        return true;
    }

    @Override
    public Shape getBounds() {
        return bounds;
    }

}

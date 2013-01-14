package org.dyndns.tdfpro.platforms;

import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState {
    private ArrayList<Component> comps = new ArrayList<Component>();

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        comps.add(new Button(100, 100, 100, 100, "start", Color.green, Color.black, new Action() {

            @Override
            public void act(GameContainer c, StateBasedGame s) {
                s.enterState(Launcher.GAME);

            }

        }));

    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g) throws SlickException {
        for (Component co : comps) {
            co.render(c, s, g);
        }

    }

    @Override
    public void update(GameContainer c, StateBasedGame s, int delta) throws SlickException {
        for (Component co : comps) {
            co.update(c, s, delta);
        }

    }

    @Override
    public int getID() {
        return Launcher.MENU;
    }

}

class ImagePanel implements Component {
    private Vector2f pos;
    private Vector2f dim;
    private Rectangle box;
    private Image img;

    public ImagePanel(int x, int y, int width, int height, Image img) {
        pos = new Vector2f(x, y);
        dim = new Vector2f(width, height);
        box = new Rectangle(pos.x, pos.y, dim.x, dim.y);
        this.img = img;
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g) {
        g.drawImage(img, pos.x, pos.y, pos.x + dim.x, pos.y + dim.y, 0, 0, img.getWidth(),
                img.getHeight());

    }

    @Override
    public void update(GameContainer c, StateBasedGame s, int delta) {

    }

    @Override
    public Shape getBounds() {
        return box;
    }

}

class Button implements Component {
    private Vec2 pos;
    private Vec2 dim;
    private Rectangle box;
    private Action a;
    private String txt;
    private int halftextwidth = -1;
    private int halftextheight = -1;
    private Color fgCol;
    private Color bgCol;

    public Button(int x, int y, int width, int height, String text, Color fg, Color bg, Action a) {
        pos = new Vec2(x, y);
        dim = new Vec2(width, height);
        this.a = a;
        txt = text;
        box = new Rectangle(pos.x, pos.y, dim.x, dim.y);
        fgCol = fg;
        bgCol = bg;
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g) {
        g.setColor(bgCol);
        g.fill(box);
        g.setLineWidth(5.0f);
        g.setColor(fgCol);

        // g.draw(new Circle(200, 200, 10));
        g.drawRoundRect(box.getX(), box.getY(), box.getWidth(), box.getHeight(), 10);

        if (halftextwidth == -1) {
            Font f = g.getFont();
            halftextwidth = f.getWidth(txt) / 2;
            halftextheight = f.getHeight(txt) / 2;
        }
        g.drawString(txt, pos.x + dim.x / 2 - halftextwidth, pos.y + dim.y / 2 - halftextheight);

    }

    @Override
    public void update(GameContainer c, StateBasedGame s, int delta) {
        Input in = c.getInput();
        if (in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
                && box.contains(in.getMouseX(), in.getMouseY())) {
            a.act(c, s);
        }

    }

    @Override
    public Shape getBounds() {
        return box;
    }

}

interface Action {
    public void act(GameContainer c, StateBasedGame s);
}

interface Component {
    public void render(GameContainer c, StateBasedGame s, Graphics g);

    public void update(GameContainer c, StateBasedGame s, int delta);

    public Shape getBounds();
}
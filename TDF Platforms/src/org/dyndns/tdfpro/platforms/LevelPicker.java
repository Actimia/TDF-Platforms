package org.dyndns.tdfpro.platforms;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelPicker extends BasicGameState {

    private static final String path = "res/maps";
    private ArrayList<String> levels = new ArrayList<String>();
    private ArrayList<Component> comps = new ArrayList<Component>();

    @Override
    public void init(GameContainer c, StateBasedGame g) throws SlickException {
        File folder = new File(path);
        int index = 0;
        for (File f : folder.listFiles()) {
            if (f.isFile()) {
                final String name = f.getName();
                if (name.endsWith(".tmx")) {
                    comps.add(new Button(100 + index % 10 * 75, 100 + index / 10 * 75, 60, 60, name
                            .substring(0, name.lastIndexOf('.')), Color.green, Color.black,
                            new Action() {
                                @Override
                                public void act(GameContainer c, StateBasedGame s) {
                                    s.addState(new Game(path + "/" + name));
                                    try {
                                        s.getState(Launcher.GAME).init(c, s);
                                    } catch (SlickException e) {
                                        e.printStackTrace();
                                    }
                                    s.enterState(Launcher.GAME);

                                }
                            }));
                }
                index++;
            }
        }

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
        return Launcher.LEVELS;
    }

}

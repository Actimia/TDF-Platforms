package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Launcher extends StateBasedGame {

    public static final int GAME = 1;
    public static final int MENU = 0;
    public static final int LEVELS = 2;

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Launcher(), 1024, 640, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(false);
            app.setAlwaysRender(true);
            app.setVSync(true);
            app.setSmoothDeltas(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public Launcher() {
        super("TDF Platforms");
    }

    @Override
    public void initStatesList(GameContainer c) throws SlickException {
        addState(new Menu());
        addState(new LevelPicker());
    }

}

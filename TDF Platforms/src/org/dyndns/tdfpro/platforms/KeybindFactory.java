package org.dyndns.tdfpro.platforms;

import org.newdawn.slick.Input;

public class KeybindFactory {
    public static Keybind simple(final int keycode) {
        return new Keybind() {
            @Override
            public boolean check(Input input) {
                return input.isKeyDown(keycode);
            }
        };
    }

    public static Keybind multi(final int... keys) {
        return new Keybind() {
            @Override
            public boolean check(Input input) {
                for (int i = 0; i < keys.length; i++) {
                    if (input.isKeyDown(keys[i])) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Keybind modified(final int mod, final int key) {
        return new Keybind() {
            @Override
            public boolean check(Input input) {
                if (input.isKeyDown(mod)) {
                    return input.isKeyDown(key);
                }
                return false;
            }
        };
    }
}

interface Keybind {
    boolean check(Input input);
}
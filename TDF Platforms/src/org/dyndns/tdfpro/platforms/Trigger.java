package org.dyndns.tdfpro.platforms;

import java.util.ArrayList;
import java.util.Iterator;

public interface Trigger {
    public void trigger();
}

class Timer {
    int delay;
    Trigger end;

    public Timer(int delay, Trigger trigger) {
        this.delay = delay;
        end = trigger;
    }

    public boolean update(int delta) {
        if ((delay -= delta) <= 0) {
            end.trigger();
            return true;
        }
        return false;
    }
}

class Timers {
    private ArrayList<Timer> timers = new ArrayList<Timer>();

    public boolean addTimer(Timer timer) {
        return timers.add(timer);
    }

    public void update(int delta) {
        Iterator<Timer> it = timers.iterator();
        while (it.hasNext()) {
            if (it.next().update(delta)) {
                it.remove();
            }
        }
    }
}
/*
 * Copyright (c) 2016. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.map;

import net.vulcan.api.server.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ethan on 9/30/2016.
 */
public class SkyObjective extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Team offense;

    public SkyObjective(Team offense) {
        this.offense = offense;
    }

    public Team getOffense() {
        return offense;
    }
}

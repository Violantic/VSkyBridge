/*
 * Copyright (c) 2016. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.map;

import net.vulcan.api.server.Map;
import net.vulcan.api.server.Team;
import net.vulcan.api.server.state.Game;

import java.util.List;

/**
 * Created by Ethan on 11/2/2016.
 */
public class SkyGame extends Game {

    public SkyGame(String name, String[] description, Map map, List<Team> teams) {
        super(name, description, map, teams);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

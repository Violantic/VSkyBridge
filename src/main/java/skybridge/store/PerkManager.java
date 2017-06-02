/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.store;

import skybridge.SkyBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 1/19/2017.
 */
public class PerkManager {

    private SkyBridge instance;
    private List<Perk> perks;
    private Map<UUID, Perk> selected;

    public PerkManager(SkyBridge instance) {
        this.instance = instance;
        this.perks = new ArrayList<Perk>();
        this.selected = new ConcurrentHashMap<UUID, Perk>();
    }

    public SkyBridge getInstance() {
        return instance;
    }

    /**
     * Perk storage
     * @return
     */
    public List<Perk> getPerks() {
        return perks;
    }

    /**
     * Selected perks for players
     * @return
     */
    public Map<UUID, Perk> getSelected() {
        return selected;
    }

    /**
     * Register a perk for use
     * @param p
     */
    public void register(Perk p) {
        perks.add(p);
    }

    /**
     * Find a perk by name
     * @param s
     * @return
     */
    public Perk getPerk(String s) {
        for(Perk p : getPerks()) {
            if(p.getName().toLowerCase().equalsIgnoreCase(s.toLowerCase())) {
                return p;
            }
        }
        return null;
    }


    /**
     * Starts all perks in beginning of game
     */
    public void invokePerks() {

    }
}

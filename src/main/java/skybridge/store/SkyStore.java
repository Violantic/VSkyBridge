/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.store;

import skybridge.SkyBridge;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 1/19/2017.
 */
public class SkyStore {

    private SkyBridge instance;
    private Map<Perk, Integer> perks;

    public SkyStore(SkyBridge instance) {
        this.instance = instance;
        perks = new ConcurrentHashMap<Perk, Integer>();
    }

    public SkyBridge getInstance() {
        return instance;
    }

    public void registerAll() {
        for(Perk p : getInstance().getPerks().getPerks()) {
            perks.put(p, p.getCost());
        }
    }

}

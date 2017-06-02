/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.store;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Ethan on 1/19/2017.
 */
public abstract class Perk {

    private String name;
    private int cost;
    private ItemStack item;

    public Perk(String name, int cost, ItemStack item) {
        this.name = name;
        this.cost = cost;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public ItemStack getItem() {
        return item;
    }

    public abstract Runnable getListener();
}

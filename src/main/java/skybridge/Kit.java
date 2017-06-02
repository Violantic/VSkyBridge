/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 11/2/2016.
 */
public class Kit {
    private String id;
    private String name;
    private String[] description;
    private List<ItemStack> items;

    public Kit(String id, String name, String[] description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = new ArrayList<ItemStack>();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String[] getDescription() {
        return this.description;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void addItem(ItemStack itemStack) {
        this.getItems().add(itemStack);
    }
}

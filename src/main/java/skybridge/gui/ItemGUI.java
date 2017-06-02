/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skybridge.SkyBridge;

import java.util.Arrays;

/**
 * Created by Ethan on 9/5/2016.
 */
public abstract class ItemGUI {

    private ItemMenu itemMenu;

    private SkyBridge instance;

    public ItemGUI(SkyBridge instance, Player player) {
        this(instance, null, player, 36);
    }

    public SkyBridge getInstance() {
        return instance;
    }

    public ItemGUI(SkyBridge instance, ItemGUI parent, Player p, int size) {
        if (parent == null) {
            itemMenu = new ItemMenu(instance, getName(), p, size);
        } else {
            itemMenu = new ItemMenu(instance, parent.getItemMenu(), getName(), p, size);
        }
        //this.onlineUser = user;
        itemMenu.setCloseOnClick(this.isCloseOnClick());
        this.instance = instance;
        this.registerItems();
    }

    public void setSize(int size) {
        itemMenu = new ItemMenu(instance, getName(), getPlayer(), size);
    }

    public void set(int slot, MenuItem item) {
        this.itemMenu.addItem(slot, item);
    }

    public void set(int x, int y, MenuItem item) {
        this.itemMenu.addItem(x, y, item);
    }

    public CustomIS createFast(Material m, int amount, String name, String... lore) {
        return this.createFast(m, amount, 0, name, lore);
    }

    public CustomIS createFast(Material m, int amount, int data, String name, String... lore) {
        ItemStack is = new ItemStack(m, amount, (short) data);
        ItemMeta meta = is.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }
        is.setItemMeta(meta);
        return new CustomIS(is);
    }

    public Player getPlayer() {
        return this.itemMenu.getPlayer();
    }

    public void refresh() {
        this.itemMenu.update();
    }

    public ItemMenu getItemMenu() {
        return this.itemMenu;
    }

    public void show() {
        this.itemMenu.show();
    }

    public void close() {
        this.itemMenu.close();
    }

    public abstract String getName();

    public abstract boolean isCloseOnClick();

    public abstract void registerItems();

    public void onClose(Runnable r) {
        this.itemMenu.onClose(r);
    }

}
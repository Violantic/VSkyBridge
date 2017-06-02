/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * Created by Ethan on 11/2/2016.
 */
public class ItemStackBuilder {

    private ItemStack item;
    private ItemMeta itemMeta;

    public ItemStackBuilder(Material mat) {
        this.item = new ItemStack(mat);
        this.itemMeta = this.item.getItemMeta();
    }

    public ItemStackBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemStackBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemStackBuilder setData(DyeColor color) {
        this.item.getData().setData(color.getData());
        return this;
    }

    public ItemStackBuilder addEnchant(Enchantment e, int level) {
        this.itemMeta.addEnchant(e, level, true);
        return this;
    }

    public ItemStackBuilder removeEnchant(Enchantment e) {
        this.itemMeta.removeEnchant(e);
        return this;
    }

    public ItemStackBuilder addItemFlags(ItemFlag[] list) {
        this.itemMeta.addItemFlags(list);
        return this;
    }

    public ItemStackBuilder removeItemFlags(ItemFlag[] list) {
        this.itemMeta.removeItemFlags(list);
        return this;
    }

    public ItemStackBuilder setName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemStackBuilder setLore(List<String> list) {
        this.itemMeta.setLore(list);
        return this;
    }

    public ItemStackBuilder setColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta)this.itemMeta;
        meta.setColor(color);
        return this;
    }

    public ItemStackBuilder setSkull(String name) {
        SkullMeta meta = (SkullMeta)this.itemMeta;
        meta.setOwner(name);
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.itemMeta);
        return this.item;
    }

    public String toString() {
        return "Type=" + this.item.getType().name() + ", Amount=" + this.item.getAmount() + ", Durability=" + this.item.getDurability() + ", Enchantments=" + this.itemMeta.getEnchants() + ", ItemFlags=" + this.itemMeta.getItemFlags() + ", Name=" + this.itemMeta.getDisplayName() + ", Lore=" + this.itemMeta.getLore();
    }

}

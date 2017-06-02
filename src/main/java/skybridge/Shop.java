/*
 * Copyright (c) 2016. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan on 9/29/2016.
 */
public class Shop implements Listener {

    private String name;
    private Map<ItemStack, Integer> shopInventory;
    private List<ItemStack> shopItems;
    private int size;

    public Shop(String name, Map<ItemStack, Integer> shopInventory, int size) {
        this.name = name;
        this.shopInventory = shopInventory;
        this.shopItems = new ArrayList<ItemStack>();
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemStack> getShopItems() {
        return shopItems;
    }

    public Map<ItemStack, Integer> getShopInventory() {
        return shopInventory;
    }

    public void setShopInventory(Map<ItemStack, Integer> shopInventory) {
        this.shopInventory = shopInventory;
    }

    public Inventory getGUI() {
        Inventory inventory = Bukkit.createInventory(null, size, getName());
        for(ItemStack itemStack : getShopItems()) {
            inventory.addItem(itemStack);
        }

        return inventory;
    }

    public void purchase(Player player, ItemStack itemStack) {
        int count = 0;

        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.getType() == Material.ENDER_STONE) {
                count += stack.getAmount();
            }
        }

        player.closeInventory();
        if(count >= getShopInventory().get(itemStack)) {
            for(int i = 0; i < player.getInventory().getSize(); i++){
                ItemStack itm = player.getInventory().getItem(i);
                if(itm != null && itm.getType().equals(Material.ENDER_STONE)){
                    int amt = itm.getAmount() - getShopInventory().get(itemStack);
                    itm.setAmount(amt);
                    player.getInventory().setItem(i, amt > 0 ? itm : null);
                    player.updateInventory();
                    break;
                }
            }

            player.getInventory().addItem(itemStack);
            player.sendMessage(SkyBridge.getInstance().getPrefix() + ChatColor.GRAY + "You have purchased " + itemStack.getItemMeta().getDisplayName());
        } else {
            player.sendMessage(SkyBridge.getInstance().getPrefix() + ChatColor.GRAY + "You don't have enough " + ChatColor.YELLOW + "Materials " + ChatColor.GRAY + "to buy that!");
        }
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getInventory().getName().equalsIgnoreCase(getName())) return;

        event.setCancelled(true);
        if(getShopInventory().containsKey(event.getCurrentItem())) {
            purchase((Player) event.getWhoClicked(), event.getCurrentItem());
        }
    }

}

/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.gui.custom;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import skybridge.SkyBridge;
import skybridge.gui.CustomIS;
import skybridge.gui.ItemGUI;
import skybridge.gui.MenuItem;

import java.util.List;

/**
 * Created by Ethan on 1/19/2017.
 */
public class PerksGUI extends ItemGUI {

    public PerksGUI(SkyBridge instance, Player player) {
        super(instance, null, player, 54);
    }

    @Override
    public String getName() {
        return "&8Skybridge Shop".replace("&", ChatColor.COLOR_CHAR + "");
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        final List<String> perks = getInstance().getDb().getPerks(getPlayer().getUniqueId().toString());
        int i = 0;
        int p = 0;
        for(int x = 0; x < 6; x++) {
            for(int y = 0; y < 9; y++) {
                // 1st index of an item = [1][3] //
                if(matrix()[x][y] == 1) {
                    if(perks.size() > p) {
                        final int finalP = p;
                        set(i, new MenuItem(new CustomIS(getInstance().getPerks().getPerks().get(p).getItem()).addLore("&b&l*&r &7You purchased this on ".replace("&", ChatColor.COLOR_CHAR + "")), new Runnable() {
                            @Override
                            public void run() {
                                if(!getInstance().getCurrentState().getName().equalsIgnoreCase("lobby")) {
                                    getPlayer().sendMessage("&c&l(!) &r&cYou cannot switch perks in the middle of a game".replace("&", ChatColor.COLOR_CHAR + ""));
                                    return;
                                }

                                getInstance().getPerks().getSelected().put(getPlayer().getUniqueId(), getInstance().getPerks().getPerk(perks.get(finalP)));
                                getPlayer().sendMessage("&e&l(!) &r&eYou have selected the &n%perk%&r&e as your perk".replace("&", ChatColor.COLOR_CHAR + "").replace("%perk%", getInstance().getPerks().getPerk(perks.get(finalP)).getName()));
                            }
                        }));
                    } else {
                        final int finalP1 = p;
                        set(i, new MenuItem(new CustomIS(getInstance().getPerks().getPerks().get(p).getItem()).setMaterial(Material.INK_SACK).setName(getInstance().getPerks().getPerks().get(p).getName()).addLore(ChatColor.RED + "You have not purchased this yet!"), new Runnable() {
                            @Override
                            public void run() {
                                int balance = getInstance().getDb().getStats(getPlayer().getUniqueId().toString()).get("coins");
                                int cost = getInstance().getPerks().getPerks().get(finalP1).getCost();
                                if((balance - cost) < 0) {
                                    getPlayer().sendMessage(("&c&l(!) &r&cYou need &n" + (cost - getInstance().getDb().getStats(getPlayer().getUniqueId().toString()).get("coins")) + "&r&c more coins to buy the &n" + getInstance().getPerks().getPerks().get(finalP1).getName() + "&r&c perk!").replace("&", ChatColor.COLOR_CHAR + ""));
                                    close();
                                    return;
                                }

                                getInstance().getDb().setStat(getPlayer().getUniqueId().toString(), "coins", (balance - cost));
                                getInstance().getDb().purchasePerk(getPlayer().getUniqueId().toString(), getInstance().getPerks().getPerks().get(finalP1).getName());
                                getPlayer().sendMessage(("&a&l(!) &r&aYou have purchased the &n" + getInstance().getPerks().getPerks().get(finalP1).getName() + " &r&aperk!").replace("&", ChatColor.COLOR_CHAR + ""));
                                getPlayer().sendMessage(("&e&l(!) &r&eYour balance is now &n" + (balance-cost)).replace("&", ChatColor.COLOR_CHAR + ""));
                                close();
                            }
                        }));
                    }
                    p++;
                } else if(matrix()[x][y] == 2) {
                    set(i, new MenuItem(new CustomIS().setMaterial(Material.COAL_BLOCK).setName("&e&l(!) &r&eComing Soon!".replace("&", ChatColor.COLOR_CHAR + "")), new Runnable() {
                        @Override
                        public void run() {
                            getPlayer().sendMessage("&e&l(!) &r&eThis perk is coming soon!".replace("&", ChatColor.COLOR_CHAR + ""));
                        }
                    }));
                } else if(matrix()[x][y] == 3) {
                    set(i, new MenuItem(new CustomIS(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) DyeColor.LIME.getData())), new Runnable(){
                        @Override
                        public void run() {

                        }
                    }));
                } else {
                    set(i, new MenuItem(new CustomIS(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7)), new Runnable(){
                        @Override
                        public void run() {

                        }
                    }));
                }

                i++;
            }
        }
    }

    public int[][] matrix() {
        return new int[][]{
                {3,3,0,0,0,0,0,3,3},
                {3,0,0,0,0,0,0,0,3},
                {0,1,1,1,2,2,2,2,0},
                {0,2,2,2,0,0,0,0,0},
                {3,0,0,0,0,0,0,0,3},
                {3,3,0,0,0,0,0,3,3}
        };
    }
}

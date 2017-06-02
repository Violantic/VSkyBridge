/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import skybridge.gui.CustomIS;
import skybridge.store.Perk;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Ethan on 1/22/2017.
 */
public class PerksUtil {

    private SkyBridge instance;

    public PerksUtil(SkyBridge instance) {
        this.instance = instance;
    }

    public SkyBridge getInstance() {
        return instance;
    }

    public void registerPerks() {
        getInstance().getPerks().register(new Perk("Aquaman", 1000, new CustomIS().setMaterial(Material.WATER_BUCKET).setSize(1).setName("Aquaman").get()) {
            @Override
            public Runnable getListener() {
                return new Runnable() {
                    @Override
                    public void run() {
                        getInstance().getServer().getOnlinePlayers().stream().filter(new Predicate<Player>() {
                            @Override
                            public boolean test(Player player) {
                                return player.getLocation().getBlock().isLiquid();
                            }
                        }).forEach(new Consumer<Player>() {
                            @Override
                            public void accept(Player player) {
                                if (getInstance().getPerks().getSelected().get(player.getUniqueId()).getName().equalsIgnoreCase("Aquaman")) {
                                    if (player.getLocation().getBlock().getType().equals(Material.WATER)) {
                                        if (player.getHealth() == player.getMaxHealth()) {
                                            return;
                                        }
                                        player.setHealth(player.getHealth() + 0.5D);
                                        player.sendMessage("&e&l(!) &r&eYou are healed by &nWater&r &7(Aquaman Perk)".replace("&", ChatColor.COLOR_CHAR + ""));
                                    }
                                }
                            }
                        });
                    }
                };
            }
        });

        getInstance().getPerks().register(new Perk("Claws", 1500, new CustomIS().setMaterial(Material.BONE).setSize(1).setName("Claws").get()) {
            @Override
            public Runnable getListener() {
                return new Runnable() {
                    boolean done = false;
                    @Override
                    public void run() {
                        if(done) return;
                        getInstance().getPerks().getSelected().keySet().stream().filter(new Predicate<UUID>() {
                            @Override
                            public boolean test(UUID uuid) {
                                return getInstance().getPerks().getSelected().get(uuid).getName().equalsIgnoreCase("Claws");
                            }
                        }).forEach(new Consumer<UUID>() {
                            @Override
                            public void accept(UUID uuid) {
                                getInstance().getServer().getPlayer(uuid).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 1));
                                getInstance().getServer().getPlayer(uuid).sendMessage("&e&l(!) &r&eYour &nClaws&r&e perk has been activated &7(Claws Perk)".replace("&", ChatColor.COLOR_CHAR + ""));
                            }
                        });
                        done = true;
                    }
                };
            }
        });

        getInstance().getPerks().register(new Perk("Elementalist", 1200, new CustomIS().setMaterial(Material.LAVA_BUCKET).setSize(1).setName("Elementalist").get()) {
            @Override
            public Runnable getListener() {
                return new Runnable() {
                    boolean done = false;
                    @Override
                    public void run() {
                        if(done) return;
                        getInstance().getPerks().getSelected().keySet().stream().filter(new Predicate<UUID>() {
                            @Override
                            public boolean test(UUID uuid) {
                                return getInstance().getPerks().getSelected().get(uuid).getName().equalsIgnoreCase("Elementalist");
                            }
                        }).forEach(new Consumer<UUID>() {
                            @Override
                            public void accept(UUID uuid) {
                                if (!getInstance().getElementalist().contains(uuid)) {
                                    getInstance().getElementalist().add(uuid);
                                }

                                if (getInstance().getServer().getPlayer(uuid).getLocation().getBlock().isLiquid()) {
                                    if (getInstance().getServer().getPlayer(uuid).getLocation().getBlock().getType().equals(Material.WATER)) {
                                        getInstance().getServer().getPlayer(uuid).sendMessage("&e&l(!) &r&eYou are hurt by &nWater&r &7(Elementalist Perk)".replace("&", ChatColor.COLOR_CHAR + ""));
                                    }
                                }
                            }
                        });
                    }
                };
            }
        });
    }
}

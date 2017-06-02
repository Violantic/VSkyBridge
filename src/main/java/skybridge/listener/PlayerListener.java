/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import skybridge.MechanicsUtil;
import skybridge.SkyBridge;
import skybridge.gui.custom.PerksGUI;

/**
 * Created by Ethan on 1/22/2017.
 */
public class PlayerListener implements Listener {

    private SkyBridge instance;

    public PlayerListener(SkyBridge instance) {
        this.instance = instance;
    }

    public SkyBridge getInstance() {
        return instance;
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        event.getPlayer().teleport(getInstance().getLocation("soto", getInstance().getConfig().getString("spawn")));
        event.getPlayer().getInventory().clear();

        getInstance().getDb().registerUser(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());

        new BukkitRunnable() {
            public void run() {
                MechanicsUtil.apply(event.getPlayer());
            }
        }.runTaskLater(getInstance(), 20L);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setKeepInventory(true);

        if (event.getEntity().getKiller() != null) {
            Bukkit.broadcastMessage(getInstance().getPrefix() + event.getEntity().getName() + " was killed by " + event.getEntity().getKiller().getName());

            event.getEntity().getKiller().sendMessage("&a&l(!) &r&aYou have earned &n5&r&a coins for killing &n%target%".replace("&", ChatColor.COLOR_CHAR + "").replace("%target%", event.getEntity().getName()));
        }

        event.getEntity().setHealth(20);
        event.getEntity().teleport(getInstance().getLocation(getInstance().getConfig().getString("spawn")));
        final Location spawn = getInstance().getTeam(event.getEntity()).getSpawn();

        new BukkitRunnable() {
            public void run() {
                event.getEntity().teleport(spawn);
            }
        }.runTaskLater(getInstance(), 20 * 5);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {

            if (getInstance().getBossHealth().containsKey(event.getRightClicked().getName())) {
                event.setCancelled(true);
                return;
            }

            if (event.getRightClicked().getCustomName().startsWith(ChatColor.GREEN + "")) {
                event.getPlayer().openInventory(getInstance().getOpShop().getGUI());
            } else {
                event.getPlayer().openInventory(getInstance().getGameShop().getGUI());
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        if (getInstance().getTeams().get(0).getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().getLocation().distanceSquared(getInstance().red.getLocation()) <= 5.0) {
                event.setCancelled(true);
            }
        } else if (getInstance().getTeams().get(1).getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().getLocation().distanceSquared(getInstance().blue.getLocation()) <= 5.0) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (getInstance().getTeams().get(0).getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().getLocation().distanceSquared(getInstance().red.getLocation()) <= 5.0) {
                event.setCancelled(true);
            }
        } else if (getInstance().getTeams().get(1).getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().getLocation().distanceSquared(getInstance().blue.getLocation()) <= 5.0) {
                event.setCancelled(true);
            }
        } else if (getInstance().getTeams().get(2).getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().getLocation().distanceSquared(getInstance().yellow.getLocation()) <= 5.0) {
                event.setCancelled(true);
            }
        } else if (getInstance().getTeams().get(3).getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().getLocation().distanceSquared(getInstance().green.getLocation()) <= 5.0) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Villager) {
            if (!getInstance().getBossHealth().containsKey(event.getEntity().getName())) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(true);
            String name = event.getEntity().getCustomName();
            if (name.contains(ChatColor.RED + "")) {
                if (getInstance().getCheck().get(event.getDamager().getUniqueId()).equalsIgnoreCase("Red")) {
                    event.getDamager().sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You cannot attack your own boss!");
                } else {
                    getInstance().getBossHealth().put(event.getEntity().getName(), getInstance().getBossHealth().get(event.getEntity().getName()) - ((Math.round(event.getFinalDamage()) / 200.0) * 100.0));
                    event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    if (getInstance().getBossHealth().get(event.getEntity().getName()) <= 0) {
                        event.getEntity().remove();
                        MechanicsUtil.knockout("Red");
                    }
                }
                event.setCancelled(true);
            } else if (name.contains(ChatColor.BLUE + "")) {
                if (getInstance().getCheck().get(event.getDamager().getUniqueId()).equalsIgnoreCase("Blue")) {
                    event.getDamager().sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You cannot attack your own boss!");
                } else {
                    getInstance().getBossHealth().put(event.getEntity().getName(), getInstance().getBossHealth().get(event.getEntity().getName()) - ((Math.round(event.getFinalDamage()) / 200.0) * 100.0));
                    event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    if (getInstance().getBossHealth().get(event.getEntity().getName()) <= 0) {
                        event.getEntity().remove();
                        MechanicsUtil.knockout("Blue");
                    }
                }
                event.setCancelled(true);
            } else if (name.contains(ChatColor.YELLOW + "")) {
                if (getInstance().getCheck().get(event.getDamager().getUniqueId()).equalsIgnoreCase("Yellow")) {
                    event.getDamager().sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You cannot attack your own boss!");
                } else {
                    getInstance().getBossHealth().put(event.getEntity().getName(), getInstance().getBossHealth().get(event.getEntity().getName()) - ((Math.round(event.getFinalDamage()) / 200.0) * 100.0));
                    event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    if (getInstance().getBossHealth().get(event.getEntity().getName()) <= 0) {
                        event.getEntity().remove();
                        MechanicsUtil.knockout("Yellow");
                    }
                }
                event.setCancelled(true);
            } else if (name.contains(ChatColor.GREEN + "")) {
                if (getInstance().getCheck().get(event.getDamager().getUniqueId()).equalsIgnoreCase("Green")) {
                    event.getDamager().sendMessage(getInstance().getPrefix() + ChatColor.GRAY + "You cannot attack your own boss!");
                } else {
                    getInstance().getBossHealth().put(event.getEntity().getName(), getInstance().getBossHealth().get(event.getEntity().getName()) - ((Math.round(event.getFinalDamage()) / 200.0) * 100.0));
                    event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    if (getInstance().getBossHealth().get(event.getEntity().getName()) <= 0) {
                        event.getEntity().remove();
                        MechanicsUtil.knockout("Green");
                    }
                }
                event.setCancelled(true);
            } else if (name.contains(ChatColor.GREEN + "")) {
                event.setCancelled(true);
            }
        } else if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player target = (Player) event.getEntity();
            if ((getInstance().getTeams().get(0).getPlayers().contains(target.getUniqueId()) && getInstance().getTeams().get(0).getPlayers().contains(event.getDamager().getUniqueId()))) {
                event.setCancelled(true);
            } else if ((getInstance().getTeams().get(1).getPlayers().contains(target.getUniqueId()) && getInstance().getTeams().get(1).getPlayers().contains(event.getDamager().getUniqueId()))) {
                event.setCancelled(true);
            } else if ((getInstance().getTeams().get(2).getPlayers().contains(target.getUniqueId()) && getInstance().getTeams().get(2).getPlayers().contains(event.getDamager().getUniqueId()))) {
                event.setCancelled(true);
            } else if ((getInstance().getTeams().get(3).getPlayers().contains(target.getUniqueId()) && getInstance().getTeams().get(3).getPlayers().contains(event.getDamager().getUniqueId()))) {
                event.setCancelled(true);
            }
        } else if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Villager) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!getInstance().getCurrentState().getName().equalsIgnoreCase("lobby")) return;
        if (event.getPlayer().isSneaking()) {
            new PerksGUI(getInstance(), event.getPlayer()).show();
        }
    }

}


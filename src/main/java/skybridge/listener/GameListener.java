/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.listener;

import net.vulcan.core.event.GameEndEvent;
import net.vulcan.core.event.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import skybridge.MechanicsUtil;
import skybridge.SkyBridge;

import java.util.UUID;

/**
 * Created by Ethan on 1/22/2017.
 */
public class GameListener implements Listener {

    private SkyBridge instance;

    public GameListener(SkyBridge instance) {
        this.instance = instance;
    }

    public SkyBridge getInstance() {
        return instance;
    }

    @EventHandler
    public void onStart(GameStartEvent event) {
        Bukkit.broadcastMessage(getInstance().getPrefix() + ChatColor.GRAY + "Shops enabling in " + ChatColor.YELLOW + "5" + ChatColor.GRAY + " seconds...");
        new BukkitRunnable() {
            public void run() {
                MechanicsUtil.startPVP();
                Bukkit.broadcastMessage(getInstance().getPrefix() + ChatColor.GRAY + "PVP is now " + ChatColor.YELLOW + "enabled");
            }
        }.runTaskLater(getInstance(), 20 * 5);
    }

    @EventHandler
    public void onEnd(GameEndEvent event) {
        for(UUID uuid : getInstance().find(event.getWinner()).getPlayers()) {
            Player player = getInstance().getServer().getPlayer(uuid);
            player.sendMessage("&a&l(!) &r&aYou have earned &n25&r&a coins for winning the game".replace("&", ChatColor.COLOR_CHAR + ""));
        }
        new BukkitRunnable() {
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            }
        }.runTaskLater(getInstance(), 20 * 10);
    }
}

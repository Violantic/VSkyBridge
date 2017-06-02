/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge;

import net.vulcan.api.server.Team;
import net.vulcan.api.server.state.GameState;
import net.vulcan.core.event.GameEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 1/22/2017.
 */
public class MechanicsUtil {

    public static void knockout(String team) {
        Team t = SkyBridge.getInstance().find(team);
        SkyBridge.getInstance().getTeams().remove(t);

        for (UUID uuid : t.getPlayers()) {
            SkyBridge.getInstance().getServer().getPlayer(uuid).setGameMode(GameMode.SPECTATOR);
        }

        SkyBridge.getInstance().getServer().broadcastMessage(SkyBridge.getInstance().getPrefix() + t.getColor() + team + "" + ChatColor.YELLOW + " has been ELIMINATED!");

        if (SkyBridge.getInstance().getTeams().size() == 1) {
            Bukkit.getPluginManager().callEvent(new GameEndEvent(SkyBridge.getInstance().getTeams().get(0).getColor() + SkyBridge.getInstance().getTeams().get(0).getName()));
        }
    }

    public static void apply(final Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("GameSB", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam("team1");
        final org.bukkit.scoreboard.Team team2 = scoreboard.registerNewTeam("team2");
        final org.bukkit.scoreboard.Team team3 = scoreboard.registerNewTeam("team3");
        final org.bukkit.scoreboard.Team team4 = scoreboard.registerNewTeam("team4");
        final org.bukkit.scoreboard.Team team5 = scoreboard.registerNewTeam("team5");
        final org.bukkit.scoreboard.Team team6 = scoreboard.registerNewTeam("team6");
        final org.bukkit.scoreboard.Team team7 = scoreboard.registerNewTeam("team7");
        final org.bukkit.scoreboard.Team team8 = scoreboard.registerNewTeam("team8");
        final org.bukkit.scoreboard.Team team9 = scoreboard.registerNewTeam("team9");
        final org.bukkit.scoreboard.Team team10 = scoreboard.registerNewTeam("team10");
        final org.bukkit.scoreboard.Team team11 = scoreboard.registerNewTeam("team11");
        final org.bukkit.scoreboard.Team team12 = scoreboard.registerNewTeam("team12");
        final org.bukkit.scoreboard.Team team13 = scoreboard.registerNewTeam("team13");


        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.AQUA.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.STRIKETHROUGH.toString()));
        team3.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLACK.toString()));
        team4.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        team5.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA.toString()));
        team6.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_PURPLE.toString()));
        team7.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN.toString()));
        team8.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_BLUE.toString()));
        team9.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY.toString()));
        team10.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        team11.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_RED.toString()));
        team12.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GREEN.toString()));
        team13.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GOLD.toString()));

        objective.getScore(ChatColor.AQUA.toString()).setScore(13);
        objective.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(12);
        objective.getScore(ChatColor.BLACK.toString()).setScore(11);
        objective.getScore(ChatColor.BLUE.toString()).setScore(10);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(9);
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(8);
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(7);
        objective.getScore(ChatColor.DARK_BLUE.toString()).setScore(6);
        objective.getScore(ChatColor.DARK_GRAY.toString()).setScore(5);
        objective.getScore(ChatColor.RED.toString()).setScore(4);
        objective.getScore(ChatColor.DARK_RED.toString()).setScore(3);
        objective.getScore(ChatColor.GREEN.toString()).setScore(2);
        objective.getScore(ChatColor.GOLD.toString()).setScore(1);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!SkyBridge.getInstance().getCurrentState().getName().equalsIgnoreCase("lobby")) {
                    cancel();
                    return;
                }

                // Not going to update because it's only lobby. //
                Map<String, Integer> stats = SkyBridge.getInstance().getDb().getStats(player.getUniqueId().toString());

                objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "SKYBRIDGE");
                team.setPrefix(ChatColor.GRAY + "(Stats)");
                team2.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "Coins");
                team3.setPrefix(ChatColor.GRAY + "" + stats.get("coins"));
                team4.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "Points");
                team5.setPrefix(ChatColor.GRAY + "" + stats.get("points"));
                team6.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "Kills");
                team7.setPrefix(ChatColor.GRAY + "" + stats.get("kills"));
                team8.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "Deaths");
                team9.setPrefix(ChatColor.GRAY + "" + stats.get("deaths"));
                team10.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "Won");
                team11.setPrefix(ChatColor.GRAY + "" + stats.get("won"));
                team12.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "Played");
                team13.setPrefix(ChatColor.GRAY + "" + stats.get("played"));
            }
        }.runTaskTimer(SkyBridge.getInstance(), 0L, 20L);

        player.setScoreboard(scoreboard);
    }

    public static void startObjective() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        final org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        final Objective objective = scoreboard.registerNewObjective("GameSB", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam("team1");
        final org.bukkit.scoreboard.Team team2 = scoreboard.registerNewTeam("team2");
        final org.bukkit.scoreboard.Team team3 = scoreboard.registerNewTeam("team3");
        final org.bukkit.scoreboard.Team team4 = scoreboard.registerNewTeam("team4");
        final org.bukkit.scoreboard.Team team5 = scoreboard.registerNewTeam("team5");
        final org.bukkit.scoreboard.Team team6 = scoreboard.registerNewTeam("team6");
        final org.bukkit.scoreboard.Team team7 = scoreboard.registerNewTeam("team7");
        final org.bukkit.scoreboard.Team team8 = scoreboard.registerNewTeam("team8");

        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.AQUA.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.STRIKETHROUGH.toString()));
        team3.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLACK.toString()));
        team4.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        team5.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA.toString()));
        team6.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_PURPLE.toString()));
        team7.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN.toString()));
        team8.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY.toString()));

        objective.getScore(ChatColor.AQUA.toString()).setScore(8);
        objective.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(7);
        objective.getScore(ChatColor.BLACK.toString()).setScore(6);
        objective.getScore(ChatColor.BLUE.toString()).setScore(5);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(4);
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(3);
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(2);
        objective.getScore(ChatColor.DARK_GRAY.toString()).setScore(1);

        new BukkitRunnable() {
            public void run() {
                objective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "SKYBRIDGE");
                team.setPrefix("");
                team2.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + "R: " + ChatColor.GRAY + ((SkyBridge.getInstance().getBossHealth().get(SkyBridge.getInstance().red.getName()) / 20.0) * 10.0) + "%");
                team3.setPrefix("");
                team4.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + "B: " + ChatColor.GRAY + ((SkyBridge.getInstance().getBossHealth().get(SkyBridge.getInstance().blue.getName()) / 20.0) * 10.0) + "%");
                team5.setPrefix("");
                team6.setPrefix(ChatColor.YELLOW + "" + ChatColor.BOLD + "Y: " + ChatColor.GRAY + ((SkyBridge.getInstance().getBossHealth().get(SkyBridge.getInstance().yellow.getName()) / 20.0) * 10.0) + "%");
                team7.setPrefix("");
                team8.setPrefix(ChatColor.GREEN + "" + ChatColor.BOLD + "G: " + ChatColor.GRAY + ((SkyBridge.getInstance().getBossHealth().get(SkyBridge.getInstance().green.getName()) / 20.0) * 10.0) + "%");
            }
        }.runTaskTimer(SkyBridge.getInstance(), 1L, 1L);

        for (Player player : SkyBridge.getInstance().getServer().getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public static void spawnNPC() {
        final Villager redShop = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getRedShop(), EntityType.VILLAGER);
        final Villager blueShop = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getBlueShop(), EntityType.VILLAGER);
        final Villager yellowShop = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getYellowShop(), EntityType.VILLAGER);
        final Villager greenShop = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getGreenShop(), EntityType.VILLAGER);
        final Villager opShop = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getLocation(SkyBridge.getInstance().getConfig().getString("opshop")), EntityType.VILLAGER);

        redShop.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        redShop.setCustomName(ChatColor.RED + "Shop");

        blueShop.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        blueShop.setCustomName(ChatColor.BLUE + "Shop");

        yellowShop.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        yellowShop.setCustomName(ChatColor.YELLOW + "Shop");

        greenShop.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        greenShop.setCustomName(ChatColor.GREEN + "Shop");

        opShop.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        opShop.setCustomName(ChatColor.GREEN + "Shop");

        final Villager redBoss = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getRed(), EntityType.VILLAGER);
        final Villager blueBoss = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getBlue(), EntityType.VILLAGER);
        final Villager yellowBoss = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getYellow(), EntityType.VILLAGER);
        final Villager greenBoss = (Villager) SkyBridge.getInstance().getGame().getMap().getWorld().spawnEntity(SkyBridge.getInstance().getGreen(), EntityType.VILLAGER);


        redBoss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        redBoss.setCustomName(ChatColor.RED + "" + "Boss");
        SkyBridge.getInstance().getBossHealth().put(redBoss.getName(), 200.0);
        SkyBridge.getInstance().red = redBoss;

        blueBoss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        blueBoss.setCustomName(ChatColor.BLUE + "" + "Boss");
        SkyBridge.getInstance().getBossHealth().put(blueBoss.getName(), 200.0);
        SkyBridge.getInstance().blue = blueBoss;

        yellowBoss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        yellowBoss.setCustomName(ChatColor.YELLOW + "" + "Boss");
        SkyBridge.getInstance().getBossHealth().put(yellowBoss.getName(), 200.0);
        SkyBridge.getInstance().yellow = yellowBoss;

        greenBoss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 100000, 127));
        greenBoss.setCustomName(ChatColor.GREEN + "" + "Boss");
        SkyBridge.getInstance().getBossHealth().put(greenBoss.getName(), 200.0);
        SkyBridge.getInstance().green = greenBoss;

        SkyBridge.getInstance().op = opShop;
    }

    public static void startPVP() {
        GameState pvp = new GameState("initiated");

        pvp.setCanAlterMap(true);
        pvp.setCanPVP(true);
        pvp.setCanPVE(false);

        SkyBridge.getInstance().setCurrentState(pvp);

        spawnNPC();
        startObjective();
        Bukkit.broadcastMessage(SkyBridge.getInstance().getPrefix() + ChatColor.YELLOW + "Shops " + ChatColor.GRAY + "have spawned in!");

        for (Player player : SkyBridge.getInstance().getServer().getOnlinePlayers()) {
            // Player never joined a team
            if (SkyBridge.getInstance().getGame().getTeams().get(0).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(1).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(0).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(2).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(0).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(3).getPlayers().size()) {
                SkyBridge.getInstance().getGame().getTeams().get(0).addPlayer(player.getUniqueId());
                SkyBridge.getInstance().getCheck().put(player.getUniqueId(), "Red");
                player.teleport(SkyBridge.getInstance().getRed());
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.updateInventory();
                player.setPlayerListName(ChatColor.RED + player.getName());
                for (ItemStack itemstack : SkyBridge.getInstance().getRedKit().getItems()) {
                    player.getInventory().addItem(itemstack);
                }
            } else if (SkyBridge.getInstance().getGame().getTeams().get(1).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(0).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(1).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(2).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(1).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(3).getPlayers().size()) {
                SkyBridge.getInstance().getGame().getTeams().get(1).addPlayer(player.getUniqueId());
                SkyBridge.getInstance().getCheck().put(player.getUniqueId(), "Blue");
                player.teleport(SkyBridge.getInstance().getBlue());
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.updateInventory();
                player.setPlayerListName(ChatColor.BLUE + player.getName());
                for (ItemStack itemstack : SkyBridge.getInstance().getBlueKit().getItems()) {
                    player.getInventory().addItem(itemstack);
                }
            } else if (SkyBridge.getInstance().getGame().getTeams().get(2).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(0).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(2).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(1).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(2).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(3).getPlayers().size()) {
                SkyBridge.getInstance().getGame().getTeams().get(2).addPlayer(player.getUniqueId());
                SkyBridge.getInstance().getCheck().put(player.getUniqueId(), "Yellow");
                player.teleport(SkyBridge.getInstance().getYellow());
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.updateInventory();
                player.setPlayerListName(ChatColor.YELLOW + player.getName());
                for (ItemStack itemstack : SkyBridge.getInstance().getYellowKit().getItems()) {
                    player.getInventory().addItem(itemstack);
                }
            } else if (SkyBridge.getInstance().getGame().getTeams().get(3).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(0).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(3).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(1).getPlayers().size()
                    || SkyBridge.getInstance().getGame().getTeams().get(3).getPlayers().size() <= SkyBridge.getInstance().getGame().getTeams().get(2).getPlayers().size()) {
                SkyBridge.getInstance().getGame().getTeams().get(2).addPlayer(player.getUniqueId());
                SkyBridge.getInstance().getCheck().put(player.getUniqueId(), "Green");
                player.teleport(SkyBridge.getInstance().getYellow());
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.updateInventory();
                player.setPlayerListName(ChatColor.GREEN + player.getName());
                for (ItemStack itemstack : SkyBridge.getInstance().getGreenKit().getItems()) {
                    player.getInventory().addItem(itemstack);
                }
            }

        }
    }

}

/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skybridge.SkyBridge;

import java.util.UUID;

/**
 * Created by Ethan on 1/24/2017.
 */
public class CoinsCommand implements CommandExecutor {

    private SkyBridge instance;

    public CoinsCommand(SkyBridge instance) {
        this.instance = instance;
    }

    public SkyBridge getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission("skybridge.admin")) return false;

        if(args.length <= 0) {
            commandSender.sendMessage("&e&l(!) &r&e/coins set|take|give <user> {amount}".replace("&", ChatColor.COLOR_CHAR + ""));
            commandSender.sendMessage("&e&l(!) &r&e/coins <user>".replace("&", ChatColor.COLOR_CHAR + ""));
        } else if(args.length == 3) {

            String who = getInstance().getDb().getUUID(args[1]);
            if(who == null) {
                commandSender.sendMessage("&c&l(!) &r&cThat player has never joined the server".replace("&", ChatColor.COLOR_CHAR + ""));
                return false;
            }

            int amt = Integer.parseInt(args[2]);
            Player tryTarget = getInstance().getServer().getPlayer(UUID.fromString(who));

            if(amt <= 0) {
                commandSender.sendMessage("&c&l(!) &r&cThe amount must be greater than 0".replace("&", ChatColor.COLOR_CHAR + ""));
                return false;
            }

            int previosAmount = 0;
            try {
                previosAmount = getInstance().getDb().getStats(who).get("coins");
            } catch (Exception e) {
                commandSender.sendMessage(ChatColor.RED + "[SKYBRIDGE] Could not fetch from database");
                return false;
            }

            if(args[0].equalsIgnoreCase("set")) {
                try {
                    getInstance().getDb().setStat(who, "coins", amt);
                    commandSender.sendMessage("&a&l(!) &r&aYou have set the coin balance of &n{0}&r&a to &n{1}".replace("&", ChatColor.COLOR_CHAR + "").replace("{0}", args[1]).replace("{1}", amt + ""));
                } catch (Exception e) {
                    commandSender.sendMessage("[SKYBRIDGE] Database error!");
                }
            } else if(args[0].equalsIgnoreCase("take")) {
                try {
                    int newAmount = (previosAmount - amt);
                    getInstance().getDb().setStat(who, "coins", newAmount);
                    commandSender.sendMessage("&a&l(!) &r&aYou have set the coin balance of &n{0}&r&a to &n{1}".replace("&", ChatColor.COLOR_CHAR + "").replace("{0}", args[1]).replace("{1}", newAmount + ""));
                } catch (Exception e) {
                    commandSender.sendMessage("[SKYBRIDGE] Database error!");
                }
            } else if(args[0].equalsIgnoreCase("give")) {
                try {
                    int newAmount = (previosAmount + amt);
                    getInstance().getDb().setStat(who, "coins", newAmount);
                    commandSender.sendMessage("&a&l(!) &r&aYou have set the coin balance of &n{0}&r&a to &n{1}".replace("&", ChatColor.COLOR_CHAR + "").replace("{0}", args[1]).replace("{1}", newAmount + ""));
                } catch (Exception e) {
                    commandSender.sendMessage("[SKYBRIDGE] Database error!");
                }
            }

            alertPlayer(tryTarget, "Your coins have been changed manually");
        }
        return false;
    }

    /**
     * Quietly
     * @param player
     * @param msg
     */
    private void alertPlayer(Player player, String msg) {
        if(player != null) {
            player.sendMessage(("&e&l(!) &r&e" + msg).replace("&", ChatColor.COLOR_CHAR + ""));
        }
    }
}

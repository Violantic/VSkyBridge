/*
 * Copyright (c) 2016. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package skybridge;

import net.vulcan.api.server.Team;
import net.vulcan.api.server.state.Game;
import net.vulcan.api.server.state.GameState;
import net.vulcan.api.server.type.ServerType;
import net.vulcan.core.VulcanCore;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import skybridge.command.CoinsCommand;
import skybridge.database.SkyDB;
import skybridge.listener.GameListener;
import skybridge.listener.PlayerListener;
import skybridge.map.SkyGame;
import skybridge.map.SkyMap;
import skybridge.store.PerkManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 9/29/2016.
 */
public class SkyBridge extends VulcanCore implements Listener {

    private static SkyBridge instance;

    public boolean doubleTime = false;
    public boolean enabled = true;
    private Shop gameShop;
    private Shop opShop;
    private Map<String, Integer> scoreMap = new HashMap<String, Integer>();
    private Map<UUID, String> check;
    private Map<String, Double> bossHealth;
    public Entity red, blue, yellow, green, op;

    private Game game;
    private SkyDB db;
    private PerkManager perks;
    private PerksUtil perksUtil;

    private List<Team> teams;
    private List<UUID> elementalist;

    @Override
    public void onEnable() {
        instance = this;
        game = new SkyGame("SkyBridge", new String[]{"Build your bridge across the sky!"
                , "Try to defend your own boss and base"
                , "Attempt to destroy the enemy boss!"}, new SkyMap(this, "soto", new String[]{"Micah_", "SecurityEthan"}), teams);

        game.setMinimumPlayers(3);

        check = new ConcurrentHashMap<UUID, String>();
        bossHealth = new ConcurrentHashMap<String, Double>();

        GameState lobby = new GameState("lobby");
        setCurrentState(lobby);

        getConfig().options().copyDefaults(getConfig().contains("center"));
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        scoreMap.put("Red", 1);
        scoreMap.put("Blue", 1);
        scoreMap.put("Yellow", 1);
        scoreMap.put("Green", 1);

        this.teams = new ArrayList<Team>();
        teams.add(new Team("Red", ChatColor.RED, new ItemStack(Material.REDSTONE_BLOCK, 1), getRed()));
        teams.add(new Team("Blue", ChatColor.BLUE, new ItemStack(Material.LAPIS_BLOCK, 1), getBlue()));
        teams.add(new Team("Yellow", ChatColor.YELLOW, new ItemStack(Material.HAY_BLOCK, 1), getYellow()));
        teams.add(new Team("Green", ChatColor.GREEN, new ItemStack(Material.CACTUS, 1), getGreen()));

        this.elementalist = new ArrayList<UUID>();

        this.game.setTeams(teams);
        this.db = new SkyDB(this, "localhost", "skybridge", "mcc", "root", "1024311");
        this.perks = new PerkManager(this);
        this.perksUtil = new PerksUtil(this);
        perksUtil.registerPerks();

        opShop = new Shop("OPShop", new HashMap<ItemStack, Integer>() {
            {
                put(new ItemStackBuilder(Material.DIAMOND_SWORD).setName("Diamond Sword").setLore(Arrays.asList("Cost: 35")).build(), 35);
                put(new ItemStackBuilder(Material.DIAMOND_SWORD).setName("Magic Diamond Sword").addEnchant(Enchantment.DAMAGE_ALL, 1).addEnchant(Enchantment.KNOCKBACK, 1).setLore(Arrays.asList("Cost: 100")).build(), 100);
                put(new ItemStackBuilder(Material.DIAMOND_PICKAXE).setName("Diamond Pickaxe").addEnchant(Enchantment.DIG_SPEED, 2).setLore(Arrays.asList("Cost: 60")).build(), 60);
                put(new ItemStackBuilder(Material.ARROW).setName("Arrow").setLore(Arrays.asList("Cost: 5")).setAmount(5).build(), 5);
                put(new ItemStackBuilder(Material.BOW).setName("Bow").addEnchant(Enchantment.ARROW_DAMAGE, 2).addEnchant(Enchantment.ARROW_KNOCKBACK, 2).setLore(Arrays.asList("Cost: 350")).build(), 350);
                put(new ItemStackBuilder(Material.DIAMOND_HELMET).setName("Diamond Helmet").setLore(Arrays.asList("Cost: 125")).build(), 125);
                put(new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).setName("Diamond Chestplate").setLore(Arrays.asList("Cost: 140")).build(), 140);
                put(new ItemStackBuilder(Material.DIAMOND_LEGGINGS).setName("Diamond Leggings").setLore(Arrays.asList("Cost: 130")).build(), 130);
                put(new ItemStackBuilder(Material.DIAMOND_BOOTS).setName("Diamond Boots").setLore(Arrays.asList("Cost: 125")).build(), 125);
                put(new ItemStackBuilder(Material.COOKED_BEEF).setAmount(3).setLore(Arrays.asList("Cost: 5")).build(), 5);
            }
        }, 18);

        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_SWORD).setName("Diamond Sword").setLore(Arrays.asList("Cost: 35")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_SWORD).setName("Magic Diamond Sword").addEnchant(Enchantment.DAMAGE_ALL, 1).addEnchant(Enchantment.KNOCKBACK, 1).setLore(Arrays.asList("Cost: 100")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_PICKAXE).setName("Diamond Pickaxe").addEnchant(Enchantment.DIG_SPEED, 2).setLore(Arrays.asList("Cost: 60")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.ARROW).setName("Arrow").setLore(Arrays.asList("Cost: 5")).setAmount(5).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.BOW).setName("Bow").addEnchant(Enchantment.ARROW_DAMAGE, 2).addEnchant(Enchantment.ARROW_KNOCKBACK, 2).setLore(Arrays.asList("Cost: 350")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_HELMET).setName("Diamond Helmet").setLore(Arrays.asList("Cost: 125")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).setName("Diamond Chestplate").setLore(Arrays.asList("Cost: 140")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_LEGGINGS).setName("Diamond Leggings").setLore(Arrays.asList("Cost: 130")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.DIAMOND_BOOTS).setName("Diamond Boots").setLore(Arrays.asList("Cost: 125")).build());
        opShop.getShopItems().add(new ItemStackBuilder(Material.COOKED_BEEF).setAmount(3).setLore(Arrays.asList("Cost: 5")).build());

        gameShop = new Shop("Shop", new HashMap<ItemStack, Integer>() {
            {
                put(new ItemStackBuilder(Material.IRON_SWORD).setName("Iron Sword").addEnchant(Enchantment.DAMAGE_ALL, 1).setLore(Arrays.asList("Cost: 35")).build(), 35);
                put(new ItemStackBuilder(Material.IRON_SWORD).setName("Magic Iron Sword").addEnchant(Enchantment.DAMAGE_ALL, 1).addEnchant(Enchantment.KNOCKBACK, 2).setLore(Arrays.asList("Cost: 100")).build(), 100);
                put(new ItemStackBuilder(Material.IRON_PICKAXE).setName("Iron Pickaxe").addEnchant(Enchantment.DIG_SPEED, 2).setLore(Arrays.asList("Cost: 15")).build(), 15);
                put(new ItemStackBuilder(Material.ARROW).setName("Arrow").setLore(Arrays.asList("Cost: 5")).setAmount(5).build(), 5);
                put(new ItemStackBuilder(Material.BOW).setName("Bow").addEnchant(Enchantment.ARROW_DAMAGE, 2).setLore(Arrays.asList("Cost: 20")).build(), 20);
                put(new ItemStackBuilder(Material.IRON_HELMET).setName("Iron Helmet").setLore(Arrays.asList("Cost: 25")).build(), 25);
                put(new ItemStackBuilder(Material.IRON_CHESTPLATE).setName("Iron Chestplate").setLore(Arrays.asList("Cost: 40")).build(), 40);
                put(new ItemStackBuilder(Material.IRON_LEGGINGS).setName("Iron Leggings").setLore(Arrays.asList("Cost: 30")).build(), 30);
                put(new ItemStackBuilder(Material.IRON_BOOTS).setName("Iron Boots").setLore(Arrays.asList("Cost: 25")).build(), 25);
                put(new ItemStackBuilder(Material.COOKED_BEEF).setAmount(3).setLore(Arrays.asList("Cost: 5")).build(), 5);
            }
        }, 18);

        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_SWORD).setName("Iron Sword").addEnchant(Enchantment.DAMAGE_ALL, 1).setLore(Arrays.asList("Cost: 35")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_SWORD).setName("Magic Iron Sword").addEnchant(Enchantment.DAMAGE_ALL, 1).addEnchant(Enchantment.KNOCKBACK, 2).setLore(Arrays.asList("Cost: 100")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_PICKAXE).setName("Iron Pickaxe").addEnchant(Enchantment.DIG_SPEED, 2).setLore(Arrays.asList("Cost: 15")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.ARROW).setName("Arrow").setLore(Arrays.asList("Cost: 5")).setAmount(5).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.BOW).setName("Bow").addEnchant(Enchantment.ARROW_DAMAGE, 2).setLore(Arrays.asList("Cost: 20")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_HELMET).setName("Iron Helmet").setLore(Arrays.asList("Cost: 25")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_CHESTPLATE).setName("Iron Chestplate").setLore(Arrays.asList("Cost: 40")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_LEGGINGS).setName("Iron Leggings").setLore(Arrays.asList("Cost: 30")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.IRON_BOOTS).setName("Iron Boots").setLore(Arrays.asList("Cost: 25")).build());
        gameShop.getShopItems().add(new ItemStackBuilder(Material.COOKED_BEEF).setAmount(3).setLore(Arrays.asList("Cost: 5")).build());

        getServer().getPluginManager().registerEvents(gameShop, this);
        getServer().getPluginManager().registerEvents(opShop, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        getCommand("coins").setExecutor(new CoinsCommand(this));

        getServer().getScheduler().runTaskTimer(this, new Runnable() {
            int second = 0;

            @Override
            public void run() {
                if (getServer().getOnlinePlayers().size() >= getGame().getMinimumPlayers() && !enabled) {
                    enabled = true;
                }

                if (enabled) {
                    second++;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setFoodLevel(20);
                }

                if (getCurrentState().getName().equalsIgnoreCase("initiated")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getInventory().addItem(new ItemStackBuilder(Material.ENDER_STONE).setName(ChatColor.YELLOW + "Sky Material").build());
                    }
                } else if (getCurrentState().getName().equalsIgnoreCase("double")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getInventory().addItem(new ItemStackBuilder(Material.ENDER_STONE).setAmount(2).setName(ChatColor.YELLOW + "Sky Material").build());
                    }
                }

                if (!getCurrentState().getName().equalsIgnoreCase("lobby")) {
                    for (Entity e : red.getNearbyEntities(3, 3, 3)) {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            if (teams.get(1).getPlayers().contains(p.getUniqueId())) {
                                p.damage(2D);
                                p.sendMessage(getPrefix() + ChatColor.GRAY + "You have been damaged by the " + ChatColor.RED + "RED " + ChatColor.GRAY + "boss!");
                            }
                        }
                    }

                    for (Entity e : blue.getNearbyEntities(3, 3, 3)) {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            if (teams.get(0).getPlayers().contains(p.getUniqueId())) {
                                p.damage(2D);
                                p.sendMessage(getPrefix() + ChatColor.GRAY + "You have been damaged by the " + ChatColor.BLUE + "BLUE " + ChatColor.GRAY + "boss!");
                            }
                        }
                    }
                }

                if (second == 30) {
                    Bukkit.broadcastMessage(getPrefix() + ChatColor.GRAY + "Starting game...");
                    MechanicsUtil.startPVP();
                }

                if (second == 300) {
                    startDouble();
                    Bukkit.broadcastMessage(getPrefix() + ChatColor.GRAY + "Material rations are now being " + ChatColor.YELLOW + "doubled" + ChatColor.GRAY + "!");
                }

                if (getCurrentState().getName().equalsIgnoreCase("initiated")) {
                    red.teleport(getRed());
                    blue.teleport(getBlue());
                }
            }
        }, 0L, 20L);

    }

    public SkyDB getDb() {
        return db;
    }

    public Map<String, Double> getBossHealth() {
        return bossHealth;
    }

    public Shop getOpShop() {
        return opShop;
    }

    public Shop getGameShop() {
        return gameShop;
    }

    public PerkManager getPerks() {
        return perks;
    }

    public Game getGame() {
        return game;
    }

    public static SkyBridge getInstance() {
        return instance;
    }

    public String getPrefix() {
        return ChatColor.YELLOW + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.YELLOW + "";
    }

    @Override
    public ServerType serverType() {
        return ServerType.SKYBRIDGE;
    }

    @Override
    public int id() {
        return getConfig().getInt("id");
    }

    public void startWait() {
        GameState waiting = new GameState("waiting");

        waiting.setCanAlterMap(false);
        waiting.setCanPVP(false);
        waiting.setCanPVE(false);

        setCurrentState(waiting);
    }

    public void startDouble() {
        GameState pvpdouble = new GameState("double");

        doubleTime = true;
        pvpdouble.setCanAlterMap(true);
        pvpdouble.setCanPVP(true);
        pvpdouble.setCanPVE(false);

        setCurrentState(pvpdouble);
    }

    public Location getLocation(String path) {
        String[] strings = path.split(",");
        double x = Double.parseDouble(strings[0]);
        double y = Double.parseDouble(strings[1]);
        double z = Double.parseDouble(strings[2]);

        return new Location(getGame().getMap().getWorld(), x, y, z);
    }

    public Location getLocation(String world, String path) {
        String[] strings = path.split(",");
        double x = Double.parseDouble(strings[0]);
        double y = Double.parseDouble(strings[1]);
        double z = Double.parseDouble(strings[2]);

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Location getRed() {
        return getLocation(getConfig().getString("red"));
    }

    public Location getRedShop() {
        return getLocation(getConfig().getString("redshop"));
    }

    public Location getBlue() {
        return getLocation(getConfig().getString("blue"));
    }

    public Location getBlueShop() {
        return getLocation(getConfig().getString("blueshop"));
    }

    public Location getYellow() {
        return getLocation(getConfig().getString("blue"));
    }

    public Location getYellowShop() {
        return getLocation(getConfig().getString("yellowshop"));
    }

    public Location getGreen() {
        return getLocation(getConfig().getString("green"));
    }

    public Location getGreenShop() {
        return getLocation(getConfig().getString("greenshop"));
    }

    public Kit getRedKit() {
        Kit kit = new Kit("red", "Starter", new String[]{"Basic pvp gear"});
        kit.addItem(new ItemStackBuilder(Material.LEATHER_HELMET).setColor(Color.RED).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setColor(Color.RED).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_LEGGINGS).setColor(Color.RED).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_BOOTS).setColor(Color.RED).build());
        kit.addItem(new ItemStackBuilder(Material.STONE_SWORD).build());
        kit.addItem(new ItemStackBuilder(Material.BOW).build());
        kit.addItem(new ItemStackBuilder(Material.ARROW).setAmount(24).build());

        return kit;
    }

    public Kit getBlueKit() {
        Kit kit = new Kit("blue", "Starter", new String[]{"Basic pvp gear"});
        kit.addItem(new ItemStackBuilder(Material.LEATHER_HELMET).setColor(Color.BLUE).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setColor(Color.BLUE).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_LEGGINGS).setColor(Color.BLUE).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_BOOTS).setColor(Color.BLUE).build());
        kit.addItem(new ItemStackBuilder(Material.STONE_SWORD).build());
        kit.addItem(new ItemStackBuilder(Material.BOW).build());
        kit.addItem(new ItemStackBuilder(Material.ARROW).setAmount(24).build());

        return kit;
    }

    public Kit getYellowKit() {
                Kit kit = new Kit("yellow", "Starter", new String[]{"Basic pvp gear"});
        kit.addItem(new ItemStackBuilder(Material.LEATHER_HELMET).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_LEGGINGS).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_BOOTS).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.STONE_SWORD).build());
        kit.addItem(new ItemStackBuilder(Material.BOW).build());
        kit.addItem(new ItemStackBuilder(Material.ARROW).setAmount(24).build());

        return kit;
    }

    public Kit getGreenKit() {
        Kit kit = new Kit("green", "Starter", new String[]{"Basic pvp gear"});
        kit.addItem(new ItemStackBuilder(Material.LEATHER_HELMET).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_LEGGINGS).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.LEATHER_BOOTS).setColor(Color.YELLOW).build());
        kit.addItem(new ItemStackBuilder(Material.STONE_SWORD).build());
        kit.addItem(new ItemStackBuilder(Material.BOW).build());
        kit.addItem(new ItemStackBuilder(Material.ARROW).setAmount(24).build());

        return kit;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public boolean isDoubleTime() {
        return doubleTime;
    }

    public Map<String, Integer> getScoreMap() {
        return scoreMap;
    }

    public Map<UUID, String> getCheck() {
        return check;
    }

    public Entity getOp() {
        return op;
    }

    public List<UUID> getElementalist() {
        return elementalist;
    }

    public Team find(String team) {
        for (Team t : teams) {
            if (t.getName().equalsIgnoreCase(team)) {
                return t;
            }
        }

        return null;
    }

    public Team getTeam(Player player) {
        for(Team t : teams) {
            if(t.getPlayers().contains(player.getUniqueId())) {
                return t;
            }
        }

        return null;
    }

    public boolean isTeamed(UUID hitter, UUID compared) {
        for (Iterator<Team> iterator = teams.iterator(); iterator.hasNext(); ) {
            Team t = iterator.next();
            return (t.getPlayers().contains(hitter) && t.getPlayers().contains(compared));
        }

        return false;
    }

}

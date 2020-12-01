/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.challenges;

import cc.fyre.stark.engine.scoreboard.ScoreFunction;
import cc.fyre.stark.util.CC;
import lombok.Getter;
import net.hcriots.hcfactions.challenges.maps.ChallengeCooldownMap;
import net.hcriots.hcfactions.challenges.maps.ChallengeMap;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ChallengeTypes {

    /* Tier 1 */
    MINE_STONE(1, ChallengeObjective.MINE, 500, "&aMine 500 Stone",
            Arrays.asList("&fMine 500 stone to complete this challenge."), Material.STONE, 1, null, Material.STONE,
            new ChallengeMap("STONE"), new ChallengeCooldownMap("STONE_CD")),

    MINE_REDSTONE(1, ChallengeObjective.MINE, 64, "&aMine 64 Redstone Ore",
            Arrays.asList("&fMine 64 redstone to complete this challenge."), Material.REDSTONE_ORE, 1, null, Material.REDSTONE_ORE,
            new ChallengeMap("REDSTONE_ORE"), new ChallengeCooldownMap("REDSTONE_ORE_CD")),

    MINE_EMERALD(1, ChallengeObjective.MINE, 16, "&aMine 16 Emerald Ore",
            Arrays.asList("&fMine 16 emerald to complete this challenge."), Material.EMERALD_ORE, 1, null, Material.EMERALD_ORE,
            new ChallengeMap("EMERALD_ORE"), new ChallengeCooldownMap("EMERALD_ORE_CD")),

    MINE_IRON(1, ChallengeObjective.MINE, 48, "&aMine 48 Iron Ore",
            Arrays.asList("&fMine 48 iron to complete this challenge."), Material.IRON_ORE, 1, null, Material.IRON_ORE,
            new ChallengeMap("IRON_ORE"), new ChallengeCooldownMap("IRON_ORE_CD")),

    MINE_COAL(1, ChallengeObjective.MINE, 64, "&aMine 64 Coal Ore",
            Arrays.asList("&fMine 64 coal to complete this challenge."), Material.COAL_ORE, 1, null, Material.COAL_ORE,
            new ChallengeMap("COAL_ORE"), new ChallengeCooldownMap("COAL_ORE_CD")),

    MINE_DIAMONDS(1, ChallengeObjective.MINE, 32, "&aMine 32 Diamond Ore",
            Arrays.asList("&fMine 32 diamonds to complete this challenge."), Material.DIAMOND_ORE, 1, null, Material.DIAMOND_ORE,
            new ChallengeMap("DIAMOND_ORE"), new ChallengeCooldownMap("DIAMOND_ORE_CD")),

    MINE_LEAVES(1, ChallengeObjective.MINE, 32, "&aMine 32 Oak Leaves",
            Arrays.asList("&fMine 32 oak leaves to complete this challenge."), Material.LEAVES, 1, null, Material.LEAVES,
            new ChallengeMap("LEAVES"), new ChallengeCooldownMap("LEAVES_CD")),

    MINE_LOGS(1, ChallengeObjective.MINE, 128, "&aMine 128 Logs",
            Arrays.asList("&fMine 128 logs to complete this challenge."), Material.LOG, 1, null, Material.LOG,
            new ChallengeMap("LOG"), new ChallengeCooldownMap("LOG_CD")),

    MINE_GLOWSTONE(1, ChallengeObjective.MINE, 32, "&aMine 32 Glowstone",
            Arrays.asList("&fMine 32 glowstone to complete this challenge."), Material.GLOWSTONE, 1, null, Material.GLOWSTONE,
            new ChallengeMap("GLOWSTONE"), new ChallengeCooldownMap("GLOWSTONE_CD")),

    //PLACE_BEACONS(1, ChallengeObjective.PLACE, 3, "&aPlace 3 Beacons",
    //        Arrays.asList("&fPlace 3 beacons to complete this challenge."), Material.BEACON, 1, null, Material.BEACON,
    //        new ChallengeMap("BEACON"), new ChallengeCooldownMap("BEACON_CD")),

    //PLACE_SAPLINGS(1, ChallengeObjective.PLACE, 6, "&aPlace 6 Saplings",
    //       Arrays.asList("&fPlace 6 saplings to complete this challenge."), Material.SAPLING, 1, null, Material.SAPLING,
    //        new ChallengeMap("SAPLING"), new ChallengeCooldownMap("SAPLING_CD")),

    KILL_COWS(1, ChallengeObjective.KILL, 32, "&aKill 32 Cows",
            Arrays.asList("&fKill 32 Cows to complete this challenge."), Material.LEATHER, 1, EntityType.COW, null,
            new ChallengeMap("COW"), new ChallengeCooldownMap("COW_CD")),

    /* Tier 2 */
    KILL_ENDERMEN(2, ChallengeObjective.KILL, 32, "&aKill 32 Endermen",
            Arrays.asList("&fKill 32 Endermen to complete this challenge."), Material.ENDER_PEARL, 3, EntityType.ENDERMAN, null,
            new ChallengeMap("ENDERMEN"), new ChallengeCooldownMap("ENDERMEN_CD")),

    KILL_BLAZE(2, ChallengeObjective.KILL, 16, "&aKill 16 Blazes",
            Arrays.asList("&fKill 16 Blazes to complete this challenge."), Material.BLAZE_ROD, 3, EntityType.BLAZE, null,
            new ChallengeMap("BLAZE"), new ChallengeCooldownMap("BLAZE_CD")),

    KILL_CREEPER(2, ChallengeObjective.KILL, 32, "&aKill 32 Creepers",
            Arrays.asList("&fKill 32 Creepers to complete this challenge."), Material.SULPHUR, 3, EntityType.CREEPER, null,
            new ChallengeMap("CREEPER"), new ChallengeCooldownMap("CREEPER_CD")),

    MINE_NETHERRACK(2, ChallengeObjective.MINE, 500, "&aMine 500 Netherrack",
            Arrays.asList("&fMine 500 Netherrack to complete this challenge."), Material.NETHERRACK, 3, null, Material.NETHERRACK,
            new ChallengeMap("NETHERRACK"), new ChallengeCooldownMap("NETHERRACK_CD")),

    MINE_QUARTZ(2, ChallengeObjective.MINE, 16, "&aMine 16 Quartz",
            Arrays.asList("&fMine 16 Quartz to complete this challenge."), Material.QUARTZ_ORE, 3, null, Material.QUARTZ_ORE,
            new ChallengeMap("QUARTZ"), new ChallengeCooldownMap("QUARTZ_CD")),

    CRAFT_GOD_APPLE(2, ChallengeObjective.CRAFT, 1, "&aCraft 1 God Apple",
            Arrays.asList("&fCraft a god apple to complete this challenge."), Material.GOLDEN_APPLE, 3, null, Material.GOLDEN_APPLE,
            new ChallengeMap("GOD_APPLE"), new ChallengeCooldownMap("GOD_APPLE_CD")),

    ENCHANT(2, ChallengeObjective.ENCHANT, 1, "&aEnchant 1 Item",
            Arrays.asList("&fEnchant an item to complete this challenge."), Material.ENCHANTMENT_TABLE, 3, null, Material.ENCHANTMENT_TABLE,
            new ChallengeMap("ENCHANT"), new ChallengeCooldownMap("ENCHANT_CD")),

    /**
     * todo I'll add this in later -senta
     */
    EQUIP_ROGUE(2, ChallengeObjective.EQUIP, 1, "&aEquip Rogue Class",
            Arrays.asList("&fEquip the rogue class to complete this challenge."), Material.CHAINMAIL_HELMET, 3, null, Material.CHAINMAIL_HELMET,
            new ChallengeMap("EQUIP_ROGUE"), new ChallengeCooldownMap("EQUIP_ROGUE_CD")),

    EQUIP_BARD(2, ChallengeObjective.EQUIP, 1, "&aEquip Bard Class",
            Arrays.asList("&fEquip the bard class to complete this challenge."), Material.GOLD_HELMET, 3, null, Material.GOLD_HELMET,
            new ChallengeMap("EQUIP_BARD"), new ChallengeCooldownMap("EQUIP_BARD_CD")),

    /* Tier 3 */
    MINE_MELONS(3, ChallengeObjective.MINE, 8, "&aMine 8 Melons",
            Arrays.asList("&fMine 8 melons to complete this challenge."), Material.MELON_BLOCK, 5, null, Material.MELON_BLOCK,
            new ChallengeMap("MINE_MELONS"), new ChallengeCooldownMap("MINE_MELONS_CD")),

    MINE_COBWEB(3, ChallengeObjective.MINE, 64, "&aMine 64 Cobwebs",
            Arrays.asList("&fMine 64 cobwebs to complete this challenge."), Material.WEB, 5, null, Material.WEB,
            new ChallengeMap("COBWEB"), new ChallengeCooldownMap("COBWEB_CD")),

    MINE_SAND(3, ChallengeObjective.MINE, 500, "&aMine 500 Sand",
            Arrays.asList("&fMine 500 sand to complete this challenge."), Material.SAND, 5, null, Material.SAND,
            new ChallengeMap("SAND"), new ChallengeCooldownMap("SAND_CD")),

    KILL_PLAYERS(3, ChallengeObjective.KILL, 10, "&aKill 10 Players",
            Arrays.asList("&fKill 10 Players to complete this challenge."), Material.DIAMOND_SWORD, 5, EntityType.PLAYER, null,
            new ChallengeMap("KILL_PLAYERS"), new ChallengeCooldownMap("KILL_PLAYERS_CD")),

    PLAYER_DIE(3, ChallengeObjective.DEATH, 3, "&aDie 3 Times",
            Arrays.asList("&fDie 3 times to complete this challenge."), Material.SKULL_ITEM, 5, EntityType.PLAYER, null,
            new ChallengeMap("DIE"), new ChallengeCooldownMap("DIE_CD")),

    PLACE_CAKE(3, ChallengeObjective.PLACE, 1, "&aPlace 1 Cake",
            Arrays.asList("&fPlace a cake to complete this challenge."), Material.CAKE, 5, null, Material.CAKE_BLOCK,
            new ChallengeMap("CAKE"), new ChallengeCooldownMap("CAKE_CD")),

    CRAFT_STICKY_PISTON(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Sticky Piston",
            Arrays.asList("&fCraft a sticky piston to complete this challenge."), Material.PISTON_STICKY_BASE, 5, null, Material.PISTON_STICKY_BASE,
            new ChallengeMap("STICKY_PISTON"), new ChallengeCooldownMap("STICKY_PISTON_CD")),

    CRAFT_LEASH(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Lead",
            Arrays.asList("&fCraft a lead to complete this challenge."), Material.LEASH, 5, null, Material.LEASH,
            new ChallengeMap("LEASH"), new ChallengeCooldownMap("LEASH_CD")),

    CRAFT_BOOK_QUILL(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Book and Quill",
            Arrays.asList("&fCraft a book and quill to complete this challenge."), Material.BOOK_AND_QUILL, 5, null, Material.BOOK_AND_QUILL,
            new ChallengeMap("BOOK_QUILL"), new ChallengeCooldownMap("BOOK_QUILL_CD")),

    CRAFT_DAYLIGHT_SENSOR(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Daylight Sensor",
            Arrays.asList("&fCraft a daylight sensor to complete this challenge."), Material.DAYLIGHT_DETECTOR, 5, null, Material.DAYLIGHT_DETECTOR,
            new ChallengeMap("DAYLIGHT_DETECTOR"), new ChallengeCooldownMap("DAYLIGHT_DETECTOR_CD")),

    CRAFT_DROPPER(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Dropper",
            Arrays.asList("&fCraft a dropper to complete this challenge."), Material.DROPPER, 5, null, Material.DROPPER,
            new ChallengeMap("DROPPER"), new ChallengeCooldownMap("DROPPER_CD")),

    CRAFT_PAINTING(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Painting",
            Arrays.asList("&fCraft a daylight sensor to complete this challenge."), Material.PAINTING, 5, null, Material.PAINTING,
            new ChallengeMap("PAINTING"), new ChallengeCooldownMap("PAINTING_CD")),

    CRAFT_FLOWER_POT(3, ChallengeObjective.CRAFT, 1, "&aCraft 1 Flower Pot",
            Arrays.asList("&fCraft a flower pot to complete this challenge."), Material.FLOWER_POT_ITEM, 5, null, Material.FLOWER_POT,
            new ChallengeMap("FLOWER_POT"), new ChallengeCooldownMap("FLOWER_POT_CD")),

    CRAFT_BOOKSHELVES(3, ChallengeObjective.CRAFT, 1, "&aCraft 3 Bookshelves",
            Arrays.asList("&fCraft 3 bookshelves to complete this challenge."), Material.BOOKSHELF, 5, null, Material.BOOKSHELF,
            new ChallengeMap("BOOKSHELF"), new ChallengeCooldownMap("BOOKSHELF_CD"));


    private final int challengeTier;
    private final ChallengeObjective challengeObjective;
    private final int challengeAmount;
    private final String challengeTitle;
    private final List<String> challengeLore;
    private final Material challengeIcon;
    private final int challengeCharmReward;
    private final EntityType entityType;
    private final Material blockType;
    private final ChallengeMap challengeMap;
    private final ChallengeCooldownMap challengeCooldownMap;

    ChallengeTypes(int challengeTier, ChallengeObjective challengeObjective, int challengeAmount, String challengeTitle, List<String> challengeLore, Material challengeIcon, int challengeCharmReward, EntityType entityType, Material blockType, ChallengeMap challengeMap, ChallengeCooldownMap challengeCooldownMap) {
        this.challengeTier = challengeTier;
        this.challengeObjective = challengeObjective;
        this.challengeAmount = challengeAmount;
        this.challengeTitle = challengeTitle;
        this.challengeLore = challengeLore;
        this.challengeIcon = challengeIcon;
        this.challengeCharmReward = challengeCharmReward;
        this.entityType = entityType;
        this.blockType = blockType;
        this.challengeMap = challengeMap;
        this.challengeMap.loadFromRedis();
        this.challengeCooldownMap = challengeCooldownMap;
        this.challengeCooldownMap.loadFromRedis();
    }

    public List<String> getCustomLore(Player player) {
        List<String> cachedLore = new ArrayList<>();

        if (challengeTier > 1) {
            cachedLore.add("&2Cooldown");
            if (this.getChallengeCooldownMap().isOnCooldown(player.getUniqueId())) {
                cachedLore.add("&f" + ScoreFunction.Companion.TIME_FANCY((float) ((this.challengeCooldownMap.getCooldown(player.getUniqueId()) - System.currentTimeMillis()) / 1000L)));
            } else {
                cachedLore.add("&cNone Active.");
            }

            cachedLore.add(" ");
        }

        cachedLore.add("&2Remaining");
        if (this.challengeAmount - challengeMap.getAmount(player.getUniqueId()) <= 0 || this.challengeCooldownMap.isOnCooldown(player.getUniqueId())) {
            cachedLore.add("&fComplete.");
        } else {
            cachedLore.add("&f" + (this.challengeAmount - challengeMap.getAmount(player.getUniqueId()) + " left to go!"));
        }

        cachedLore.add(" ");
        cachedLore.add("&2Objective");
        cachedLore.addAll(challengeLore);

        cachedLore.add(" ");
        cachedLore.add("&2Reward");
        cachedLore.add("&f" + (challengeCharmReward) + " Charm(s).");

        return cachedLore;
    }

    public ItemStack getCusomItem(Player player) {
        ItemStack item = new ItemStack(challengeIcon, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(CC.INSTANCE.translate(challengeTitle));
        meta.setLore(CC.INSTANCE.translate(getCustomLore(player)));

        item.setItemMeta(meta);
        return item;
    }

    public enum ChallengeObjective {
        MINE,
        KILL,
        DEATH,
        PLACE,
        CRAFT,
        ENCHANT,
        EQUIP
    }
}

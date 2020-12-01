package net.hcriots.hcfactions.commands;

import cc.fyre.stark.engine.command.Command;
import net.hcriots.hcfactions.blockshop.menu.BlockShopMenu;
import net.hcriots.hcfactions.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BlockshopCommand {

    @Command(names = {"blockshop", "block shop"}, permission = "")
    public static void blockShop(Player sender) {
        if (!DTRBitmask.SAFE_ZONE.appliesAt(sender.getLocation()) && !CustomTimerCreateCommand.isSOTWTimer()) {
            sender.sendMessage(ChatColor.RED + "You can only use this command in safe-zones.");
            return;
        }

        new BlockShopMenu().openMenu(sender);
    }
}

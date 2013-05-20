/* 
 * Copyright (C) 2013 Dr Daniel R. Naylor
 * 
 * This file is part of mcMMO Party Admin.
 *
 * mcMMO Party Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mcMMO Party Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mcMMO Party Admin.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package uk.co.drnaylor.mcmmopartyadmin.commands.subcommands;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;
import uk.co.drnaylor.mcmmopartyadmin.Util;
import uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor.DualSubCommandInterface;
import uk.co.drnaylor.mcmmopartyadmin.locales.L10n;

public class AddPlayerSubCommand implements DualSubCommandInterface {

    private List<String> permissions = new ArrayList<String>();
    
    public AddPlayerSubCommand() {
        permissions.add("mcmmopartyadmin.admin");
    }
    
    public List<String> getSubCommands() {
        return Arrays.asList(new String[] { "addplayer", "apl" });
    }

    public String getShortHelp() {
        return ChatColor.YELLOW + "/partyadmin apl <player> <party> " + ChatColor.WHITE + "- " + L10n.getString("Description.Add");
    }

    public String[] getLongHelp() {
        return new String[] { getShortHelp() };
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean checkPermissions(CommandSender sender) {
        if (!(sender instanceof Player) || sender.isOp()) return true;
        
        for (String p : permissions) {
            if (sender.hasPermission(p)) return true;
        }
        
        return false;
    }

    public void executeSubCommand(CommandSender sender, String[] cmdargs) {
        if (cmdargs.length == 2) {
            addPlayerToParty(sender, cmdargs[0], cmdargs[1]);
            return;
        }
        sender.sendMessage(ChatColor.RED + "Incorrect usage!");
        sender.sendMessage(getLongHelp());
    }

    public List<String> onSubCommandTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Util.getPartyCollection();
        }
        return null;
    }
    
    
    /**
     * Adds a player to a party.
     *
     * @param sender Player requesting the addition
     * @param player Player to add to party
     * @param partyName Party to add player to
     */
    private void addPlayerToParty(CommandSender sender, String player, String partyName) {
        // Get the OfflinePlayer
        Player targetPlayer = PartyAdmin.getPlugin().getServer().getPlayerExact(player);

        Party party = Util.getPartyFromList(partyName);

        // No party!
        if (party == null) {
            sender.sendMessage(L10n.getString("Party.DoesNotExist", partyName));
            return;
        } else if (targetPlayer == null) {
            sender.sendMessage(L10n.getString("Player.NotOnline", player));
            return;
        }

        if (PartyAPI.inParty(targetPlayer)) {
            PartyAPI.removeFromParty(targetPlayer);
        }

        // If the player is online, we can add them to the party using the API
        PartyAPI.addToParty(targetPlayer, partyName);
        // Check to see that it happened and the event wasn't cancelled.
        if (PartyAPI.getPartyName(targetPlayer).equals(partyName)) {
            sender.sendMessage(L10n.getString("Commands.Added.Success", targetPlayer.getName(), partyName));
        } else {
            sender.sendMessage(L10n.getString("Commands.Added.Failed", targetPlayer.getName(), partyName));
        }
    }
    
}

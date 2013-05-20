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
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.player.UserManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;
import uk.co.drnaylor.mcmmopartyadmin.Util;
import uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor.DualSubCommandInterface;
import uk.co.drnaylor.mcmmopartyadmin.locales.L10n;

public class ChangeOwnerSubCommand implements DualSubCommandInterface {

    private List<String> permissions = new ArrayList<String>();

    public ChangeOwnerSubCommand() {
        permissions.add("mcmmopartyadmin.admin");
    }
    
    public List<String> getSubCommands() {
        return Arrays.asList(new String[] {"chown", "changeowner"});
    }

    public String getShortHelp() {
        return ChatColor.YELLOW + "/partyadmin chown <player> <party> " + ChatColor.WHITE + "- " + L10n.getString("Description.ChangeOwner");
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
            changePartyOwner(sender, cmdargs[0], cmdargs[1]);
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
     * Change the owner of a party.
     *
     * @param sender Player requesting the change
     * @param player Player to make the owner
     * @param partyName Party to make them the owner of
     */
    private void changePartyOwner(CommandSender sender, String player, String partyName) {
        OfflinePlayer targetPlayer = PartyAdmin.getPlugin().getServer().getOfflinePlayer(player);

        if (targetPlayer == null) {
            // Player doesn't exist
            sender.sendMessage(L10n.getString("Player.NotFound", player));
            return;
        }

        McMMOPlayer mcplayer = UserManager.getPlayer(targetPlayer);
        Party party = mcplayer.getParty();

        if (party.getName().equals(partyName)) {
            PartyAPI.setPartyLeader(partyName, targetPlayer.getName());
            sender.sendMessage(L10n.getString("Commands.ChangeOwner.Success", player, partyName));
        } else {
            sender.sendMessage(L10n.getString("Commands.ChangeOwner.NotInParty", player, partyName));
        }
    }
}

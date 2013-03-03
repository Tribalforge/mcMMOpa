/* 
 * Copyright (C) 2013 Dr Daniel R. Naylor
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 **/
package uk.co.drnaylor.mcmmopartyadmin;

import com.gmail.nossr50.api.ChatAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyAdminCommand implements CommandExecutor {

    private PartyAdmin plugin;
    
    public PartyAdminCommand(PartyAdmin plugin) {
        this.plugin = plugin;
    }
    
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (sender.hasPermission("mcmmopartyadmin.commands.partyadmin") || sender.isOp() || player == null) {

            if (args.length > 2 && (args[0].equalsIgnoreCase("pc") || args[0].equalsIgnoreCase("chat"))) {
                partyChat(sender, player, command, label, args);
                return true;
            }

            switch (args.length) {
                case 1:
                    if (args[0].equalsIgnoreCase("list")) {
                        // Get ALL the parties!
                        List<Party> parties = PartyAPI.getParties();
                        
                        // No parties? That's a shame...
                        if (parties.isEmpty()) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "There are no parties.");
                        } else {
                            // Header
                            sender.sendMessage(ChatColor.DARK_AQUA + "Current Parties");
                            sender.sendMessage(ChatColor.DARK_AQUA + "===============");
                            
                            // Over each party...
                            for (Party a : parties) {
                                
                                // Get Party Leader
                                String leader = PartyAPI.getPartyLeader(a.getName());
                                
                                // Start building new string
                                StringBuffer tempList = new StringBuffer();

                                // Over all players
                                for (OfflinePlayer otherPlayerName : a.getMembers()) {
                                    tempList.append(" ");
                                    if (leader.equals(otherPlayerName.getName())) {
                                        // Leader in Gold
                                        tempList.append(ChatColor.GOLD); 
                                    } else if (otherPlayerName.isOnline()) {
                                        // Online players in White
                                        tempList.append(ChatColor.WHITE);
                                    } else {
                                        // Offline players in Grey
                                        tempList.append(ChatColor.GRAY);
                                    }
                                    // Add name and space
                                    tempList.append(otherPlayerName.getName());
                                }
                               
                                
                                // Send the message
                                sender.sendMessage(ChatColor.DARK_AQUA + a.getName() + ":" + tempList);
                            }
                        }
                        return true;
                    }


                case 2:
                    if (args[0].equalsIgnoreCase("removeparty") || args[0].equalsIgnoreCase("remparty") || args[0].equalsIgnoreCase("delparty") || args[0].equalsIgnoreCase("rp")) {

                        Party target = Util.getPartyFromList(args[1]);
                        
                        if (target == null) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "The party " + args[1] + " does not exist!");
                            return true;
                        }
                        
                        // From Party Disband command
                        
                        for (Player member : target.getOnlineMembers()) {
                            if (!PartyManager.handlePartyChangeEvent(member, target.getName(), null, McMMOPartyChangeEvent.EventReason.KICKED_FROM_PARTY)) {
                                return true;
                            }
                            
                            member.sendMessage(ChatColor.DARK_AQUA + "An admin has disbanded your party (" + args[1] + ")");
                        }

                        PartyManager.disbandParty(target);
                        
                        sender.sendMessage(ChatColor.DARK_AQUA + "The party " + args[1] + " has been disbanded!");

                        return true;

                    } else if (args[0].equalsIgnoreCase("removeplayer") || args[0].equalsIgnoreCase("rpl") || args[0].equalsIgnoreCase("kickplayer")) {
                        Player targetPlayer = plugin.getServer().getPlayer(args[1]);
                        
                        // If the player is online
                        if (targetPlayer != null) {
                            // Is the player in a party?
                            if (PartyAPI.inParty(targetPlayer)) {
                                // Remove from the party!
                                PartyAPI.removeFromParty(targetPlayer);

                                // Tell them, and the sender
                                targetPlayer.sendMessage(ChatColor.DARK_AQUA + "An admin has kicked you from the party");
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " is no longer in a party");
                            } else {
                                // Tell the sender that the can't do that!
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " is not in a party");
                            }
                        } else {
                            //Check to see if there is an offline player
                            OfflinePlayer targetOfflinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                            
                            // Is there a player that has joined the server before?
                            if (targetOfflinePlayer == null) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be found!");
                                return true;
                            }
                            
                            McMMOPlayer mcMMOPlayer = UserManager.getPlayer(targetOfflinePlayer.getName());

                            if (mcMMOPlayer == null) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be found in the mcMMO system!");
                                return true;
                            }
                            Party party = mcMMOPlayer.getParty();

                            if (party != null) {
                                mcMMOPlayer.removeParty();
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " is no longer in a party");
                            }
                            else {
                                // Not in a party
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " is not in a party");
                                return true;
                            }
                        }
                        return true;
                    } else {
                        listCommands(sender);
                        return true;
                    }

                case 3:
                    if (args[0].equalsIgnoreCase("addplayer") || args[0].equalsIgnoreCase("apl")) {
                        
                        // Get the OfflinePlayer
                        OfflinePlayer targetPlayer = plugin.getServer().getOfflinePlayer(args[1]);
                        
                        Party party = Util.getPartyFromList(args[2]);
                        
                        // No party!
                        if (party == null) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "That party cannot be found.");
                            return true;
                        } else if (targetPlayer == null) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be found!");
                            return true;
                        }
                        
                        // If the player is online, we can add them to the party using the API
                        if (targetPlayer.isOnline()) {
                            Player onlinePlayer = targetPlayer.getPlayer();
                            PartyAPI.addToParty(onlinePlayer, args[2]);
                            // Check to see that it happened and the event wasn't cancelled.
                            if (PartyAPI.getPartyName(onlinePlayer).equals(args[2])) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + onlinePlayer.getName() + ChatColor.DARK_AQUA + " has been added to the party " + ChatColor.WHITE + args[2]);
                            }
                            else {
                                sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + onlinePlayer.getName() + ChatColor.DARK_AQUA + " could not be added to the requested party at this time.");
                            }
                            return true;
                        }
                        
                        // Player is offline. 

                        McMMOPlayer mcplayer = UserManager.getPlayer(targetPlayer.getName());
                        
                        try {
                            mcplayer.removeParty(); // Remove the party before adding them
                            mcplayer.setParty(party);
                            sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + targetPlayer.getName() + ChatColor.DARK_AQUA + " has been added to the party " + ChatColor.WHITE + args[2]);
                        }
                        catch (Exception e) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + targetPlayer.getName() + ChatColor.DARK_AQUA + " could not be added to the requested party at this time.");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("changeowner") || args[0].equalsIgnoreCase("chown")) {

                        OfflinePlayer targetPlayer = plugin.getServer().getOfflinePlayer(args[1]);

                        if (targetPlayer == null) {
                            // Player doesn't exist
                            sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be found!");
                            return true;
                        }

                        McMMOPlayer mcplayer = UserManager.getPlayer(targetPlayer);
                        Party party = mcplayer.getParty();
                        
                        if (party.getName().equals(args[2])) {
                            PartyAPI.setPartyLeader(targetPlayer.getName(), party.getName());
                            sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.DARK_AQUA + " is now the leader of their party.");
                        } else {
                            sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + targetPlayer.getName() + ChatColor.DARK_AQUA + " is not in the party " + ChatColor.WHITE + args[2]);
                        }
                        return true;
                    } else if ((args[0].equalsIgnoreCase("pc") || args[0].equalsIgnoreCase("chat"))) {
                            partyChat(sender, player, command, label, args);
                    } else {
                        listCommands(sender);
                        return true;
                    }
                default:
                    listCommands(sender);
                    return true;
            }
        } else {
            // No perms! Leave us be!
            player.sendMessage(ChatColor.RED + "You do not have permission to do this");
        }

        return true;
    }
    
    
    
    private void partyChat(CommandSender sender, Player player, Command command, String label, String[] args) {
        if (Util.getPartyFromList(args[1]) == null) {
            sender.sendMessage(LocaleLoader.getString("Party.InvalidName"));
            return;
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(args[2]);

        for (int i = 3; i < args.length; i++) {
            buffer.append(" ");
            buffer.append(args[i]);
        }

        String message = buffer.toString();
        
        if (!(sender instanceof Player)) {
            ChatAPI.sendPartyChat(plugin,"*Console*", args[1], message);
        } else {
            ChatAPI.sendPartyChat(plugin,sender.getName(), args[1], message);
        }
        
        if (sender instanceof Player) {
            Player send = (Player) sender;
            if (!PartySpy.isSpy(send)) {
                sender.sendMessage(ChatColor.DARK_AQUA + "WARNING: Party Spy is OFF. Toggle with " + ChatColor.YELLOW + "/partyspy");
                String p2 = ChatColor.GRAY + "[" + args[1] + "] " + ChatColor.GREEN + " (" + ChatColor.WHITE + sender.getName() + ChatColor.GREEN + ") ";
                sender.sendMessage(p2 + message); 
            }
        } 
    }

    private void listCommands(CommandSender player) {
        player.sendMessage(ChatColor.DARK_AQUA + "mcMMO Party Admin");
        player.sendMessage(ChatColor.DARK_AQUA + "=================");
        player.sendMessage(ChatColor.YELLOW + "/partyadmin list " + ChatColor.WHITE + "- List current parties");
        player.sendMessage(ChatColor.YELLOW + "/partyadmin rp <party> " + ChatColor.WHITE + "- Delete party");
        player.sendMessage(ChatColor.YELLOW + "/partyadmin apl <player> <party> " + ChatColor.WHITE + "- Add player to party");
        player.sendMessage(ChatColor.YELLOW + "/partyadmin rpl <player> " + ChatColor.WHITE + "- Remove player from party");
        player.sendMessage(ChatColor.YELLOW + "/partyadmin chown <player> <party> " + ChatColor.WHITE + "- Change ownership of party to player");
        player.sendMessage(ChatColor.YELLOW + "/partyadmin pc <party> " + ChatColor.WHITE + "- Chat to party without joining it");
    }
}

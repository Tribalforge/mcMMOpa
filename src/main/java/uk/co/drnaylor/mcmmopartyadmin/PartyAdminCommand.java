package uk.co.drnaylor.mcmmopartyadmin;

import com.gmail.nossr50.api.ChatAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.PlayerProfile;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.party.Party;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.Users;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author dualspiral
 */
public class PartyAdminCommand implements CommandExecutor {

    private mcMMO mcmmo;
    private PartyAdmin plugin;
    
    public PartyAdminCommand(PartyAdmin plugin) {
        this.plugin = plugin;
        this.mcmmo = plugin.mcmmo;
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
                            player.sendMessage(ChatColor.DARK_AQUA + "There are no parties.");
                        } else {
                            // Header
                            player.sendMessage(ChatColor.DARK_AQUA + "Current Parties");
                            player.sendMessage(ChatColor.DARK_AQUA + "===============");
                            
                            // Over each party...
                            for (Party a : parties) {
                                
                                // Get Party Leader
                                String leader = PartyAPI.getPartyLeader(a.getName());
                                
                                // Start building new string
                                StringBuffer tempList = new StringBuffer();

                                // Over all players
                                for (String otherPlayerName : a.getMembers()) {
                                    if (leader.equals(otherPlayerName)) {
                                        // Leader in Gold
                                        tempList.append(ChatColor.GOLD); 
                                    } else if (plugin.getServer().getPlayer(otherPlayerName) != null) {
                                        // Online players in White
                                        tempList.append(ChatColor.WHITE);
                                    } else {
                                        // Offline players in Grey
                                        tempList.append(ChatColor.GRAY);
                                    }
                                    // Add name and space
                                    tempList.append(otherPlayerName + " ");
                                }
                                
                                // Send the message
                                player.sendMessage(ChatColor.DARK_AQUA + a.getName() + ": " + tempList);

                            }
                        }
                        return true;
                    }


                case 2:
                    if (args[0].equalsIgnoreCase("removeparty") || args[0].equalsIgnoreCase("remparty") || args[0].equalsIgnoreCase("delparty") || args[0].equalsIgnoreCase("rp")) {

                        // Get the party
                        Party target = PartyManager.getInstance().getParty(args[1]);

                        if (target != null) {
                            String oldparty = args[1];
                            List<Player> players = Collections.unmodifiableList(PartyAPI.getOnlineMembers(target.getName()));

                            // Remove online players
                            if (!players.isEmpty() && players != null) {
                                for (Player a : players) {
                                    a.sendMessage(ChatColor.RED + "You have left your party, as it has been deleted by an admin.");
                                    PartyAPI.removeFromParty(a);
                                }
                            }

                            // Remove offline players
                            List<String> members = target.getMembers();
                            if (!members.isEmpty()) {
                                // To avoid comodification, we need to clone the list in memory, not just the pointer.
                                List<String> mem = new ArrayList<String>();
                                mem.addAll(members);
                                for (int i = 0; i < mem.size(); i++) {
                                    PartyManager.getInstance().removeFromParty(mem.get(i), target);
                                }
                            }
                            target = PartyManager.getInstance().getParty(args[1]);

                            sender.sendMessage(ChatColor.DARK_AQUA + "The party " + oldparty + " has been deleted!");
                        } else {
                            sender.sendMessage(ChatColor.DARK_AQUA + "The party " + args[1] + " does not exist!");
                        }
                        return true;

                    } else if (args[0].equalsIgnoreCase("removeplayer") || args[0].equalsIgnoreCase("rpl") || args[0].equalsIgnoreCase("kickplayer")) {
                        String playername;
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

                            playername = targetOfflinePlayer.getName();
                            Party party = PartyManager.getInstance().getPlayerParty(playername);
                            if (party == null) {
                                // Not in a party
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " is not in a party");
                                return true;
                            } else {
                                // Remove!
                                PartyManager.getInstance().removeFromParty(playername, party);
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " is no longer in a party");
                            }
                        }
                        return true;
                    } else {
                        listCommands(sender);
                        return true;
                    }

                case 3:
                    if (args[0].equalsIgnoreCase("addplayer") || args[0].equalsIgnoreCase("apl")) {
                        
                        String playername;
                        // Get the player
                        Player targetPlayer = plugin.getServer().getPlayer(args[1]);
                        PlayerProfile profile = null;

                        // If there is no player, then we check for an offline player
                        if (targetPlayer == null) {
                            OfflinePlayer targetOfflinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                            playername = targetOfflinePlayer.getName();
                            profile = Users.getProfile(playername);

                            // The player needs to be online to add them to a party, however.
                            if (playername == null) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be found!");
                                return true;
                            } else if (profile == null) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be added to a party at this time!");
                                return true;
                            }
                        }

                        //OK! So the player is online. Get the name!
                        playername = targetPlayer.getName();
                        if (PartyManager.getInstance().isParty(args[2])) {
                            // Add them to the party!
                            PartyAPI.addToParty(targetPlayer, args[2]);
                            sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + playername + ChatColor.DARK_AQUA + " has been added to the party " + ChatColor.WHITE + args[2]);
                        } else {
                            // Wait... there is no party!
                            sender.sendMessage(ChatColor.DARK_AQUA + "That party cannot be found.");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("changeowner") || args[0].equalsIgnoreCase("chown")) {
                        String playername;
                        Player targetPlayer = plugin.getServer().getPlayer(args[1]);
                        if (targetPlayer != null) {
                            //Get the name!
                            playername = targetPlayer.getName();
                        } else {
                            //Check to see if there is an offline player
                            OfflinePlayer targetOfflinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                            playername = targetOfflinePlayer.getName();
                        }

                        if (playername == null) {
                            // Player doesn't exist
                            sender.sendMessage(ChatColor.DARK_AQUA + "The player " + args[1] + " cannot be found!");
                            return true;
                        }

                        // Player exists! Party
                        Party party = PartyManager.getInstance().getParty(args[2]);
                        if (party != null) {
                            // Get member list
                            List<String> members = party.getMembers();
                            if (members.contains(playername)) {
                                // If the player is in the party, set them as leader
                                sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + playername + ChatColor.DARK_AQUA + " is now the owner of " + ChatColor.WHITE + args[2]);
                                PartyAPI.setPartyLeader(party.getName(), playername);
                                if (targetPlayer != null) {
                                    targetPlayer.sendMessage(ChatColor.DARK_AQUA + "You are now the owner of " + ChatColor.WHITE + args[2]);
                                }
                            } else {
                                // Or not, if they aren't in the party
                                sender.sendMessage(ChatColor.DARK_AQUA + "Player " + ChatColor.WHITE + playername + ChatColor.DARK_AQUA + " is not a member of the party " + ChatColor.WHITE + args[2]);
                            }
                        } else {
                            // The party is just not there!
                            sender.sendMessage(ChatColor.DARK_AQUA + "That party cannot be found.");
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
        if (!PartyManager.getInstance().isParty(args[1])) {
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
        
        ChatAPI.sendPartyChat(sender.getName(), args[1], message);
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

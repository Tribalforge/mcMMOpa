package uk.co.drnaylor.mcmmopartyadmin.commands.subcommands;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;
import uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor.DualSubCommandInterface;
import uk.co.drnaylor.mcmmopartyadmin.locales.L10n;

public class ListPartiesSubCommand implements DualSubCommandInterface {

    private List<String> permissions = new ArrayList<String>();
    
    public ListPartiesSubCommand() {
        permissions.add("mcmmopartyadmin.admin");
    }

    public List<String> getSubCommands() {
        return Arrays.asList("list");
    }

    public String getShortHelp() {
        return ChatColor.YELLOW + "/partyadmin list " + ChatColor.WHITE + "- " + L10n.getString("Description.List");
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
        if (cmdargs.length == 0) {
            listParties(sender);
            return;
        }
        sender.sendMessage(ChatColor.RED + "Incorrect Usage!");
        sender.sendMessage(getLongHelp());
    }

    public List<String> onSubCommandTabComplete(CommandSender sender, String[] args) {
        return null;
    }
    
    
    /**
     * Sends a list of all parties and it's members to the requester.
     *
     * @param sender Requester to send the list to.
     */
    private void listParties(CommandSender sender) {
        // Get ALL the parties!
        List<Party> parties = PartyAPI.getParties();

        // No parties? That's a shame...
        if (parties.isEmpty()) {
            sender.sendMessage(L10n.getString("Commands.List.NoParties"));
        } else {
            // Header
            sender.sendMessage(L10n.getString("Commands.List.PartyListHeader"));
            sender.sendMessage(ChatColor.DARK_AQUA + "===============");

            // This next bit has no need for localisation

            // Over each party...
            for (Party a : parties) {

                // Get Party Leader
                String leader = PartyAPI.getPartyLeader(a.getName());

                // Start building new string
                StringBuilder tempList = new StringBuilder();

                tempList.append(ChatColor.DARK_AQUA);
                tempList.append(a.getName());
                tempList.append(":");

                // Over all players
                for (String pa : a.getMembers()) {
                    OfflinePlayer otherPlayerName = PartyAdmin.getPlugin().getServer().getOfflinePlayer(pa);
                    
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
                sender.sendMessage(tempList.toString());
            }
        }
    }
    
}

package uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface DualSubCommandInterface {
 
    /**
     * Get the subcommands that this object represents.
     * @return Subcommands (i.e. the first argument of the normal command)
     */
    public List<String> getSubCommands();
    
    /**
     * Gets the one-line help associated with the command.
     * @return Message with help for the command
     */
    public String getShortHelp();
    
    /**
     * Gets the multi-line help associated with the command.
     * @return Message with help for the command
     */
    public String[] getLongHelp();
    
    /**
     * Gets the permissions required to execute this sub-command.
     * @return List of strings representing the required permissions.
     */
    public List<String> getPermissions();
    
    /**
     * Checks that the requester has the permissions required to execute this sub-command.
     * @return true if so, false otherwise
     */
    public boolean checkPermissions(CommandSender sender);
    
    /**
     * Execute the subcommand that this object represents.
     * @param sender Command sender
     * @param cmdargs Arguments of the subcommand.
     */
    public void executeSubCommand(CommandSender sender, String[] cmdargs);
    
    /**
     * Provides the TabExecutor with a list of subcommands to use when TAB is pressed. The auto complete acts on the
     * last item in the arguments list, which is the arguments to the <em>sub command</em> - that is, with the 
     * first argument removed from the full command (as that is the sub command name!).
     * 
     * @param sender Sender of the tab request
     * @param args Arguments in the list so far
     * @return List of strings for the autocomplete.
     */
    public List<String> onSubCommandTabComplete(CommandSender sender, String[] args);
}

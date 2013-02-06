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

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartySpy implements CommandExecutor {
    
    private PartyAdmin _plugin;
    private static List<String> _spies;
    
    public PartySpy(PartyAdmin plugin) {
        _plugin = plugin;
        _plugin.reloadConfig();
        _spies = _plugin.getConfig().getStringList("partyspy");
    }
    
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cmnd.getName().equalsIgnoreCase("partyspy")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!(cs instanceof Player) || cs.hasPermission("mcmmopartyadmin.admin") || cs.isOp()) {
                    _plugin.reloadConfig();
                    _spies = _plugin.getConfig().getStringList("partyspy");
                }
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
                    if (!(cs instanceof Player) || cs.hasPermission("mcmmopartyadmin.admin") || cs.isOp()) {
                        _plugin.getConfig().set("partyspy", _spies);
                        _plugin.saveConfig();
                    }
                }
            else if(args.length == 0) {
                if (cs instanceof Player) {
                    Player player = (Player) cs;
                    toggleSpy(player);
                    cs.sendMessage(ChatColor.DARK_AQUA + "PartySpy is now toggled to " + ChatColor.YELLOW + (isSpy(player) ? "on" : "off") );
                    _plugin.getConfig().set("partyspy", _spies);
                    _plugin.saveConfig();
                }
                else {
                    cs.sendMessage(ChatColor.DARK_AQUA + "Console, you can't set this!");
                }
            }
            else {
                cs.sendMessage(ChatColor.DARK_AQUA + "Incorrect usage!");
            }
            return true;
        }
        return false;
    }
    
    public static boolean isSpy(Player player) {
        if (player.hasPermission("mcmmopartyadmin.spy") || player.isOp()) {
            return _spies.contains(player.getName());
        }
        return false;
    }
    
    public static void toggleSpy(Player player) {
        if (_spies.contains(player.getName())) {
            _spies.remove(player.getName());
        }
        else {
            _spies.add(player.getName());
        }
    }
}
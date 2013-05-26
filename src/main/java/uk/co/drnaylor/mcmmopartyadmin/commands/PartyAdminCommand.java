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
package uk.co.drnaylor.mcmmopartyadmin.commands;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;
import uk.co.drnaylor.mcmmopartyadmin.commands.subcommands.*;
import uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor.DualCommandExecutor;
import uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor.DualSubCommandInterface;
import uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor.SubCommandException;
import uk.co.drnaylor.mcmmopartyadmin.locales.L10n;

public class PartyAdminCommand extends DualCommandExecutor {

    public PartyAdminCommand() {
        super();
        // Register the sub commands
        try {
            this.RegisterSubCommand(new ListPartiesSubCommand());
        } catch (SubCommandException ex) {
            Logger.getLogger(PartyAdminCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.RegisterSubCommand(new AddPlayerSubCommand());
        } catch (SubCommandException ex) {
            Logger.getLogger(PartyAdminCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.RegisterSubCommand(new RemovePlayerSubCommand());
        } catch (SubCommandException ex) {
            Logger.getLogger(PartyAdminCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.RegisterSubCommand(new RemovePartySubCommand());
        } catch (SubCommandException ex) {
            Logger.getLogger(PartyAdminCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.RegisterSubCommand(new ChangeOwnerSubCommand());
        } catch (SubCommandException ex) {
            Logger.getLogger(PartyAdminCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.RegisterSubCommand(new PartyChatSubCommand());
        } catch (SubCommandException ex) {
            Logger.getLogger(PartyAdminCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean onNoSubCommand(CommandSender sender, Command command, String label) {
        sender.sendMessage(ChatColor.DARK_AQUA + "mcMMO Party Admin v" + PartyAdmin.getPlugin().getDescription().getVersion()); //No need to localise this line
        sender.sendMessage(ChatColor.DARK_AQUA + "=================");
        for (DualSubCommandInterface iface : GetSubCommands()) {
            sender.sendMessage(iface.getShortHelp());
        }
        return true;
    }

    @Override
    public boolean onInvalidSubCommand(CommandSender sender, Command command, String label, String[] args) {
        return onNoSubCommand(sender, command, label);
    }

    @Override
    public boolean onNoPermissionsSubCommand(CommandSender sender, Command command, String label, String[] args, List<String> requiredPerms) {
        sender.sendMessage(L10n.getString("Commands.NoPermission"));
        return true;
    }
    
}

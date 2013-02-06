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

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PartyChangeListener implements Listener {
    
    @EventHandler
    public void PartyChange(McMMOPartyChangeEvent event) {
        if (event.getNewParty() != null) {
            try {
                List<Player> players = PartyAPI.getOnlineMembers(event.getNewParty());
                for (Player a : players) {
                    if (a == event.getPlayer()) {
                        continue;
                    }
                    a.sendMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.GREEN + " has joined your party.");
                }
            } finally {
            }
        }
        
        if (event.getOldParty() != null) {
            try {
                List<Player> players = PartyAPI.getOnlineMembers(event.getOldParty());
                for (Player a : players) {
                    if (a == event.getPlayer()) {
                        continue;
                    }
                    a.sendMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.GREEN + " has left your party.");
                }
            } finally {
            }
        }
        
        String oldp = event.getOldParty();
        String newp = event.getNewParty();
        
        if (oldp == null) {
            oldp = "No party";
        }
        
        if (newp == null) {
            newp = "No party";
        }
        
        for (Player online : PartyAdmin.plugin.getServer().getOnlinePlayers()) {
          if (online.hasPermission("mcmmopartyadmin.spy") || online.isOp())
          {
                String p2 = ChatColor.GRAY + "[Party Change] " + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.GREEN + ": " + oldp + " => " + newp;
                online.sendMessage(p2);
          }
        }
        
    }
    
}

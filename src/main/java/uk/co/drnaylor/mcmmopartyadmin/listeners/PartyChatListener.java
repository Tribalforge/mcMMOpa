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
package uk.co.drnaylor.mcmmopartyadmin.listeners;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.events.chat.McMMOPartyChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;
import uk.co.drnaylor.mcmmopartyadmin.locales.L10n;

public class PartyChatListener implements Listener {

    @EventHandler
    public void PartyChat(McMMOPartyChatEvent event) {
        // For each player online....
        for (Player online : PartyAdmin.getPlugin().getServer().getOnlinePlayers()) {
            
            // Are they a spy?
            if (PartyAdmin.getPlugin().getPartySpyHandler().isSpy(online)) {

                // Are they in the party?
                if (!PartyAPI.inParty(online) || !(PartyAPI.getPartyName(online).equals(event.getParty()))) {
                    
                    // Spy not in the party, so we spy!
                    String p2 = L10n.getString("PartySpy.message", event.getParty(), event.getDisplayName(), event.getMessage());
                    online.sendMessage(p2);

                }
            }
        }
    }
}

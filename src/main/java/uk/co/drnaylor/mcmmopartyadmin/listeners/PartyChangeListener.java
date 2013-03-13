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

import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;
import uk.co.drnaylor.mcmmopartyadmin.locales.L10n;
import uk.co.drnaylor.mcmmopartyadmin.permissions.PermissionHandler;

public class PartyChangeListener implements Listener {

    @EventHandler
    public void PartyChange(McMMOPartyChangeEvent event) {
        String oldp = event.getOldParty();
        String newp = event.getNewParty();

        if (oldp == null) {
            oldp = "No party";
        }

        if (newp == null) {
            newp = "No party";
        }

        for (Player online : PartyAdmin.plugin.getServer().getOnlinePlayers()) {
            if (PermissionHandler.canSpy(online)) {
                String p2 = L10n.getString("PartyChange.change",event.getPlayer().getName(), oldp, newp);
                online.sendMessage(p2);
            }
        }

    }
}

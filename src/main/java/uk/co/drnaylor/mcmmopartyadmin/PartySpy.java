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
package uk.co.drnaylor.mcmmopartyadmin;

import java.util.List;
import org.bukkit.entity.Player;
import uk.co.drnaylor.mcmmopartyadmin.permissions.PermissionHandler;

public class PartySpy {
    
    private List<String> spies;
    
    public PartySpy(List<String> spies) {
        this.spies = spies;
    }
    
    /**
     * Returns the list of spies.
     * 
     * @return List of spies.
     */
    public List<String> getSpies() {
        return spies;
    }
    
    /**
     * Checks to see if a player is a PartySpy
     * 
     * @param player Player to check
     * @return true if so, false otherwise
     */
    public boolean isSpy(Player player) {
        if (PermissionHandler.canSpy(player)) {
            return spies.contains(player.getName());
        }
        else if (spies.contains(player.getName())) {
            // This line removes the player in question if the player doesn't have
            // permission, but is in the spyers file.
            spies.remove(player.getName());
            saveList();
        }
        return false;
    }
    
    /**
     * Toggles the spy state of the player.
     * 
     * @param player Player to toggle
     */
    public void toggleSpy(Player player) {
        if (spies.contains(player.getName())) {
            spies.remove(player.getName());
        }
        else {
            spies.add(player.getName());
        }
    }
    
    /**
     * Reload the config file and get the list of players with PartySpy enabled.
     */
    public void reloadSpies() {
        PartyAdmin.getPlugin().reloadConfig();
        spies = PartyAdmin.getPlugin().getConfig().getStringList("partyspy");
        saveList();
    }
    
    /**
     * Update and save the config file.
     */
    public void saveList() {
        PartyAdmin.getPlugin().getConfig().set("partyspy", spies);
        PartyAdmin.getPlugin().saveConfig();
    }
}

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

import org.bukkit.plugin.java.JavaPlugin;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.Users;
import java.lang.reflect.Method;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

public class PartyAdmin extends JavaPlugin {

    public static PartyAdmin plugin;
    public static mcMMO mcmmo;
    public PartyChangeListener pa;
    public PartyChatListener pc;

    @Override
    public void onEnable() {
        plugin = this;
        if (!isMcmmoAvailable()) {
            this.getServer().getLogger().severe("mcMMO is not loaded on the server.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        this.getServer().getLogger().info("[mcMMO Party Admin] mcMMO hooked.");

        this.getServer().getLogger().info("[mcMMO Party Admin] Checking to see if mcMMO 1.4+ is installed...");
        
        if (!checkForRequiredMethod()) {
            this.getServer().getLogger().severe("[mcMMO Party Admin] mcMMO 1.4 is NOT installed. Disabling.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        this.getServer().getLogger().info("[mcMMO Party Admin] mcMMO 1.4 has been detected.");
        pa = new PartyChangeListener();
        pc = new PartyChatListener();

        getServer().getPluginManager().registerEvents(pa, this);
        getServer().getPluginManager().registerEvents(pc, this);

        getCommand("pa").setExecutor(new PartyAdminCommand(this));
        getCommand("partyspy").setExecutor(new PartySpy(this));
        this.getServer().getLogger().log(Level.INFO, "[mcMMO Party Admin] mcMMO Party Admin {0} is now enabled.", this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        this.getServer().getLogger().info("mcMMO Party Admin is disabling.");
    }
    
    private boolean checkForRequiredMethod() {
   
        // Reflection!
        try {
            Method m = PartyManager.class.getMethod("disbandParty");
            Method n = Users.class.getMethod("getPlayer");
            return ((m != null) && (n != null));
        } catch (Exception e) {
            // doesn't matter
        }
        return false;
    }

    public boolean isMcmmoAvailable() {
        // Checking for mcMMO, just in case
        Plugin _plugin = this.getServer().getPluginManager().getPlugin("mcMMO");

        //If we have found a plugin by the name of "mcMMO", check if it is actually
        //mcMMO. If not, or if we didn't find it, then it's not loaded in.
        if (_plugin == null || !(_plugin instanceof mcMMO)) {
            return false; //Nope, it's not loaded.
        }
        mcmmo = (mcMMO) _plugin;
        return true;
    }
}

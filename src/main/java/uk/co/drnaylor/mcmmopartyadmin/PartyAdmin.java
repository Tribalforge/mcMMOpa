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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.bukkit.plugin.Plugin;

public class PartyAdmin extends JavaPlugin {

    public static PartyAdmin plugin;
    public static mcMMO mcmmo;
    public PartyChangeListener pa;
    public PartyChatListener pc;
    private Properties _props;
    private String _version;

    @Override
    public void onEnable() {
        plugin = this;
        try {
            _props = getPropertiesFromClasspath("resource.properties");
            _version = _props.getProperty("app.version");
        }
        catch (IOException e) {
            _version = "dualspiral";
        }
        if (!isMcmmoAvailable()) {
            this.getServer().getLogger().severe("mcMMO is not loaded on the server.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        this.getServer().getLogger().info("[mcMMO Party Admin] mcMMO hooked.");

        pa = new PartyChangeListener();
        pc = new PartyChatListener();

        getServer().getPluginManager().registerEvents(pa, this);
        getServer().getPluginManager().registerEvents(pc, this);

        getCommand("pa").setExecutor(new PartyAdminCommand(this));
        getCommand("partyspy").setExecutor(new PartySpy(this));
        this.getServer().getLogger().info("[mcMMO Party Admin] mcMMO Party Admin " + _version + " is now enabled.");
    }

    @Override
    public void onDisable() {
        this.getServer().getLogger().info("mcMMO Party Admin is disabling.");
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

    public Properties getPropertiesFromClasspath(String propFileName) throws IOException {
        // loading xmlProfileGen.properties from the classpath
        Properties props = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
        //.getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + propFileName
                    + "' not found in the classpath");
        }

        props.load(inputStream);

        return props;
    }
}

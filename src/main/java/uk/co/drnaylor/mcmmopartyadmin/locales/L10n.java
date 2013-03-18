/* 
 * Copyright (C) 2013 Dr Daniel R. Naylor, portions copyright the mcMMO team.
 * Thanks to them for making their I18n code open source!
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
package uk.co.drnaylor.mcmmopartyadmin.locales;

import com.gmail.nossr50.locale.LocaleLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.bukkit.ChatColor;
import uk.co.drnaylor.mcmmopartyadmin.PartyAdmin;

public final class L10n {

    private static ResourceBundle bundle = null;
    private static ResourceBundle enBundle = null;
    
    public static String getString(String key) {
        return getString(key, (Object[]) null);
    }

    /**
     * Gets the appropriate string from the Locale files.
     *
     * @param key The key to look up the string with
     * @param messageArguments Any arguments to be added to the string
     * @return The properly formatted locale string
     */
    public static String getString(String key, Object... messageArguments) {
        if (bundle == null) {
            init();
        }

        try {
            return getString(key, bundle, messageArguments);
        }
        catch (MissingResourceException ex) {
            try {
                return getString(key, enBundle, messageArguments);
            }
            catch (MissingResourceException ex2) {
                return '!' + key + '!';
            }
        }
    }
    
    
    private static String getString(String key, ResourceBundle bundle, Object... messageArguments) throws MissingResourceException {
        String output = bundle.getString(key);

        if (messageArguments != null) {
            MessageFormat formatter = new MessageFormat("");
            formatter.applyPattern(output);
            output = formatter.format(messageArguments);
        }

        output = addColours(output);

        return output;
    }
    
    /**
     * Initalizes the locale loader. This simply attempts to hook into mcMMO's
     * locale, and provides a fallback of en-GB (NOT en-US)
     */
    private static void init() {
        // Get Locale from mcMMO
        try {
            Locale locale = LocaleLoader.getCurrentLocale();
            bundle = ResourceBundle.getBundle("uk.co.drnaylor.mcmmopartyadmin.locales.messages", locale);
        }
        catch (NoSuchMethodError e) {
            // Can't localise a message here, but as we know we are alling back to en_GB, it matters not.
            PartyAdmin.getPlugin().getServer().getLogger().warning("[mcMMO Party Admin] Locale could not be detected. Falling back to en_GB");
            bundle = ResourceBundle.getBundle("uk.co.drnaylor.mcmmopartyadmin.locales.messages", Locale.UK);
        }
        enBundle = ResourceBundle.getBundle("uk.co.drnaylor.mcmmopartyadmin.locales.messages", Locale.UK);
    }
    
    /**
     * Converts colour markers in the resources file to server colours. Uses the
     * same method as the mcMMO guys, to try and keep things simple.
     * 
     * @param input String to convert
     * @return Converted string
     */
    private static String addColours(String input) {
        input = input.replaceAll("\\Q[[BLACK]]\\E", ChatColor.BLACK.toString());
        input = input.replaceAll("\\Q[[DARK_BLUE]]\\E", ChatColor.DARK_BLUE.toString());
        input = input.replaceAll("\\Q[[DARK_GREEN]]\\E", ChatColor.DARK_GREEN.toString());
        input = input.replaceAll("\\Q[[DARK_AQUA]]\\E", ChatColor.DARK_AQUA.toString());
        input = input.replaceAll("\\Q[[DARK_RED]]\\E", ChatColor.DARK_RED.toString());
        input = input.replaceAll("\\Q[[DARK_PURPLE]]\\E", ChatColor.DARK_PURPLE.toString());
        input = input.replaceAll("\\Q[[GOLD]]\\E", ChatColor.GOLD.toString());
        input = input.replaceAll("\\Q[[GRAY]]\\E", ChatColor.GRAY.toString());
        input = input.replaceAll("\\Q[[DARK_GRAY]]\\E", ChatColor.DARK_GRAY.toString());
        input = input.replaceAll("\\Q[[BLUE]]\\E", ChatColor.BLUE.toString());
        input = input.replaceAll("\\Q[[GREEN]]\\E", ChatColor.GREEN.toString());
        input = input.replaceAll("\\Q[[AQUA]]\\E", ChatColor.AQUA.toString());
        input = input.replaceAll("\\Q[[RED]]\\E", ChatColor.RED.toString());
        input = input.replaceAll("\\Q[[LIGHT_PURPLE]]\\E", ChatColor.LIGHT_PURPLE.toString());
        input = input.replaceAll("\\Q[[YELLOW]]\\E", ChatColor.YELLOW.toString());
        input = input.replaceAll("\\Q[[WHITE]]\\E", ChatColor.WHITE.toString());
        input = input.replaceAll("\\Q[[BOLD]]\\E", ChatColor.BOLD.toString());
        input = input.replaceAll("\\Q[[UNDERLINE]]\\E", ChatColor.UNDERLINE.toString());
        input = input.replaceAll("\\Q[[ITALIC]]\\E", ChatColor.ITALIC.toString());
        input = input.replaceAll("\\Q[[STRIKE]]\\E", ChatColor.STRIKETHROUGH.toString());
        input = input.replaceAll("\\Q[[MAGIC]]\\E", ChatColor.MAGIC.toString());
        input = input.replaceAll("\\Q[[RESET]]\\E", ChatColor.RESET.toString());

        return input;
    }
    
    
}

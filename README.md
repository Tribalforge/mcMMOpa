mcMMO Party Admin
=================

Party Admin tool for mcMMO

## What is it?

mcMMO Party Admin is a plugin that snaps into mcMMO’s party system. Using this plugin, and thanks to mcMMO’s API, Admins can now spy on party chat, remove parties, and move players between different parties. Admins can also chat to parties they are not part of themselves.

mcMMO has also been known to duplicate player entries in the party table. A ````/fixparties```` command has been added to alliviate this as of the v0.5 snapshots, which checks for and removes duplicate player entries from the party lists.

## Plugin Information

This plugin is licensed under the GPLv3 license. Basically, you are free to use and modify it, but your source code must be published with any derivative works under the same license.

This license is used as this plugin has used code from the base mcMMO plugin, which is GPLv3 in itself.

#### To function, this plugin requires:

* Server that runs the Bukkit API 1.4.7-R1.0 or above
* mcMMO 1.4.00+

#### Extra features can be activated when this plugin has the following installed:

* SuperPerms compatible Permissions System

## Installation & Configuration

Installation is as simple as putting the plugin into your plugin folder, and starting your server.

There are currently no configuation options.

## Permissions

* ````mcmmopartyadmin.commands.partyadmin```` – Allows player to use the party admin commands
* ````mcmmopartyadmin.admin```` – Allows player to use server admin functions
* ````mcmmopartyadmin.spy```` – Allows player to spy on party chat

## Usage

#### /partyadmin
<small>Requires ````mcmmopartyadmin.commands.partyadmin```` - Alias /pa.</small>

Accesses the Party Admin commands:

* ````/partyadmin rp [party]```` – Delete party [party]
* ````/partyadmin apl [player] [party]```` – Add [player] to [party] - _NB: the player must be online_
* ````/partyadmin rpl [player]```` – Remove [player] from current party. _NB: the player must be online_
* ````/partyadmin chown [player] [party]```` – Change owner of [party] to [player]
* ````/partyadmin pc [party] [message]```` – Chat to [party]

#### /partyspy
<small>Requires ````mcmmopartyadmin.spy````</small>.

Toggles Party Spy on or off (Note that PartySpy defaults to OFF, and players MUST have the ````mcmmopartyadmin.spy```` permission in order for any setting here to take effect!)

* ````/partyspy```` – Requires ````mcmmopartyadmin.spy```` – Toggle party spy on or off.
* ````/partyspy save```` – Requires ````mcmmopartyadmin.admin```` – Force save the spyers file.
* ````/partyspy reload```` – Requires ````mcmmopartyadmin.admin```` – Reloads the spyers file from disc.

#### /fixparties
<small>Requires ````mcmmopartyadmin.commands.partyadmin````.</small>

Removes any duplicate players in the party lists.

## More Information

See http://plugins.tribalforge.co.uk/mcmmo-party-admin/ for more information and binary downloads.

Please note that this software is in active development, and I hope to make it completely dependant on the mcMMO API before hitting the big 1.0. This plugin uses reflection to ensure the required methods are there, and will disable if it detects they are not.

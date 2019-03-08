# DiscordWhitelist [WIP]
DiscordWhitelist is a plugin for Spigot 1.13 designed to allow server owners to whitelist users based on a message sent via Discord.

## Features
- Check users have a specific Discord role
- Allows admins to disable the whitelisting feature

## Permissions
There is only one permission for the plugin:
`discordwhitelist.admin`
This permission allows a player to disable the whitelisting function of this plugin (effectively disabling it).

##Requirements
This plugin requires [Vault](https://www.spigotmc.org/resources/vault.34315/).

## Installation
To use the plugin, you must first [register an application](https://discordapp.com/developers/applications/) with the Discord API website. Once you've done this, you will need to grab your application's `clientID` and `clientSecret` tokens and enter them into the `config.yml` file found within the plugin's data folder.

Once you've done that, the server operator will need to put their Discord into 'Developer Mode'. If you're not sure how to do this, [here's how](https://support.discordapp.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-).

Once in Developer Mode, the owner can right click elements on Discord to get their IDs. The important ones are:

- `ownerID` - This is the server owner's personal ID. This allows the plugin to recognise who can use commands with the bot directly inside Discord.
- `roleID` - This is the role that you want to check for in order to allow a player to be whitelisted.
- `channelID` - This is the channel in which the bot will send the messages to whitelist players.

## Usage
To get started, make sure the plugin is running on the server. If you have set up the config correctly, the bot will need to be invited into your Discord server. To do this, simply replace `CLIENTID` in the URL below, and then load that link in a browser.
*Please note: this requires you to have server administrator on the Discord server you wish to add the bot to.*
```
https://discordapp.com/oauth2/authorize?client_id=CLIENTID&scope=bot
```
Once you've done this, the bot will now show up in the userlist of your Discord server.

If you have set the `channelID` correctly, the bot should automatically send a message saying *'DiscordWhitelist enabled'*.

Now, when a user type `!whitelist <minecraftusername>` the bot should automatically check whether they have the role specified in the config file.
If they do, the bot will automatically whitelist the user on the server (provided it is a valid username, of course).

Once the bot whitelists a user, it will automatically reload the whitelist.

Once this has been completed, the bot will respond to the user informing them that they have been whitelisted.

## Planned Features
- Bungeecord support
- Authcode-based whitelisting

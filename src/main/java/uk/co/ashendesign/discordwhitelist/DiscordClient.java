package uk.co.ashendesign.discordwhitelist;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import static org.bukkit.Bukkit.getLogger;

public class DiscordClient {

    public static void initialise(String token) {

        System.out.println("Bot is being created!");

        if(!token.equalsIgnoreCase("CLIENTID_HERE")){
            client(token);
        } else {
            getLogger().severe("Bot does not have a token assigned!");
            Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        }
    }

    public static void client(String token){
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        getLogger().info("Bot has connected to the Discord server.");

        api.addMessageCreateListener(event -> {
            if(event.getMessageContent().equalsIgnoreCase("!notping")) {
                event.getChannel().sendMessage("NOT PONG!");
            } else {
                //Do something
            }
        });
    }
}

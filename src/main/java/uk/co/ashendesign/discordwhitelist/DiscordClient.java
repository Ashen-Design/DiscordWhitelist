package uk.co.ashendesign.discordwhitelist;

import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import static org.bukkit.Bukkit.*;

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

        boolean whitelistEnabled = Main.getInstance().getConfig().getBoolean("enabled");

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        getLogger().info("Bot has connected to the Discord server.");

        api.addMessageCreateListener(event -> {
            if(event.getMessageContent().equalsIgnoreCase("!notping")) {
                event.getChannel().sendMessage("NOT PONG!");
            } else if(event.getMessageContent().contains("!whitelist") && whitelistEnabled) {
                //If user sends 'whitelist command' run this:
                //Gets the full message contents
                String message = event.getMessageContent();
                //Splits message on spaces to get the username.
                String[] args = message.split(" ");

                if(args.length < 1){
                    event.getChannel().sendMessage("You haven't provided a username. Please use !whitelist <username>");
                }else{

                    //Sends the whitelisting commands via the console.
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.dispatchCommand(getConsoleSender(),"whitelist add " + args[1]));
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.dispatchCommand(getConsoleSender(), "whitelist reload"));

                    //Informs the user in Discord that the username has been whitelisted.
                    event.getChannel().sendMessage("Successfully whitelisted: " + args[1]);

                    event.addReactionToMessage("✅");
                }
            }else if(event.getMessageContent().contains("!whitelist") && !whitelistEnabled) {
                event.getChannel().sendMessage("Whitelisting is currently disabled.");
            }
        });
    }
}

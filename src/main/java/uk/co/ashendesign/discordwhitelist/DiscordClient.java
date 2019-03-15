package uk.co.ashendesign.discordwhitelist;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.bukkit.Bukkit.*;

public class DiscordClient {

    private JSONArray userList;
    private File userFile;

    public static void initialise(String token) {

        System.out.println("Bot is being created!");

        if(!token.equalsIgnoreCase("CLIENTID_HERE")){
            client(token);
        } else {
            getLogger().severe("Bot does not have a valid token assigned!");
            Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        }
    }

    private static void client(String token){

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

    private void loadUsers() {
        // Load the parser
        JSONParser parser = new JSONParser();

        //Try to read the userlist file.
        try (FileReader reader = new FileReader("userlist.json")) {

            Object obj = parser.parse(reader);

            //Saves the contents of the existing file to the userList variable to work with later.
            userList = (JSONArray) obj;


        } catch (FileNotFoundException e) {
            System.out.println("userlist.json is missing! Creating now...");

            try {

                userFile = new File(Main.getInstance().getDataFolder(), "userlist.json");

            } catch  (Exception ex) {
                System.out.println("Could not create the file! Shutting down!");
                Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
            }
        } catch (IOException e) {

            e.printStackTrace();

        }catch (ParseException e) {

            e.printStackTrace();
        }
    }

    private static void addUser(String userID, String minecraftID) {


    }
}

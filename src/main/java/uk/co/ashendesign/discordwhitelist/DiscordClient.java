package uk.co.ashendesign.discordwhitelist;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.*;

import static org.bukkit.Bukkit.*;

public class DiscordClient {

    public static JSONArray userList;
    private static File userFile;

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

        loadUsers();
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

                    addUser(event.getMessageAuthor().getIdAsString(), args[1]);
                    System.out.println("Currently whitelisting: " + event.getMessageAuthor().getId());

                    //Informs the user in Discord that the username has been whitelisted.
                    event.getChannel().sendMessage("Successfully whitelisted: " + args[1]);

                    event.addReactionToMessage("✅");
                }
            }else if(event.getMessageContent().contains("!whitelist") && !whitelistEnabled) {
                event.getChannel().sendMessage("Whitelisting is currently disabled.");
            }
        });
    }

    private static void loadUsers() {
        // Load the parser
        JSONParser parser = new JSONParser();

        userFile = new File(Main.getInstance().getDataFolder(), "userlist.json");

        //Try to read the userlist file.
        try (FileReader reader = new FileReader(userFile)) {

            Object obj = parser.parse(reader);

            //Saves the contents of the existing file to the userList variable to work with later.
            userList = (JSONArray)obj;


        } catch (FileNotFoundException e) {
            System.out.println("userlist.json is missing!");

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }catch (ParseException e) {

            e.printStackTrace();
        }
    }

    private static void addUser(String userID, String minecraftID) {

            JSONObject userDetails = new JSONObject();

            userDetails.put("UserID", userID);
            userDetails.put("minecraftID", minecraftID);

            userList.add(userDetails);

            try (FileWriter file = new FileWriter(userFile)) {
                file.write(userList.toJSONString());
                file.flush();
                loadUsers();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}

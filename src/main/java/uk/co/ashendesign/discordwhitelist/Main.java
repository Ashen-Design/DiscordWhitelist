package uk.co.ashendesign.discordwhitelist;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin{

    private File configFile;
    private FileConfiguration customConfig;


    private static Main instance;
    public static Main getInstance() { return instance; }

    public static Main saveConfigInstance() {
        instance.saveConfig();
        return instance;
    }

    @Override
    public void onEnable(){

        //Register whitelist command
        this.getCommand("discordwhitelist").setExecutor(new CommandWhitelist());

        //Run method to create config file.
        createCustomConfig();

        instance = this;
    }

    public FileConfiguration getCustomConfig(){
        return this.customConfig;
    }

    //Create the initial config file for use in the plugin.
    private void createCustomConfig(){
        configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()){
            configFile.getParentFile().mkdirs();
            saveResource("config.yml",false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable(){

    }
}

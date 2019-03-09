package uk.co.ashendesign.discordwhitelist;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

//Import Vault permissions management
import net.milkbowl.vault.permission.Permission;


public class Main extends JavaPlugin{

    private File configFile;
    private FileConfiguration customConfig;

    private static final Logger log = Logger.getLogger("Minecraft");

    public static Permission perms = null;

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

        //Enable Permissions
        if(!setupPermissions()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
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

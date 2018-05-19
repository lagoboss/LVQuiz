package com.gmail.helplagoverse.LVQuiz;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by lake.smith on 5/16/2018.
 */
public class Main extends JavaPlugin implements Listener{

    public static Economy econ;

    //will be executed when the plugin starts
    @Override
    public void onEnable(){

        //checks if Vault is installed
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //saves default config file from plugin jar
        this.saveDefaultConfig();
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));



        //allows plugin to register events
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("LVQuiz").setExecutor(new CommandProcessor(this));
    }

    //will be executed when the plugin stops

    @Override
    public void onDisable(){
        this.saveConfig();

    }

    //allows plugin to use Vault
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcononomy() {
        return econ;
    }

}

package me.architettoplugin.csvaddon.utils;

import me.architettoplugin.csvaddon.CSVaddon;
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.configuration.file.YamlConfiguration;



import java.io.File;

import java.io.IOException;
import java.util.Objects;


public class CustomConfigUtils {


    private static File file;
    private static FileConfiguration customFile;

    //Finds or generates the custom config file

    public static void setup(){

        file = new File( Objects.requireNonNull ( Bukkit.getServer ().getPluginManager ().getPlugin ( "CSVaddon" ) ).getDataFolder(), "csvTownList.yml");
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }



    public static FileConfiguration get(){
        return customFile;
    }



    public static void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            System.out.println("Impossibile salvare il file!");
        }
    }


    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }




}
package me.architettoplugin.csvaddon.utils;


import com.palmergames.bukkit.towny.TownyUniverse;

import com.palmergames.bukkit.towny.object.Town;
;
import me.architettoplugin.csvaddon.CSVaddon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


public class CmdUtils {

    private static final File file = new File("plugins/CSVaddon", "csvTownList.yml");
    private static final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static void AUTOremoveTownCustomConfig(String townName) {
        if(cfg.contains ( townName )){
            cfg.set ( townName,null);
            try {
                cfg.save(file);
            } catch (IOException e) {
                System.out.println ("Impossibile salvare il File csvTownList");
            }
        }


    }


    //Restituisce la lista di tutte le città presenti sul piano
    public ArrayList<String> getTownList (){
        ArrayList<String> listaTown = new ArrayList<String> ();
        for (Town town : TownyUniverse.getInstance ().getDataSource ().getTowns ()){
            listaTown.add(town.getName());
        }
        return listaTown;
    }



    //Inserisce la locazione CSV di una città
    public static void  insTownCustomConfig (String nomeCity, Location loc,CommandSender sender){
        int defaultBoost = 1;
        if (!cfg.contains ( nomeCity )) {
            cfg.createSection ( nomeCity );
            cfg.createSection ( nomeCity+".Posizione" );
            cfg.set ( nomeCity+".Posizione",LocationUtils.LightStringFromLoc ( loc ) );
            cfg.createSection ( nomeCity+".Boost" );
            cfg.set (nomeCity+".Boost", defaultBoost);
            try {
                cfg.save(file);
                sender.sendMessage ( "CSV aggiunta correttamente!" );
            } catch (IOException e) {
                System.out.println ("Impossibile salvare il File csvTownList");
            }

        }else
            sender.sendMessage ( "La citta' "+nomeCity+" possiede gia' una CSV!" );
    }


    //Modifica il valore del boost
    public static void setTownBoost(String nomeCity, int boost, CommandSender sender){
        if(cfg.contains ( nomeCity )) {
            cfg.set ( nomeCity + ".Boost", boost );
            sender.sendMessage ( "La citta' "+nomeCity+" ha un boost pari a : "+boost );
            try {
                cfg.save(file);
            } catch (IOException e) {
                System.out.println ("Impossibile salvare il File csvTownList");
            }
        }
    }


    public static int getTownBoost(String nomeCity){
        int boost = 0;
        if(cfg.contains ( nomeCity )) {
            boost = cfg.getInt ( nomeCity + ".Boost" );
        }
        return boost;
    }



    public static String getLocationCSV (String nomeCity) {
        String Sloc = "";
        if (cfg.contains ( nomeCity )) {
            Sloc = cfg.getString ( nomeCity + ".Posizione" );
        }
        return Sloc;

    }


    //Modifica la posizione di una CSV


    public static void  removeTownCustomConfig (String nomeCity,CommandSender sender){
        if(cfg.contains ( nomeCity )){
            cfg.set ( nomeCity,null);
            sender.sendMessage ( "CSV di "+ nomeCity +" rimossa correttamente !" );
            try {
                cfg.save(file);
            } catch (IOException e) {
                System.out.println ("Impossibile salvare il File csvTownList");
            }
        }
    }




    public List<String> getListaCityConCSV (){

        return new ArrayList<> ( cfg.getKeys ( false ) );
    }




}

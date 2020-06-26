package me.architettoplugin.csvaddon;

import it.sauronsoftware.cron4j.Scheduler;
import me.architettoplugin.csvaddon.command.CommandsEx;
import me.architettoplugin.csvaddon.listener.onDeleteTownEvent;
import me.architettoplugin.csvaddon.listener.onNewTownEvent;
import me.architettoplugin.csvaddon.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;



import java.io.File;
import java.util.*;


public final class CSVaddon extends JavaPlugin {

    public static List<String> listaCityPiano;
    public static List<String> listaCityConCSV;
    public static List<Material> ALLmaterials;
    public static Economy econ = null;

    public static HashMap<String,Integer> mapMaterialsValue = new HashMap<> ();
    public static int minvaluematerial;
    public static int maxvaluematerial;


    private static final String pathMaterialsValue = "plugins/CSVaddon/MaterialsValue.txt";


    private static Plugin plugin;


    @Override
    public void onEnable() {
        // Plugin startup logic

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;



        getConfig ().options ().copyDefaults ();
        saveDefaultConfig ();

        //Prendo dal config il valore minimo e massimo che può essere inserito ai materiali vendibili
        minvaluematerial = getConfig ().getInt ( "Min material value" );
        maxvaluematerial = getConfig ().getInt ( "Max material value" );
        boolean scheduletoggle = getConfig ().getBoolean ( "Auto-sell toggle" );


        CustomConfigUtils.setup();
        CustomConfigUtils.get().options().copyDefaults(true);
        CustomConfigUtils.save();

        //Carico alcune variabili che mi servono
        listaCityPiano = new CmdUtils ().getTownList ();
        listaCityConCSV = new CmdUtils ().getListaCityConCSV ();
        ALLmaterials = ( Arrays.asList ( Material.values () ) );


        File MaterialsValue = new File(pathMaterialsValue);
        try {
            MaterialsValue.createNewFile ();
            if(MaterialsValue.length ()!=0)
                mapMaterialsValue = SLAPI.load ( pathMaterialsValue );

        } catch (Exception e) {
            e.printStackTrace ();
        }

        // TEST TEST TEST TEST

        if(scheduletoggle) {
            String scheduledate = getConfig ().getString ( "Auto-sell time" );
            System.out.println ("[CSVaddon] : Auto-sell attivo .");
            MyTask task = new MyTask ();
            Scheduler scheduler = new Scheduler ();
            scheduler.schedule ( scheduledate, task );
            scheduler.start ();
        }else
            System.out.println ("[CSVaddon] : Auto-sell non attivo .");

        // TEST TEST TEST TEST










        //Listeners
        this.getServer ().getPluginManager ().registerEvents ( new onDeleteTownEvent (),this);
        this.getServer ().getPluginManager ().registerEvents ( new onNewTownEvent (),this);

        //Commands
        getCommand ( "CSVaddon" ).setExecutor ( new CommandsEx () );

        //TabCompleter
        //ToDo


    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            if(!mapMaterialsValue.isEmpty ())
            SLAPI.save ( mapMaterialsValue,pathMaterialsValue );
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}




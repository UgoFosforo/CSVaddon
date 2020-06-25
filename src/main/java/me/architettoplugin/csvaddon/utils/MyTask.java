package me.architettoplugin.csvaddon.utils;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import me.architettoplugin.csvaddon.CSVaddon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MyTask extends Task {

    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {

        Bukkit.getScheduler().runTaskLater(CSVaddon.getPlugin(), new Runnable() {
            @Override
            public void run() {

                if(CSVaddon.listaCityConCSV.size ()==0)
                    return;

                for (int i =0;i< CSVaddon.listaCityConCSV.size ();i++){

                    String StringCSVLocation = CmdUtils.getLocationCSV ( CSVaddon.listaCityConCSV.get ( i ) );
                    int boostCSV = CmdUtils.getTownBoost ( CSVaddon.listaCityConCSV.get ( i ) );
                    Location CSVLocation = LocationUtils.LightLocFromString ( StringCSVLocation );
                    Block block = CSVLocation.getBlock ();

                    if (block.getType () != Material.CHEST)
                        System.out.println ("Nella citta' di " + CSVaddon.listaCityConCSV.get ( i ) + " non e' stata trovata la CSV alle coordinate indicate !");
                    else {
                        Inventory inv = ((BlockInventoryHolder) block.getState ()).getInventory ();
                        ItemStack[] listaitems = inv.getContents ();

                        // TEST TEST TEST TEST
                        try {
                            Town town = TownyAPI.getInstance ().getTownBlock ( CSVLocation ).getTown ();
                            int valore = SellUtils.SVsell (listaitems, boostCSV);

                            town.getAccount ().collect ( valore , "CSV payday" );
                            System.out.println ( ChatColor.GOLD + "GUADAGNO OTTENUTO da " + CSVaddon.listaCityConCSV.get ( i ) + " : " + ChatColor.WHITE + valore + " zenar." );

                        } catch (EconomyException | NotRegisteredException e) {
                            e.printStackTrace ();
                        }

                        // TEST TEST TEST TEST


                        //ToDo: sarebbe meglio eliminare solo gli items effettivamente venduti.
                        inv.clear ();

                    }
                }

                TownyMessaging.sendGlobalMessage("GIORNO DI PAGA !");            }
        }, 20L);
    }


}


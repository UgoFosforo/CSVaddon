package me.architettoplugin.csvaddon.utils;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import me.architettoplugin.csvaddon.CSVaddon;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;




public class SellUtils {


    public static void sellall(CommandSender sender) throws NotRegisteredException, EconomyException {

        for (int i =0;i< CSVaddon.listaCityConCSV.size ();i++){

           String StringCSVLocation = CmdUtils.getLocationCSV ( CSVaddon.listaCityConCSV.get ( i ) );
           int boostCSV = CmdUtils.getTownBoost ( CSVaddon.listaCityConCSV.get ( i ) );
           Location CSVLocation = LocationUtils.LightLocFromString ( StringCSVLocation );
           Block block = CSVLocation.getBlock ();

            if (block.getType () != Material.CHEST)
                sender.sendMessage ("Nella citta' di " + CSVaddon.listaCityConCSV.get ( i ) + " non e' stata trovata la CSV alle coordinate indicate !");
            else {
                Inventory inv = ((BlockInventoryHolder) block.getState ()).getInventory ();
                ItemStack[] listaitems = inv.getContents ();

                // TEST TEST TEST TEST
                Town town = TownyAPI.getInstance ().getTownBlock ( CSVLocation ).getTown ();
                int valore = SVsell(listaitems, boostCSV);
                town.getAccount ().collect ( valore , "CSV payday" );

                // TEST TEST TEST TEST




                sender.sendMessage ( ChatColor.GOLD + "GUADAGNO OTTENUTO da " + CSVaddon.listaCityConCSV.get ( i ) + " : " + ChatColor.WHITE + valore + " zenar." );


                //ToDo: sarebbe meglio eliminare solo gli items effettivamente venduti.
                inv.clear ();
                //ToDo : ovviamente va collegato a Vault... non ho ancora idea di come ....

            }




        }




    }

    public static void sell (String CityName , CommandSender sender) throws NotRegisteredException, EconomyException {

        String StringCSVLocation = CmdUtils.getLocationCSV ( CityName );
        int boostCSV = CmdUtils.getTownBoost ( CityName );
        Location CSVLocation = LocationUtils.LightLocFromString ( StringCSVLocation );
        Block block = CSVLocation.getBlock ();

        if (block.getType () != Material.CHEST)
            sender.sendMessage ("Nella citta' di " + CityName + " non e' stata trovata la CSV alle coordinate indicate !");
        else {
            Inventory inv = ((BlockInventoryHolder) block.getState ()).getInventory ();
            ItemStack[] listaitems = inv.getContents ();

            Town town = TownyAPI.getInstance ().getTownBlock ( CSVLocation ).getTown ();
            int valore = SVsell(listaitems, boostCSV);
            town.getAccount ().collect ( valore , "CSV payday" );





            sender.sendMessage ( ChatColor.GOLD + "GUADAGNO OTTENUTO da " + CityName + " : " + ChatColor.WHITE + valore + " zenar." );




            inv.clear ();

        }






    }


    private static int SVsell(ItemStack[] listaitems, int boost){

        int totalvalue = 0;

        for (ItemStack listaitem : listaitems) {
            if (listaitem != null) {


                if( CSVaddon.mapMaterialsValue.containsKey ( listaitem.getType ().name ())){

                    totalvalue = totalvalue + listaitem.getAmount () * CSVaddon.mapMaterialsValue.get ( listaitem.getType ().name () ) * boost ;

                }

            }
        }




        return totalvalue ;
    }




}

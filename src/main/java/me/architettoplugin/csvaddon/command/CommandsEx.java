package me.architettoplugin.csvaddon.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.*;
import me.architettoplugin.csvaddon.CSVaddon;
import me.architettoplugin.csvaddon.utils.LocationUtils;
import me.architettoplugin.csvaddon.utils.CmdUtils;
import me.architettoplugin.csvaddon.utils.SellUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsEx implements CommandExecutor {

    private Town town;
    private boolean IsInMyTown = false;
    private String townName;
    private Resident resident;




    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        if(!(sender instanceof Player)) {
            System.out.println ( "I comandi non sono utilizzabili dal terminale!" );
            return true;
        }

        Player player = (Player)sender;

        try {

            resident = TownyAPI.getInstance ().getDataSource ().getResident ( player.getName () );
            if (resident.hasTown ()) {
                town = resident.getTown ();
                townName = town.getName ();
                if(TownyAPI.getInstance().isWilderness(player.getLocation()))
                    IsInMyTown = false;
                else {
                    if (resident.getTown () == TownyAPI.getInstance ().getTownBlock ( player.getLocation () ).getTown ())
                        IsInMyTown = true;
                }
            }
        } catch (NotRegisteredException e) {
            e.printStackTrace ();
        }







        switch (args[0].toLowerCase ()) {
            case "chest":



                switch ( (args[1]).toLowerCase ()){
                    case "set":

                        if (!sender.hasPermission ( "csvperm.chest.set" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if (!resident.hasTown ()){
                            sender.sendMessage ( "Non appartieni ad alcuna citta' !" );
                            return true;
                        }

                        if (!town.getMayor ().getName ().equals ( player.getName () )){
                            sender.sendMessage ( "Solo il sindaco può settare una CSV !" );
                            return true;
                        }

                        if(IsInMyTown){
                            Block csv = LocationUtils.getSelectedBlock ( player );
                            if(csv.getType () != Material.CHEST) {
                                sender.sendMessage ( "Il blocco target deve essere una chest !" );
                                return true;
                            }
                            Location loc = csv.getLocation ();
                            try {
                                if (TownyAPI.getInstance ().isWilderness ( loc ) || !TownyAPI.getInstance ().getTownBlock ( loc ).getTown ().getName ().equals ( townName )){
                                    sender.sendMessage ( "La CSV è in una posizione inadeguata >:-( ! Ci hai provato." );
                                return true;
                                }

                            } catch (NotRegisteredException e) {
                                e.printStackTrace ();
                            }

                            CmdUtils.insTownCustomConfig ( townName, loc, sender );
                            player.playSound ( player.getLocation (), Sound.ENTITY_SHULKER_OPEN, 3, 1  );
                            CSVaddon.listaCityConCSV = new CmdUtils ().getListaCityConCSV ();



                        }

                        return true;

                    case "remove":
                        if (!sender.hasPermission ( "csvperm.chest.remove" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if (!resident.hasTown ()){
                            sender.sendMessage ( "Non appartieni ad alcuna citta' !" );
                            return true;
                        }

                        if (!town.getMayor ().getName ().equals ( player.getName () )){
                            sender.sendMessage ( "Solo il sindaco può rimuovere una CSV !" );
                            return true;
                        }

                        CmdUtils.removeTownCustomConfig ( townName,sender );
                        player.playSound ( player.getLocation (), Sound.ENTITY_EGG_THROW, 3, 1  );
                        CSVaddon.listaCityConCSV = new CmdUtils ().getListaCityConCSV ();
                        sender.sendMessage ( "Chest rimossa correttamente !" );
                        return true;

                    case "info":
                        if (!sender.hasPermission ( "csvperm.chest.info" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if (!resident.hasTown ()){
                            sender.sendMessage ( "Non appartieni ad alcuna citta' !" );
                            return true;
                        }

                        String SCVmyTownLoc = new CmdUtils ().getLocationCSV ( townName );
                        if(SCVmyTownLoc.isEmpty ())
                            sender.sendMessage ( "La tua citta' non possiede nessuna CSV !" );
                        else {
                            sender.sendMessage ( SCVmyTownLoc );
                        }


                        return true;

                    default:
                        return true;
                }




            case "boost":
                switch (args[1].toLowerCase ()){

                    case "set":

                        if (!sender.hasPermission ( "csvperm.boost.set" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if (args.length!=4){
                            sender.sendMessage ( "Non hai iserito correttamente i parametri del comando !" );
                            return true;
                        }

                        if(!CSVaddon.listaCityPiano.contains ( args[2] ) || !CSVaddon.listaCityConCSV.contains ( args[2] )){
                            sender.sendMessage ( "La citta' inserita non esiste oppure non possiede alcuna CSV !" );
                            return true;
                        }

                        CmdUtils.setTownBoost ( args[2], Integer.parseInt ( args[3] ), sender );
                        return true;

                    case "info":


                        if (!sender.hasPermission ( "csvperm.boost.info" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if (args.length!=3){
                            sender.sendMessage ( "Non hai iserito correttamente i parametri del comando !" );
                            return true;
                        }

                        if(!CSVaddon.listaCityPiano.contains ( args[2] ) || !CSVaddon.listaCityConCSV.contains ( args[2] )){
                            sender.sendMessage ( "La citta' inserita non esiste oppure non possiede alcuna CSV !" );
                            return true;
                        }

                        sender.sendMessage ( "La citta "+ args[2] + "possiede un boost pari a : " +  CmdUtils.getTownBoost ( args[2] ) );
                        return true;


                    default:
                        return true;

                }



            case "materials":

                switch (args[1].toLowerCase ()){

                    case "set":

                        if (!sender.hasPermission ( "csvperm.materials.set" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if(args.length!=4) {
                            sender.sendMessage ( "Non hai iserito correttamente i parametri del comando !" );
                            return true;
                        }

                        if(!CSVaddon.ALLmaterials.toString ().contains ( args[2] )) {
                            sender.sendMessage ( "Non esiste un materiale di quel tipo !" );
                            return true;
                        }


                        try {
                            Double.parseDouble(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("L'ultimo parametro deve eseere un numero !");
                            return true;
                        }


                        //ToDo : Impostare da config il valore max e minimo
                        //ToDo : da cambiare!
                        if(Integer.parseInt ( args[3] )<=0 || Integer.parseInt ( args[3] )>=1000){
                            sender.sendMessage ( "Il valore inserito è troppo alto o troppo basso !" );
                            return true;
                        }

                        CSVaddon.mapMaterialsValue.put(args[2],Integer.parseInt(args[3]));
                        sender.sendMessage ( "Il valore è stato inserito correttamente" );


                        return true;




                    case "remove":

                        if (!sender.hasPermission ( "csvperm.materials.remove" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if(args.length!=3) {
                            sender.sendMessage ( "Non hai iserito correttamente i parametri del comando !" );
                            return true;
                        }

                        if(!CSVaddon.ALLmaterials.toString ().contains ( args[2] )) {
                            sender.sendMessage ( "Non esiste un materiale di quel tipo !" );
                            return true;
                        }

                        if(!CSVaddon.mapMaterialsValue.containsKey ( args[2] )){
                            //Se il materiale non è presente nell'hashmap non fa nulla.
                            return true;
                        }

                        CSVaddon.mapMaterialsValue.remove ( args[2] );
                        return true;




                    case "list":

                        if (!sender.hasPermission ( "csvperm.materials.list" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }


                        if(CSVaddon.mapMaterialsValue.isEmpty ()){
                            sender.sendMessage ( "Al momento nessun materiale ha valore !" );
                            return true;
                        }

                        //Todo: Formattare meglio l'uscita.
                        sender.sendMessage ( CSVaddon.mapMaterialsValue.toString ());
                        return true;




                    default:
                        return true;

                }

            case "opc":

                switch (args[1].toLowerCase ()){

                    case "setchest":
                        if (!sender.hasPermission ( "csvperm.opc.setchest" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }


                        Block csv = LocationUtils.getSelectedBlock ( player );
                        if(csv.getType () != Material.CHEST) {
                            sender.sendMessage ( "Il blocco target deve essere una chest !" );
                            return true;
                        }
                        Location loc = csv.getLocation ();
                        try {
                            String CityDelBlocco = TownyAPI.getInstance ().getTownBlock ( loc ).getTown ().getName ();
                            if(CSVaddon.listaCityConCSV.contains ( CityDelBlocco )){
                                sender.sendMessage ( "Questa citta' ha gia' una CSV !" );
                                return true;
                            }

                            CmdUtils.insTownCustomConfig ( CityDelBlocco,loc,sender );
                            CSVaddon.listaCityConCSV = new CmdUtils ().getListaCityConCSV ();
                            player.playSound ( player.getLocation (), Sound.ENTITY_SHULKER_OPEN, 3, 1  );

                        } catch (NotRegisteredException e) {
                            e.printStackTrace ();
                        }
                        return true;



                    case "removechest":
                        if (!sender.hasPermission ( "csvperm.opc.removechest" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        Block csv1 = LocationUtils.getSelectedBlock ( player );
                        if(csv1.getType () != Material.CHEST) {
                            sender.sendMessage ( "Il blocco target deve essere una chest !" );
                            return true;
                        }
                        Location loc1 = csv1.getLocation ();
                        try {
                            String CityDelBlocco = TownyAPI.getInstance ().getTownBlock ( loc1 ).getTown ().getName ();
                            if(!CSVaddon.listaCityConCSV.contains ( CityDelBlocco )){
                                sender.sendMessage ( "Questa citta' non ha una CSV !" );
                                return true;
                            }

                            CmdUtils.removeTownCustomConfig(CityDelBlocco,sender);
                            CSVaddon.listaCityConCSV = new CmdUtils ().getListaCityConCSV ();
                            player.playSound ( player.getLocation (), Sound.ENTITY_EGG_THROW, 3, 1  );


                        } catch (NotRegisteredException e) {
                            e.printStackTrace ();
                        }
                        return true;


                    case "chestinfo":
                        if (!sender.hasPermission ( "csvperm.opc.chestinfo" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if(args.length!=3){
                            sender.sendMessage ( "I parametri non sono inseriti correttamente !" );
                            return  true;
                        }

                        String SCVmyTownLoc = new CmdUtils ().getLocationCSV ( args[2] );
                        if(SCVmyTownLoc.isEmpty ())
                            sender.sendMessage ( "La citta' " + args[2] + " non possiede nessuna CSV !" );
                        else
                            sender.sendMessage ( SCVmyTownLoc );

                        return true;


                    case "chestlist":

                        if (!sender.hasPermission ( "csvperm.opc.chestlist" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if(CSVaddon.listaCityConCSV.isEmpty ()) {
                            sender.sendMessage ( "Attualmente nessuna citta' possiede una CSV !" );
                            return  true;
                        }

                        sender.sendMessage ( String.valueOf ( CSVaddon.listaCityConCSV ) );
                        return true;



                    case "sellall":

                        if (!sender.hasPermission ( "csvperm.opc.sellall" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if(CSVaddon.listaCityConCSV.isEmpty ()) {
                            sender.sendMessage ( "Attualmente nessuna citta' possiede una CSV !" );
                            return  true;
                        }

                        try {
                            SellUtils.sellall ( sender );
                        } catch (NotRegisteredException | EconomyException e) {
                            e.printStackTrace ();
                        }
                        return  true;


                    case "sell":

                        if (!sender.hasPermission ( "csvperm.opc.sellall" )){
                            sender.sendMessage ( "Non possiedi i permessi necessari !" );
                            return true;
                        }

                        if(args.length!=3){
                            sender.sendMessage ( "I parametri non sono inseriti correttamente !" );
                            return true;
                        }

                        if(!CSVaddon.listaCityConCSV.contains ( args[2] )) {
                            sender.sendMessage ( "La citta' " + args[2] + " non possiede una CVS !" );
                            return  true;
                        }

                        try {
                            SellUtils.sell ( args[2],sender );
                        } catch (NotRegisteredException | EconomyException e) {
                            e.printStackTrace ();
                        }
                        return true;


                    case "reload":
                        //ToDo


                    default:
                        return true;


                }



        }

        return false;
    }
}

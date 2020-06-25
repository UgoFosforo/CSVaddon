package me.architettoplugin.csvaddon.listener;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import me.architettoplugin.csvaddon.CSVaddon;
import me.architettoplugin.csvaddon.utils.CmdUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onDeleteTownEvent implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void nuovacity (DeleteTownEvent e){

        CSVaddon.listaCityPiano = new CmdUtils ().getTownList ();
        CmdUtils.AUTOremoveTownCustomConfig ( e.getTownName () );
        CSVaddon.listaCityConCSV.remove ( e.getTownName () );



    }

}

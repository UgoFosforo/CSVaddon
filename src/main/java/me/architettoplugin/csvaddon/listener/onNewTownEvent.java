package me.architettoplugin.csvaddon.listener;


import com.palmergames.bukkit.towny.event.NewTownEvent;
import me.architettoplugin.csvaddon.CSVaddon;
import me.architettoplugin.csvaddon.utils.CmdUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onNewTownEvent implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void deletecity (NewTownEvent e){
        CSVaddon.listaCityPiano = new CmdUtils ().getTownList ();
    }

}

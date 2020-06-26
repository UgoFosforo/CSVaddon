package me.architettoplugin.csvaddon.utils;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import me.architettoplugin.csvaddon.CSVaddon;
import org.bukkit.Bukkit;


public class MyTask extends Task {

    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {

        Bukkit.getScheduler().runTaskLater(CSVaddon.getPlugin(), new Runnable() {
            @Override
            public void run() {

                try {
                    SellUtils.sellall ();
                } catch (NotRegisteredException | EconomyException e) {
                    e.printStackTrace ();
                }


            }
        }, 20L);
    }


}


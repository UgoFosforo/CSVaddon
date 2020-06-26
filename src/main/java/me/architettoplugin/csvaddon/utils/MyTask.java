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
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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


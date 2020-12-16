package com.github.ms5984.clans.clansbanks.gui;

import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.hempcore.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BankMenu extends Menu {

    public BankMenu(Player player) {
        super(new GuiLibrary(player));
    }

    @Override
    public String getMenuName() {
        return null;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {

    }
}

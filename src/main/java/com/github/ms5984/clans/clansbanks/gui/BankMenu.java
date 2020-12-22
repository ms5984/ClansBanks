package com.github.ms5984.clans.clansbanks.gui;

import com.github.ms5984.clans.clansbanks.gui.util.MenuText;
import com.youtube.hempfest.hempcore.gui.GuiLibrary;
import com.youtube.hempfest.hempcore.gui.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BankMenu extends Menu { // TODO: finish gui for next release

    private static Map<Integer, ItemStack> items = new HashMap<>();

    public BankMenu(Player player) {
        super(new GuiLibrary(player));
    }

    @Override
    public String getMenuName() {
        return MenuText.MAIN_TITLE.toString();
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
    }

    @Override
    public void setMenuItems() {
    }

    private void initMap() {
        //        inventory.setItem(0, exit); // top-left slot
        items.putIfAbsent(13, makeItem(Material.BARRIER, MenuText.GLOBAL_EXIT.getRaw()));
    }
}

package com.github.ms5984.clans.clansbanks;

import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.commands.BankManager;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.github.ms5984.clans.clansbanks.gui.util.MenuText;
import com.github.ms5984.clans.clansbanks.model.BankEventsListener;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import com.github.ms5984.clans.clansbanks.model.Bank;
import com.github.ms5984.clans.clansbanks.util.Permissions;
import com.youtube.hempfest.clans.metadata.ClanMeta;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

public final class ClansBanks extends JavaPlugin implements BanksAPI {

    public static final int BANKS_META_ID = 100;
    private static ClansBanks instance;
    private Economy economy;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getConfig();
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        saveConfig();
        setupPermissions();
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Unable to load Vault!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.economy = rsp.getProvider();
        Messages.setup(this, getConfig().getString("lang"));
        MenuText.setup(this, getConfig().getString("lang"));
        getServer().getPluginManager().registerEvents(new BankManager(), this);
        getServer().getPluginManager().registerEvents(new BankEventsListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setupPermissions() {
        final Permission balance = new Permission(Permissions.BANKS_BALANCE.node);
        final Permission deposit = new Permission(Permissions.BANKS_DEPOSIT.node);
        final Permission withdraw = new Permission(Permissions.BANKS_WITHDRAW.node);
        final Permission use = new Permission(Permissions.BANKS_USE.node);
        balance.addParent(use, true);
        final Permission useStar = new Permission(Permissions.BANKS_USE_STAR.node);
        use.addParent(useStar, true);
        deposit.addParent(useStar, true);
        withdraw.addParent(useStar, true);
        final Permission star = new Permission(Permissions.BANKS_STAR.node);
        useStar.addParent(star, true);
        getServer().getPluginManager().addPermission(star);
        getServer().getPluginManager().addPermission(useStar);
        getServer().getPluginManager().addPermission(use);
        getServer().getPluginManager().addPermission(deposit);
        getServer().getPluginManager().addPermission(withdraw);
        getServer().getPluginManager().addPermission(balance);
    }

    @Override
    public ClanBank getBank(Clan clan) {
        HUID huid = clan.getId(BANKS_META_ID);
        final String clanId = clan.getClanID();
        if (huid == null) {
            final Bank bank = new Bank(clanId);
            getServer().getPluginManager().callEvent(new NewBankEvent(clan, bank));
            return bank;
        } else {
            ClanMeta meta = PersistentClan.loadTempInstance(huid);
            if (meta == null) {
                meta = PersistentClan.loadSavedInstance(huid);
                if (meta == null) {
                    final Bank bank = new Bank(clanId);
                    getServer().getPluginManager().callEvent(new NewBankEvent(clan, bank));
                    return bank;
                }
            }
            try {
                return (Bank) new HFEncoded(meta.value(MetaObject.BANK.id)).deserialized();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public BigDecimal defaultBalance() {
        return (BigDecimal) getConfig().get("default-balance");
    }

    @Override
    public LogLevel logToConsole() {
        final int anInt = getConfig().getInt("log-level");
        if (anInt < 0 || anInt > 2) {
            getLogger().severe("Invalid log level! Using api default 1 - Quiet");
            return BanksAPI.super.logToConsole();
        }
        return LogLevel.values()[anInt];
    }

    public static BanksAPI getAPI() {
        return instance;
    }

    public static Economy getEconomy() {
        return instance.economy;
    }

    public static Logger log() {
        return instance.getLogger();
    }
}

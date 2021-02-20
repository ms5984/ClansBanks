/*
 *  Copyright 2020 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  This file is part of ClansBanks.
 *
 *  ClansBanks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  ClansBanks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.clans.clansbanks;

import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.commands.BankManager;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.github.ms5984.clans.clansbanks.model.BankEventsListener;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import com.github.ms5984.clans.clansbanks.model.Bank;
import com.github.ms5984.clans.clansbanks.util.Permissions;
import com.github.sanctum.labyrinth.library.HFEncoded;
import com.github.sanctum.labyrinth.library.HUID;
import com.youtube.hempfest.clans.metadata.ClanMeta;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

public final class ClansBanks extends JavaPlugin implements BanksAPI {

    private static final int STATS_ID = 9743;
    public static final int BANKS_META_ID = 100;
    private static ClansBanks instance;
    private Economy economy;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        getConfig();
        setupPermissions();
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Unable to load Vault!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.economy = rsp.getProvider();
        getServer().getServicesManager().register(BanksAPI.class, this, this, ServicePriority.Normal);
        Messages.setup(this, getConfig().getString("lang"));
        getServer().getPluginManager().registerEvents(new BankManager(), this);
        getServer().getPluginManager().registerEvents(new BankEventsListener(), this);
        Metrics metrics = new Metrics(this, STATS_ID);
        metrics.addCustomChart(new Metrics.SimplePie("lang", () -> getConfig().getString("lang", "en-US")));
        metrics.addCustomChart(new Metrics.SimplePie("log_level", () -> String.valueOf(logToConsole().ordinal())));
        metrics.addCustomChart(new Metrics.SimplePie("starting_bank_balance", () -> startingBalance().toString())); // regex filter: \d+(\.\d*){0,1}
        metrics.addCustomChart(new Metrics.SimplePie("maximum_clan_balance", () -> {
            final BigDecimal maxBalance = maxBalance();
            if (maxBalance == null) return "None";
            return maxBalance.toString();
        }));
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
    public BigDecimal startingBalance() {
        final String string = getConfig().getString("default-balance");
        if (string == null) {
            getLogger().severe("Error reading default-balance, returning 0!");
        } else {
            try {
                return new BigDecimal(string);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                getLogger().severe("Improperly formatted default-balance!");
                getLogger().info("Using 0.");
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public @Nullable BigDecimal maxBalance() {
        final String string = getConfig().getString("maximum-balance");
        if (string != null) {
            try {
                return new BigDecimal(string);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                getLogger().severe("Improperly formatted maximum-balance!");
                getLogger().info("Maximum not set.");
            }
        }
        return null;
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

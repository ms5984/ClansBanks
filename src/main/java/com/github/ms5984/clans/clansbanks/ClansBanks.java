package com.github.ms5984.clans.clansbanks;

import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.commands.BankManager;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.github.ms5984.clans.clansbanks.model.BankEventsListener;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import com.github.ms5984.clans.clansbanks.model.Bank;
import com.youtube.hempfest.clans.metadata.ClanMeta;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.logging.Logger;

public final class ClansBanks extends JavaPlugin implements BanksAPI {

    public static final int BANKS_META_ID = 100;
    private static ClansBanks instance;
    private Economy economy;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getConfig().addDefaults(Collections.singletonMap("default-balance", BigDecimal.ZERO));
        saveConfig();
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Unable to load Vault!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.economy = rsp.getProvider();
        Messages.setup(this, null);
        getServer().getPluginManager().registerEvents(new BankManager(), this);
        getServer().getPluginManager().registerEvents(new BankEventsListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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

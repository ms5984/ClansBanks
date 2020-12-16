package com.github.ms5984.clans.clansbanks;

import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.github.ms5984.clans.clansbanks.model.Bank;
import com.youtube.hempfest.clans.metadata.ClanMeta;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

public final class ClansBanks extends JavaPlugin implements BanksAPI {

    public static final int META_ID = 100;
    private static ClansBanks instance;
    private Economy economy;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getConfig().addDefaults(Collections.singletonMap("default-balance", BigDecimal.ZERO));
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Unable to load Vault!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.economy = rsp.getProvider();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public ClanBank getBank(Clan clan) {
        HUID huid = clan.getId(META_ID);
        final String clanId = clan.getClanID();
        if (huid == null) {
            final PersistentClan persistentClan = new PersistentClan(clanId);
            persistentClan.storeTemp();
            new BukkitRunnable() {
                @Override
                public void run() {
                    persistentClan.saveMeta(META_ID);
                }
            }.runTask(this);
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
                return (Bank) new HFEncoded(meta.value()).deserialized();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        final Bank bank = new Bank(clanId);
        getServer().getPluginManager().callEvent(new NewBankEvent(clan, bank));
        return bank;
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
}

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
package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.events.AsyncNewBankEvent;
import com.github.sanctum.labyrinth.library.HFEncoded;
import com.github.sanctum.labyrinth.library.HUID;
import com.youtube.hempfest.clans.metadata.ClanMeta;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class BankMeta implements Serializable {
    public static final int BANKS_META_ID = 100;
    private static final long serialVersionUID = 4445662686153606368L;
    private static final Map<Clan, BankMeta> instances = new HashMap<>();
    private transient final JavaPlugin providingPlugin = JavaPlugin.getProvidingPlugin(BankMeta.class);
    private transient Clan clan;
    private final String clanId;
    private String bank = "";
    private String accessMap = "";
    private String bankLog = "";

    private BankMeta(Clan clan) {
        this.clan = clan;
        this.clanId = clan.getClanID();
        loadMetaFromClan();
    }

    public Clan getClan() {
        if (clan == null) {
            clan = Clan.clanUtil.getClan(clanId);
        }
        return clan;
    }

    public void storeBank(Bank bank) {
        try {
            this.bank = new HFEncoded(bank).serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        storeMetaToClan();
    }
    public void storeAccessMap(BankAction.AccessMap accessMap) {
        try {
            this.accessMap = new HFEncoded(accessMap).serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        storeMetaToClan();
    }
    public void storeBankLog(BankLog bankLog) {
        try {
            this.bank = new HFEncoded(bankLog).serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        storeMetaToClan();
    }

    public Optional<Bank> getBank() {
        if (bank.isEmpty()) {
            final Bank bank = new Bank(clanId);
            final Clan clan = getClan();
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new AsyncNewBankEvent(clan, bank));
                }
            }.runTaskAsynchronously(providingPlugin);
            return Optional.of(bank);
        }
        try {
            return Optional.ofNullable((Bank) new HFEncoded(bank).deserialized());
        } catch (IOException | ClassNotFoundException e) {
            providingPlugin.getLogger().severe("Unable to load clan bank file! Prepare for NPEs.");
            return Optional.empty();
        }
    }
    public Optional<BankAction.AccessMap> getAccessMap() {
        if (!this.accessMap.isEmpty()) {
            try {
                return Optional.ofNullable((BankAction.AccessMap) new HFEncoded(this.accessMap).deserialized());
            } catch (IOException | ClassNotFoundException e) {
                providingPlugin.getLogger().warning("Unable to load bank access map. Generating new.");
            }
        }
        return Optional.empty();
    }
    public Optional<BankLog> getBankLog() {
        if (!this.bankLog.isEmpty()) {
            try {
                return Optional.ofNullable((BankLog) new HFEncoded(this.bankLog).deserialized());
            } catch (IOException | ClassNotFoundException e) {
                providingPlugin.getLogger().warning("Unable to load bank log. Generating new.");
            }
        }
        return Optional.empty();
    }

    private synchronized void storeMetaToClan() {
        final HUID huid = getClan().getId(BANKS_META_ID);
        if (huid != null) PersistentClan.deleteInstance(huid);
        final PersistentClan persistentClan = new PersistentClan(clanId);
        persistentClan.setValue(this);
        persistentClan.storeTemp();
        persistentClan.saveMeta(BANKS_META_ID);
    }

    private synchronized void loadMetaFromClan() {
        final HUID huid = getClan().getId(BANKS_META_ID);
        if (huid != null) {
            ClanMeta meta = PersistentClan.loadTempInstance(huid);
            if (meta == null) {
                meta = PersistentClan.loadSavedInstance(huid);
            }
            try {
                try {
                    final String legacyFormat = meta.value(0);
                    if (legacyFormat != null) {
                        final Object deserialized = new HFEncoded(legacyFormat).deserialized();
                        if (deserialized instanceof Bank) {
                            final Bank legacyBank = (Bank) deserialized;
                            providingPlugin.getLogger().info(
                                    () -> String.format("Legacy bank converted for clanId=%s with balance %s",
                                        legacyBank.clanId,
                                        legacyBank.balance));
                            storeBank(legacyBank);
                            return;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    // this means we have the new format, proceed with normal loading
                }
                final BankMeta saved = (BankMeta) new HFEncoded(meta.value()).deserialized();
                this.bank = saved.bank;
                this.accessMap = saved.accessMap;
                this.bankLog = saved.bankLog;
            } catch (IOException | ClassNotFoundException e) {
                providingPlugin.getLogger().severe("Unable to load bank meta!");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankMeta bankMeta = (BankMeta) o;
        return clanId.equals(bankMeta.clanId) &&
                bank.equals(bankMeta.bank) &&
                accessMap.equals(bankMeta.accessMap) &&
                bankLog.equals(bankMeta.bankLog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clanId, bank, accessMap, bankLog);
    }

    public static BankMeta get(Clan clan) {
        return instances.computeIfAbsent(clan, BankMeta::new);
    }

    public static void clearManagerCache() {
        instances.clear();
    }
}
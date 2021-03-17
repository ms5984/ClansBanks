/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BankMeta implements Serializable {
    public static final int BANKS_META_ID = 100;
    private static final long serialVersionUID = 4445662686153606368L;
    private static final Map<Clan, BankMeta> INSTANCES = new ConcurrentHashMap<>();
    final transient JavaPlugin providingPlugin = JavaPlugin.getProvidingPlugin(BankMeta.class);
    transient Clan clan;
    final String clanId;
    String bank = "";
    String accessMap = "";
    String bankLog = "";

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
            providingPlugin.getLogger().warning(() -> "Unable to store bank for clanId" + clanId);
            providingPlugin.getLogger().warning(e::getMessage);
        }
        storeMetaToClan();
    }
    public void storeAccessMap(BankAction.AccessMap accessMap) {
        try {
            this.accessMap = new HFEncoded(accessMap).serialize();
        } catch (IOException e) {
            providingPlugin.getLogger().warning(() -> "Unable to store bank access map for clanId" + clanId);
            providingPlugin.getLogger().warning(e::getMessage);
        }
        storeMetaToClan();
    }
    public void storeBankLog(BankLog bankLog) {
        try {
            this.bankLog = new HFEncoded(bankLog).serialize();
        } catch (IOException e) {
            providingPlugin.getLogger().warning(() -> "Unable to store bank log for clanId" + clanId);
            providingPlugin.getLogger().warning(e::getMessage);
        }
        storeMetaToClan();
    }

    public Optional<Bank> getBank() {
        if (this.bank.isEmpty()) {
            final Bank newBankObject = new Bank(clanId);
            storeBank(newBankObject);
            final AsyncNewBankEvent event =
                    new AsyncNewBankEvent(Optional.ofNullable(clan).orElseGet(this::getClan), newBankObject);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(event);
                }
            }.runTaskAsynchronously(providingPlugin);
            return Optional.of(newBankObject);
        }
        try {
            return Optional.ofNullable((Bank) new HFEncoded(this.bank).deserialized());
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
        val metaId = getClan().getId(BANKS_META_ID);
        if (metaId != null) PersistentClan.deleteInstance(metaId);
        val persistentClan = new PersistentClan(clanId);
        persistentClan.setValue(this);
        persistentClan.storeTemp();
        persistentClan.saveMeta(BANKS_META_ID);
    }

    private synchronized void loadMetaFromClan() {
        final HUID metaId = getClan().getId(BANKS_META_ID);
        if (metaId != null) {
            ClanMeta meta = PersistentClan.loadTempInstance(metaId);
            if (meta == null) {
                meta = PersistentClan.loadSavedInstance(metaId);
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
                } catch (IndexOutOfBoundsException ignored) {
                    // this means we have the new format, proceed with normal loading
                }
                val saved = (BankMeta) new HFEncoded(meta.value()).deserialized();
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
        return INSTANCES.computeIfAbsent(clan, BankMeta::new);
    }

    public static void clearManagerCache() {
        INSTANCES.clear();
    }
}

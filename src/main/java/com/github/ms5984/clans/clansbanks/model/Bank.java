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

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.events.BankPreTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.BankSetBalanceEvent;
import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import com.github.sanctum.labyrinth.library.HUID;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public final class Bank implements ClanBank, Serializable {
    private static final long serialVersionUID = -5283828168295980464L;
    protected static final Economy ECO = ClansBanks.getEconomy();
    protected static final PluginManager PM = Bukkit.getServer().getPluginManager();
    protected BigDecimal balance;
    protected boolean enabled;
    protected final String clanId;
    private transient PersistentClan meta = null;

    public Bank(@NotNull String clanId) {
        this.balance = Objects.requireNonNull(ClansBanks.getAPI()).defaultBalance();
        this.enabled = true;
        this.clanId = clanId;
    }

    @Override
    public boolean deposit(Player player, BigDecimal amount) {
        if (!enabled) return false;
        final boolean has = ECO.has(player, player.getWorld().getName(), amount.doubleValue());
        final BankPreTransactionEvent preTransactionEvent =
                new BankPreTransactionEvent(player, this, amount, clanId, has, BankTransactionEvent.Type.DEPOSIT);
        PM.callEvent(preTransactionEvent);
        return preTransactionEvent.isSuccess();
    }

    @Override
    public boolean withdraw(Player player, BigDecimal amount) {
        if (!enabled) return false;
        final BankPreTransactionEvent preTransactionEvent =
                new BankPreTransactionEvent(player, this, amount, clanId, has(amount), BankTransactionEvent.Type.WITHDRAWAL);
        PM.callEvent(preTransactionEvent);
        return preTransactionEvent.isSuccess();
    }

    @Override
    public boolean has(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    @Override
    public double getBalanceDouble() {
        return balance.doubleValue();
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void setBalanceDouble(double newBalance) {
        ClanBank.super.setBalanceDouble(newBalance);
        PM.callEvent(new BankSetBalanceEvent(this, clanId, BigDecimal.valueOf(newBalance)));
    }

    @Override
    public void setBalance(BigDecimal newBalance) {
        ClanBank.super.setBalance(newBalance);
        PM.callEvent(new BankSetBalanceEvent(this, clanId, newBalance));
    }

    protected PersistentClan getMeta() {
        if (meta != null) {
            return meta;
        }
        final HUID huid = Clan.clanUtil.getClan(clanId).getId(ClansBanks.BANKS_META_ID);
        if (huid != null) {
            return meta = Objects.requireNonNull(PersistentClan.loadSavedInstance(huid));
        }
        return meta = new PersistentClan(clanId);
    }
}

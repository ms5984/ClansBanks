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

import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import com.youtube.hempfest.clans.util.construct.Clan;
import net.md_5.bungee.api.ChatColor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BankLog implements Serializable {
    private static final long serialVersionUID = -3400485111996318187L;

    public static final class Transaction implements Serializable {
        private static final long serialVersionUID = -679847970264259944L;
        public final String entity;
        public final BankTransactionEvent.Type type;
        public final BigDecimal amount;
        public final LocalDateTime localDateTime;

        public Transaction(String entity, BankTransactionEvent.Type type, BigDecimal amount) {
            this(entity, type, amount, LocalDateTime.now());
        }

        public Transaction(String entity, BankTransactionEvent.Type type, BigDecimal amount, LocalDateTime localDateTime) {
            this.entity = entity;
            this.type = type;
            this.amount = amount;
            this.localDateTime = localDateTime;
        }

        @Override
        public String toString() {
            return ChatColor.translateAlternateColorCodes('&',
                    String.format("&6%s %s &f{0} &7at &f%s".replace("{0}", amount.toString()),
                            entity,
                            (type == BankTransactionEvent.Type.DEPOSIT ? "&adeposited" : "&cwithdrew"),
                            localDateTime.format(DateTimeFormatter.ofPattern("h:mma '&7on&f' MMM dd',' yyyy"))));
        }
    }

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(BankTransactionEvent e) {
        transactions.add(new Transaction(e.getPlayer().getDisplayName(), e.getType(), e.getAmount()));
        saveForClan(e.getClan());
    }
    public void addTransaction(BankTransactionEvent e, LocalDateTime localDateTime) {
        transactions.add(new Transaction(e.getPlayer().getDisplayName(), e.getType(), e.getAmount(), localDateTime));
        saveForClan(e.getClan());
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void saveForClan(Clan clan) {
        BankMeta.get(clan).storeBankLog(this);
    }

    public static BankLog getForClan(Clan clan) {
        return BankMeta.get(clan).getBankLog().orElseGet(BankLog::new);
    }
}

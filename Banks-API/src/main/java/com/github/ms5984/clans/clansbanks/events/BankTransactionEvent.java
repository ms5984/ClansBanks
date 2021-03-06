/*
 *  Copyright 2020 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  This file is part of ClansBanks.
 *
 *  ClansBanks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  ClansBanks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.messaging.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class BankTransactionEvent extends BankActionEvent {

    public enum Type {
        DEPOSIT, WITHDRAWAL
    }

    protected final Player player;
    protected final BigDecimal amount;
    protected final boolean success;
    protected final Type type;

    public BankTransactionEvent(Player player, ClanBank clanBank, BigDecimal amount, String clanId, boolean success, Type type) {
        super(clanBank, clanId);
        this.player = player;
        this.amount = amount;
        this.success = success;
        this.type = type;
    }
    public BankTransactionEvent(BankTransactionEvent event) {
        super(event.clanBank, event.clanId);
        this.player = event.player;
        this.amount = event.amount;
        this.success = event.success;
        this.type = event.type;
    }

    /**
     * Get the player associated with this transaction.
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the ClanBank associated with this transaction.
     * @return the ClanBank
     */
    @Override
    public ClanBank getClanBank() {
        return super.getClanBank();
    }

    /**
     * Get the amount involved with this transaction.
     * @return a BigDecimal amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Denotes whether or not the transaction was successful.
     * @return true if successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * A transaction may constitute a deposit or withdrawal.
     * @return {@link Type} representing this transaction
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case DEPOSIT:
                return Message.TRANSACTION_DEPOSIT.toString()
                        .replace("{0}", this.success ? Message.TRANSACTION_SUCCESS.toString() : Message.TRANSACTION_FAILED.toString())
                        .replace("{1}", player.getName())
                        .replace("{2}", amount.toString())
                        .replace("{3}", getClan().getClanTag());
            case WITHDRAWAL:
                return Message.TRANSACTION_WITHDRAW.toString()
                        .replace("{0}", this.success ? Message.TRANSACTION_SUCCESS.toString() : Message.TRANSACTION_FAILED.toString())
                        .replace("{1}", player.getName())
                        .replace("{2}", amount.toString())
                        .replace("{3}", getClan().getClanTag());
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

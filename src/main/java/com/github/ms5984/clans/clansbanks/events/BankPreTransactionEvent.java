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
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class BankPreTransactionEvent extends AsyncBankTransactionEvent implements Cancellable {

    private boolean tempSuccess;
    private boolean cancelled;

    public BankPreTransactionEvent(Player player, ClanBank clanBank, BigDecimal amount, String clanId, boolean success, AsyncBankTransactionEvent.Type type) {
        super(player, clanBank, amount, clanId, success, type, false);
        this.tempSuccess = success;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        if (cancel && tempSuccess) { // don't flip cancel, this is meant to set success to false on cancel = true
            tempSuccess = false;
        }
    }

    @Override
    public boolean isSuccess() {
        return this.tempSuccess;
    }

    public void setSuccess(boolean success) {
        this.tempSuccess = success;
    }

    @Override
    public String toString() {
        switch (type) {
            case DEPOSIT:
                return (cancelled ? Messages.TRANSACTION_DEPOSIT_PRE_CANCELLED : Messages.TRANSACTION_DEPOSIT_PRE).toString()
                        .replace("{0}", (tempSuccess ? Messages.PRETRANSACTION_PENDING.toString() : Messages.PRETRANSACTION_FAILURE.toString()))
                        .replace("{1}", player.getName())
                        .replace("{2}", amount.toString())
                        .replace("{3}", getClan().getClanTag());
            case WITHDRAWAL:
                return (cancelled ? Messages.TRANSACTION_WITHDRAW_PRE_CANCELLED : Messages.TRANSACTION_WITHDRAW_PRE).toString()
                        .replace("{0}", (tempSuccess ? Messages.PRETRANSACTION_PENDING.toString() : Messages.PRETRANSACTION_FAILURE.toString()))
                        .replace("{1}", player.getName())
                        .replace("{2}", amount.toString())
                        .replace("{3}", getClan().getClanTag());
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}

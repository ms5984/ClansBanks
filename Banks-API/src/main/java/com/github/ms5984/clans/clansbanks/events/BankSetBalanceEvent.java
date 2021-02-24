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
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public final class BankSetBalanceEvent extends BankActionEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final BigDecimal newBalance;
    private boolean cancelled = false;

    public BankSetBalanceEvent(ClanBank clanBank, String clanId, BigDecimal newBalance) {
        super(clanBank, clanId);
        this.newBalance = newBalance;
    }

    /**
     * Get the potential new balance
     * @return the desired balance as a BigDecimal
     */
    public BigDecimal getNewBalance() {
        return newBalance;
    }

    /**
     * Get the potential new balance
     * @return the desired balance as a double
     */
    public double getNewBalanceAsDouble() {
        return newBalance.doubleValue();
    }

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
    }
}

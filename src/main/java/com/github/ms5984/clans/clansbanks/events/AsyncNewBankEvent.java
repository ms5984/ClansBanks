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
import com.youtube.hempfest.clans.util.construct.Clan;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public final class AsyncNewBankEvent extends ClansBanksEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Clan clan;
    private final BigDecimal startingBalance;

    public AsyncNewBankEvent(Clan clan, ClanBank clanBank) {
        super(clanBank, true);
        this.clan = clan;
        this.startingBalance = clanBank.getBalance();
    }

    /**
     * Get the clan whose bank was just created
     * @return the Clan
     */
    public Clan getClan() {
        return clan;
    }

    /**
     * Get the starting balance of the bank (usually 0 per default configuration).
     * @return the initial balance
     */
    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

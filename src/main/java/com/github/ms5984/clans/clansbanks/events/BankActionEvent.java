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

import java.util.concurrent.CompletableFuture;

public abstract class BankActionEvent extends ClansBanksEvent {

    protected final String clanId;

    protected BankActionEvent(ClanBank clanBank, String clanId, boolean async) {
        super(clanBank, async);
        this.clanId = clanId;
    }

    /**
     * Get the direct clanId for this event
     * @return clanId as String
     */
    public String getClanId() {
        return clanId;
    }

    /**
     * Get the clan associated with this bank event
     * @return the Clan whose bank this is
     */
    public Clan getClan() {
        return CompletableFuture.supplyAsync(() -> Clan.clanUtil.getClan(clanId)).join();
    }
}

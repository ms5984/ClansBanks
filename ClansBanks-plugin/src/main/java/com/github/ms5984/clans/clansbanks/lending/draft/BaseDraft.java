/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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
package com.github.ms5984.clans.clansbanks.lending.draft;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public abstract class BaseDraft {
    protected final ClanBank clanBank;
    protected BigDecimal principal;
    private boolean callable;

    protected BaseDraft(@NotNull ClanBank clanBank, @NotNull BigDecimal principal) {
        this.clanBank = clanBank;
        this.principal = principal;
    }

    public ClanBank getBank() {
        return clanBank;
    }

    public BigDecimal principal() {
        return principal;
    }

    public void setPrincipal(@NotNull BigDecimal newPrincipal) {
        this.principal = newPrincipal;
    }

    public boolean isCallable() {
        return callable;
    }

    public void setCallable(boolean canCall) {
        this.callable = canCall;
    }
}

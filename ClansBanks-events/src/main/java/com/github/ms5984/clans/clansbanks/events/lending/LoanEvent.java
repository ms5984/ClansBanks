/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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
package com.github.ms5984.clans.clansbanks.events.lending;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import com.github.ms5984.clans.clansbanks.events.BankActionEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base for all loan events.
 */
public abstract class LoanEvent extends BankActionEvent {
    protected Loan loan;

    protected LoanEvent(ClanBank clanBank, String clanId, Loan loan) {
        super(clanBank, clanId);
        this.loan = loan;
    }

    /**
     * Get the loan involved with this event.
     *
     * @return loan object (may be unset)
     */
    @Nullable
    public Loan getLoan() {
        return loan;
    }
}

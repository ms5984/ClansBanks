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
package com.github.ms5984.clans.clansbanks.lending;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Base class for loan implementations.
 */
public abstract class AbstractLoan implements Loan {

    protected final ClanBank clanBank;
    protected final BigDecimal principal;
    protected BigDecimal currentBalance;

    protected AbstractLoan(@NotNull ClanBank clanBank, @NotNull BigDecimal principal) {
        this.clanBank = Objects.requireNonNull(clanBank);
        this.principal = Objects.requireNonNull(principal);
    }

    @Override
    public ClanBank getBank() {
        return clanBank;
    }

    @Override
    public BigDecimal principal() {
        return principal;
    }

    @Override
    public void makePayment(BigDecimal amount, Consumer<Boolean> callback) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("You cannot make negative payments.");
    }

    @Override
    public BigDecimal remainingBalance() {
        return currentBalance;
    }
}

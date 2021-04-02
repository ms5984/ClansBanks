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

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import com.youtube.hempfest.clans.util.construct.Clan;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Base class for loan implementations.
 */
@SuppressWarnings("serial")
public abstract class AbstractLoan implements Loan, Serializable {

    protected final BigDecimal principal;
    protected BigDecimal currentBalance;
    protected String clanId;

    protected AbstractLoan(@NotNull BigDecimal principal, @Nullable String clanId) {
        this.principal = Objects.requireNonNull(principal);
        this.clanId = clanId;
    }

    @Override
    public ClanBank getBank() {
        return Optional.ofNullable(Clan.clanUtil.getClan(clanId))
                .map(ClansBanks.getAPI()::getBank)
                .orElse(null);
    }

    @Override
    public BigDecimal principal() {
        return principal;
    }

    @Override
    public void makePayment(@NotNull BigDecimal amount, Function<BigDecimal, Boolean> check, Consumer<BigDecimal> callback) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("You cannot make negative payments.");
        if (currentBalance.compareTo(amount) < 0) {
            // do not allow overpay
            callback.accept(null);
            return;
        }
        if (check.apply(amount)) {
            // has amount, update balance + collect amount
            currentBalance = currentBalance.subtract(amount);
            callback.accept(amount);
        }
    }

    @Override
    public BigDecimal remainingBalance() {
        return currentBalance;
    }
}

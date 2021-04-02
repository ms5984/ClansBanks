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

import com.github.ms5984.clans.clansbanks.api.lending.HasInterest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class for loans with periodic interest collection.
 */
public final class PeriodicInterestLoan extends AbstractLoan implements HasInterest {

    private static final long serialVersionUID = -4920729108108931359L;
    protected BigDecimal interestRate;
    protected long period;
    protected long lastInterest = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    protected long tempInterest = 0L;

    protected PeriodicInterestLoan(@NotNull BigDecimal principal,
                                   @NotNull BigDecimal interestRate,
                                   long periodInSeconds,
                                   @Nullable String clanId) {
        super(principal, clanId);
        this.interestRate = interestRate;
        this.period = periodInSeconds;
    }

    @Override
    public BigDecimal calculateInterest() {
        return currentBalance.multiply(interestRate);
    }

    @Override
    public void collectInterest(Function<BigDecimal, Boolean> check, Consumer<BigDecimal> callback) {
        tempInterest = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if (tempInterest - lastInterest > period) {
            lastInterest += period; // Slowly catch up if server has been offline for a while.
            final BigDecimal calc = calculateInterest();
            if (check.apply(calc)) {
                // take interest
                callback.accept(calc);
            } else {
                // capitalize interest
                currentBalance = currentBalance.add(calc);
            }
        }
    }

    @Override
    public long getPeriodInSeconds() {
        return period;
    }
}

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Base class for loans with periodic interest collection.
 */
@SuppressWarnings("serial")
public abstract class PeriodicInterestLoan extends AbstractLoan implements HasInterest {

    protected BigDecimal interestRate;
    protected long period;
    protected long lastInterest = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    protected long tempInterest = 0L;

    protected PeriodicInterestLoan(@NotNull BigDecimal principal,
                                   @NotNull BigDecimal interestRate,
                                   long periodInSeconds) {
        super(principal);
        this.interestRate = interestRate;
        this.period = periodInSeconds;
    }

    @Override
    public BigDecimal calculateInterest() {
        return currentBalance.multiply(interestRate);
    }

    @Override
    public long getPeriodInSeconds() {
        return period;
    }
}

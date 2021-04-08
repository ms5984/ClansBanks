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
import com.github.ms5984.clans.clansbanks.api.lending.HasInterestDraft;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Represents a draft for a loan with interest.
 */
public final class InterestLoanDraft extends BaseDraft implements HasInterestDraft {

    private BigDecimal interestRate;
    private long periodInSeconds;

    public InterestLoanDraft(@NotNull ClanBank clanBank,
                             @NotNull BigDecimal principal,
                             BigDecimal interestRate,
                             long periodInSeconds) {
        super(clanBank, principal);
        this.interestRate = (interestRate == null) ? BigDecimal.ZERO : interestRate;
        this.periodInSeconds = periodInSeconds;
    }

    @Override
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    @Override
    public void setInterestRate(@NotNull BigDecimal interestRate) throws IllegalArgumentException {
        if (interestRate.compareTo(BigDecimal.ZERO) < 0 || interestRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Invalid interest rate!");
        }
        this.interestRate = interestRate;
    }

    @Override
    public long getPeriodInSeconds() {
        return periodInSeconds;
    }

    @Override
    public void setPeriodInSeconds(long periodInSeconds) {
        this.periodInSeconds = periodInSeconds;
    }
}

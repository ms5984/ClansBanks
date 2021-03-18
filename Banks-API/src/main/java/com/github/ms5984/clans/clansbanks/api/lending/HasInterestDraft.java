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
package com.github.ms5984.clans.clansbanks.api.lending;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Describes a potential instrument that will accrue interest.
 */
public interface HasInterestDraft {
    /**
     * Get the interest rate per period.
     *
     * @return interest rate as BigDecimal
     */
    BigDecimal getInterestRate();

    /**
     * Set the interest rate per period.
     *
     * @param interestRate new interest rate as BigDecimal
     * @throws IllegalArgumentException if rate less than 0 or greater than 1.
     */
    void setInterestRate(@NotNull BigDecimal interestRate) throws IllegalArgumentException;

    /**
     * Return the frequency in seconds of interest accrual.
     */
    long getPeriodInSeconds();

    /**
     * Set the frequency in seconds of interest accrual.
     * <p>
     * Interest will be paid after each elapsed period.
     *
     * @param periodInSeconds amount of time in seconds
     */
    void setPeriodInSeconds(long periodInSeconds);
}

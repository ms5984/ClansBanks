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

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Describes an instrument that accrues interest.
 */
public interface HasInterest {
    /**
     * Calculate the interest for the loan.
     *
     * @return calculated interest
     */
    BigDecimal calculateInterest();

    /**
     * Depends on perspective; could be income or expense.
     *
     * @param callback logic to run with completion status
     */
    void collectInterest(Consumer<Boolean> callback);

    /**
     * Return the frequency in seconds of interest accrual.
     */
    long getPeriodInSeconds();
}

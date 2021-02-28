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

import com.github.ms5984.clans.clansbanks.api.ClanBank;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Describes a loan held by a clan bank.
 */
public interface Loan {
    /**
     * Get the clan bank that holds this loan.
     *
     * @return the clan bank that holds the loan
     */
    ClanBank getBank();

    /**
     * Returns the nominal principal of the loan.
     *
     * @return as BigDecimal
     */
    BigDecimal principal();

    /**
     * Returns the nominal principal of the loan.
     * <p>
     * May return {@link Double#POSITIVE_INFINITY}
     * if BigDecimal value is out of range.
     *
     * @return principal as a double
     */
    default double principalDouble() {
        return principal().doubleValue();
    }

    /**
     * Make a payment to the loan.
     *
     * @param amount payment amount as BigDecimal
     * @param callback function to call with success/fail
     */
    void makePayment(BigDecimal amount, Consumer<Boolean> callback);

    /**
     * Get the current remaining balance.
     *
     * @return principal plus interest/fees less payments
     */
    BigDecimal remainingBalance();
}

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
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Describes a potential loan.
 */
public interface LoanDraft {
    /**
     * Get the clan bank that will hold this loan.
     *
     * @return the clan bank that will hold the loan
     */
    ClanBank getBank();

    /**
     * Returns the nominal principal of the draft loan.
     *
     * @return as BigDecimal
     */
    BigDecimal principal();

    /**
     * Set a different principal amount.
     *
     * @param newPrincipal amount as BigDecimal
     */
    void setPrincipal(@NotNull BigDecimal newPrincipal);

    /**
     * Returns the nominal principal of the draft loan.
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
     * Set a different principal amount.
     *
     * @param newPrincipal amount as a double
     */
    default void setPrincipalDouble(double newPrincipal) {
        setPrincipal(BigDecimal.valueOf(newPrincipal));
    }

    /**
     * Whether or not the loan can be called.
     * <p>
     * Defaults to false.
     *
     * @return whether the loan can be called
     */
    boolean isCallable();

    /**
     * Set whether the loan can be called.
     *
     * @param canCall whether the loan can be called
     */
    void setCallable(boolean canCall);
}

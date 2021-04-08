/*
 *  This file is part of Banks-API.
 *
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

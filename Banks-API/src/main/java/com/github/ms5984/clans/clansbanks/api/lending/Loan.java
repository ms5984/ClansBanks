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
import java.util.function.Consumer;
import java.util.function.Function;

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
     * <p>
     * If callback receives null, amount provided exceeded
     * owed balance and was rejected.
     *
     * @param amount payment amount as BigDecimal
     * @param check logic to test for payment amount
     * @param callback logic to collect amount
     */
    void makePayment(@NotNull BigDecimal amount, Function<BigDecimal, Boolean> check, Consumer<BigDecimal> callback);

    /**
     * Get the current remaining balance.
     *
     * @return principal plus interest/fees less payments
     */
    BigDecimal remainingBalance();
}

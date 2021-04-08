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

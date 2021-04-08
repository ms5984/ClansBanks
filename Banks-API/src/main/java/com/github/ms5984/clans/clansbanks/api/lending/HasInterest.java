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

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Function;

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
     * <p>
     * This method does nothing if the interest is not being collected.
     *
     * @param check logic to run to verify payer can pay
     * @param callback logic to collect interest
     */
    void collectInterest(Function<BigDecimal, Boolean> check, Consumer<BigDecimal> callback);

    /**
     * Return the frequency in seconds of interest accrual.
     */
    long getPeriodInSeconds();
}

/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *
 *  This file is part of Banks-API.
 *
 *  Banks-API is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  Banks-API is distributed in the hope that it will be useful,
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
import java.util.function.Function;

/**
 * Describes an instrument that has a fee.
 */
public interface HasFee {
    /**
     * Get the amount of the fee.
     *
     * @return fee as BigDecimal
     */
    BigDecimal getFee();

    /**
     * Pay the fee. This method does nothing if {@link #feePaid()} is true.
     *
     * @param check logic to run to verify payer can pay
     * @param callback logic to collect fee
     */
    void payFee(Function<BigDecimal, Boolean> check, Consumer<BigDecimal> callback);

    /**
     * Returns true if the fee has already been paid.
     *
     * @return true; otherwise false
     */
    boolean feePaid();
}

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
 * Describe a potential instrument that will have a fee.
 */
public interface HasFeeDraft {
    /**
     * Get the amount of the fee.
     *
     * @return fee as BigDecimal
     */
    BigDecimal getFee();

    /**
     * Set the fee amount.
     *
     * @param newFee amount as BigDecimal
     */
    void setFee(@NotNull BigDecimal newFee);
}

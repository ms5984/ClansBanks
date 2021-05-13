/*
 *  This file is part of ClansBanks-events.
 *
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License or
 *  the license mentioned above.
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
package com.github.ms5984.clans.clansbanks.events.lending;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Called when a new loan is successfully created.
 */
public final class NewLoanEvent extends LoanEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public NewLoanEvent(ClanBank clanBank, String clanId, @NotNull Loan loan) {
        super(clanBank, clanId, Objects.requireNonNull(loan));
    }

    @Override
    public @NotNull Loan getLoan() {
        return loan;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

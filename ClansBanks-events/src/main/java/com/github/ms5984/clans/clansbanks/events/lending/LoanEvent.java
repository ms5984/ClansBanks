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
import com.github.ms5984.clans.clansbanks.events.BankActionEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base for all loan events.
 */
public abstract class LoanEvent extends BankActionEvent {
    protected Loan loan;

    protected LoanEvent(ClanBank clanBank, String clanId, Loan loan) {
        super(clanBank, clanId);
        this.loan = loan;
    }

    /**
     * Get the loan involved with this event.
     *
     * @return loan object (may be unset)
     */
    @Nullable
    public Loan getLoan() {
        return loan;
    }
}

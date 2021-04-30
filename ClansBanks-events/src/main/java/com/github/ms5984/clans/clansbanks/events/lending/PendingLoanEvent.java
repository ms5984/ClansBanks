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
import com.github.ms5984.clans.clansbanks.api.lending.LoanDraft;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a loan is requested. Triggers logic regarding
 * underwriting, etc.
 */
public final class PendingLoanEvent extends LoanEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LoanDraft loanDraft;
    private boolean cancelled;

    public PendingLoanEvent(ClanBank clanBank, String clanId, LoanDraft loanDraft) {
        super(clanBank, clanId, null);
        this.loanDraft = loanDraft;
    }

    /**
     * Get the draft loan object.
     * <p>
     * Changes in the draft loan will update the final loan.
     * <p>
     * Note that by the time this event is called the loan
     * draft will already be determined to be fee-based or
     * interest-based and cannot be changed.
     *
     * @return draft of loan
     */
    public LoanDraft getLoanDraft() {
        return loanDraft;
    }

    /**
     * Set the final loan object contained in this event.
     * <p>
     * A null value will clear the current object.
     *
     * @param loan final loan object
     */
    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

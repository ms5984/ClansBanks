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
     *
     * @return draft loan
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

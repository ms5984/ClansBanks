package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import com.github.ms5984.clans.clansbanks.api.lending.LoanHolder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Meta container for storing loans.
 */
public final class Portfolio implements LoanHolder, Serializable {
    private static final long serialVersionUID = 4518336119936312718L;
    private final Set<Loan> loans = new HashSet<>();

    @Override
    public @NotNull Set<Loan> getLoans() {
        return loans;
    }
}

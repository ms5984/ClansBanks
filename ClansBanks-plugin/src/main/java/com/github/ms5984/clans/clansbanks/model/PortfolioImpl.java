package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import com.github.ms5984.clans.clansbanks.api.lending.Portfolio;
import com.github.ms5984.clans.clansbanks.lending.AbstractLoan;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Meta container for storing loans.
 */
public final class PortfolioImpl implements Portfolio, Serializable {
    private static final long serialVersionUID = 4518336119936312718L;
    private final Set<AbstractLoan> loans = new HashSet<>();

    @Override
    public @NotNull Set<Loan> getLoans() {
        return Collections.unmodifiableSet(loans);
    }

    @Override
    public void addNewLoan(Loan loan) {
        if (loan instanceof AbstractLoan) loans.add((AbstractLoan) loan);
        throw new IllegalArgumentException("Loans from external plugins not supported!");
    }
}

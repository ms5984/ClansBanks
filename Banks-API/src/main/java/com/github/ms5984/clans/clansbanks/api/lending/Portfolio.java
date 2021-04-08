package com.github.ms5984.clans.clansbanks.api.lending;

/**
 * Store and perform operations on a group of loans.
 */
public interface Portfolio extends LoanHolder {
    /**
     * Add a new loan to this portfolio.
     */
    void addNewLoan(Loan loan);
}

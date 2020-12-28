package com.github.ms5984.clans.clansbanks.api;

public interface CallableLoan extends Loan {
    /**
     * Call the loan
     *
     * @return true if successful; otherwise false
     */
    boolean call();
}

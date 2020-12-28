package com.github.ms5984.clans.clansbanks.api;

import java.math.BigDecimal;

public interface Loan {
    /**
     * Get the bank this loan belongs to.
     * @return
     */
    ClanBank getBank();

    /**
     * Returns the nominal principal of the loan.
     * @return principal as BigDecimal
     */
    BigDecimal principal();

    /**
     * Returns the nominal principal of the loan.
     *
     * @return principal as a double
     */
    default double principalDouble() {
        return principal().doubleValue();
    }
}

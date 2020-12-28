package com.github.ms5984.clans.clansbanks.api;

import java.math.BigDecimal;
import java.util.function.Consumer;

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

    /**
     * Make a payment to the loan.
     * @param amount payment amount as BigDecimal
     * @param callback function to call with success/fail
     */
    void makePayment(BigDecimal amount, Consumer<Boolean> callback);

    /**
     * Get the current remaining balance.
     * @return principal plus interest/fees less payments
     */
    BigDecimal remainingBalance();
}

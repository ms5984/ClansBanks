package com.github.ms5984.clans.clansbanks.api;

import org.bukkit.entity.Player;

import java.math.BigDecimal;

public interface ClanBank {
    /**
     * Take an amount from the player and deposit into the bank
     * @return true if successful
     */
    boolean deposit(Player player, BigDecimal amount);

    /**
     * Withdraw an amount from the bank and give to the player
     * @return true if successful
     */
    boolean withdraw(Player player, BigDecimal amount);

    /**
     * Check if the bank has an amount
     * @return true if the bank has at least amount
     */
    boolean has(BigDecimal amount);

    /**
     * Get the balance of the bank
     * @return balance as double
     */
    double getBalanceDouble();

    /**
     * Get the balance of the bank
     * @return balance as BigDecimal
     */
    BigDecimal getBalance();

    /**
     * Set the balance of the bank
     * @param newBalance the desired balance as a double
     */
    void setBalanceDouble(double newBalance);

    /**
     * Set the balance of the bank
     * @param newBalance the desired balance as BigDecimal
     */
    void setBalance(BigDecimal newBalance);
}

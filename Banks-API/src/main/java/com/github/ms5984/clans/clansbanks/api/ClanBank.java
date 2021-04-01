/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  This file is part of Banks-API.
 *
 *  Banks-API is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  Banks-API is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.clans.clansbanks.api;

import com.github.ms5984.clans.clansbanks.api.lending.LoanHolder;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

/**
 * The public API for Clan banks.
 */
public interface ClanBank extends LoanHolder {
    /**
     * Take an amount from the player and deposit into the bank.
     *
     * @param player player to take amount from
     * @param amount amount to deposit
     * @return true if successful
     */
    boolean deposit(Player player, BigDecimal amount);

    /**
     * Withdraw an amount from the bank and give to the player.
     *
     * @param player player to give amount to
     * @param amount amount to withdraw
     * @return true if successful
     */
    boolean withdraw(Player player, BigDecimal amount);

    /**
     * Check if the bank has an amount.
     *
     * @param amount amount to test
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
     *
     * @param newBalance the desired balance as a double
     * @throws IllegalArgumentException if desired balance is negative
     */
    default void setBalanceDouble(double newBalance) {
        if (newBalance < 0d) throw new IllegalArgumentException();
    }

    /**
     * Set the balance of the bank
     *
     * @param newBalance the desired balance as BigDecimal
     * @throws IllegalArgumentException if desired balance is negative
     */
    default void setBalance(BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException();
    }

    // NEW as of v1.2.0

    /**
     * Get the net worth of the bank.
     *
     * @return the net worth of the bank
     */
    default BigDecimal getBankValue() {
        return getAssets().add(getLiabilities());
    }

    /**
     * Get the value of the bank's assets.
     *
     * @return value of the bank's assets
     */
    BigDecimal getAssets();

    /**
     * Get the value of the bank's liabilities.
     * <p>
     * Returns zero or a negative value.
     *
     * @return value of the bank's liabilities
     */
    BigDecimal getLiabilities();
}
